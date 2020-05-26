package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.exception.place.PlaceBadRequestException;
import com.lits.tovisitapp.exception.place.PlaceNotFoundException;
import com.lits.tovisitapp.googleplaces.exception.GooglePlacesApiException;
import com.lits.tovisitapp.googleplaces.parser.GooglePlacesResponseParser;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchStatus;
import com.lits.tovisitapp.googleplaces.type.SearchableType;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Type;
import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.repository.TypeRepository;
import com.lits.tovisitapp.service.PlaceService;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@PropertySource("googlePlaces.properties")
@Log
public class PlaceServiceImpl implements PlaceService {

	private PlaceRepository placeRepository;
	private TypeRepository typeRepository;
	private ModelMapper mapper;
	private RestTemplate httpClient;
	private GooglePlacesResponseParser responseParser;

	@Value("${uri.findPlacesNearby}")
	private String uriFindPlacesNearby;

	@Value("${uri.findPlacesByText}")
	private String uriFindPlacesByText;

	@Value("${uri.findPlaceById}")
	private String uriFindPlaceById;

	@Value("${apiKey}")
	private String apiKey;

	@Value("${retriesOnErrors}")
	private int retriesOnErrors;

	@Value("${nextPageSearch.tries}")
	private int nextPageSearchTries;

	@Value("${nextPageSearch.sleepMs}")
	private int nextPageSearchSleepMs;

	public PlaceServiceImpl(
			ModelMapper mapper,
			PlaceRepository placeRepository,
			TypeRepository typeRepository,
			RestTemplate httpClient,
			GooglePlacesResponseParser responseParser) {
		this.mapper = mapper;
		this.placeRepository = placeRepository;
		this.typeRepository = typeRepository;
		this.httpClient = httpClient;
		this.responseParser = responseParser;
	}

	@Override
	public PlacesSearchResponse findNearby(
			PlacesSearchCircle circle,
			SearchableType type,
			boolean obtainParentLocation) {
		if (circle == null) {
			throw new PlaceBadRequestException("Search circle cannot be null");
		}
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesNearby)
				.queryParam("location", circle.getLatitude() + "," + circle.getLongitude())
				.queryParam("radius", circle.getRadius());
		if (type != null) {
			uriBuilder = uriBuilder.queryParam("type", type.name().toLowerCase());
		}
		uriBuilder = uriBuilder.queryParam("key", apiKey);

		log.info("find place nearby: circle: " + circle.toString()
				+ (type != null ? " and type: '" + type.name().toLowerCase() + "'" : ""));
		return searchPlaces(uriBuilder.build().toString(), obtainParentLocation, false);
	}

	//<editor-fold desc="Exposed methods">
	@Override
	public PlacesSearchResponse findByText(
			String query,
			PlacesSearchCircle circle,
			SearchableType type,
			boolean obtainParentLocation) {
		if (query == null || query.isBlank()) {
			throw new PlaceBadRequestException("Search query cannot be null or blank");
		}

		DefaultUriBuilderFactory builderFactory = new DefaultUriBuilderFactory();
		builderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesByText)
				.queryParam("query", query);
		if (circle != null)  {
			uriBuilder = uriBuilder
					.queryParam("location", circle.getLatitude(), circle.getLongitude())
					.queryParam("radius", circle.getRadius());
		}
		if (type != null) {
			uriBuilder = uriBuilder.queryParam("type", type.name().toLowerCase());
		}
		uriBuilder = uriBuilder.queryParam("key", apiKey);
		log.info("find place by text: with query: '" + query + "'"
				+ (circle != null ? " and circle: " + circle.toString() : "")
				+ (type != null ? " and type: '" + type.name().toLowerCase() + "'" : ""));
		return searchPlaces(uriBuilder.build().toString(), obtainParentLocation, false);
	}

	@Override
	public PlacesSearchResponse findNextPage(String pageToken, boolean obtainParentLocation) {
		if (pageToken == null || pageToken.isBlank()) {
			throw new PlaceBadRequestException("Next page token cannot be null");
		}
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesByText)
				.queryParam("pagetoken", pageToken)
				.queryParam("key", apiKey);

		log.info("find next page: token: '" + pageToken + "'");
		return searchPlaces(uriBuilder.build().toString(), obtainParentLocation, true);
	}

	@Override
	public PlaceDTO findPlaceById(long placeId) {
		if (placeId <= 0) {
			throw new PlaceBadRequestException("Place id must be positive");
		}
		Place foundPlace = placeRepository.findById(placeId).orElseThrow(
				() -> new PlaceNotFoundException("Place with id " + placeId + " not found"));
		return mapper.map(foundPlace, PlaceDTO.class);
	}

	@Override
	public PlaceDTO savePlace(PlaceDTO submittedPlaceDTO) {
		if (submittedPlaceDTO == null) {
			throw new PlaceBadRequestException("Place cannot be null");
		}

		Place submittedPlace = mapper.map(submittedPlaceDTO, Place.class);

		if (!submittedPlace.getTypes().isEmpty()) {
			List<Type> foundTypes = typeRepository.findByNameIn(submittedPlaceDTO.getTypes());
			Set<Type> updatedTypes = submittedPlace.getTypes().stream().map(st ->
					foundTypes.stream()
							.filter(ft -> ft.getName().equalsIgnoreCase(st.getName()))
							.findFirst().orElse(st)).collect(Collectors.toSet());
			submittedPlace.setTypes(updatedTypes);
		}

		Place savedPlace = placeRepository.save(submittedPlace);
		return mapper.map(savedPlace, PlaceDTO.class);
	}

	@Override
	public void deletePlace(long placeId) {

		placeRepository.deleteById(placeId);
	}
	//</editor-fold>

	//<editor-fold desc="Inner methods">
	private PlacesSearchResponse searchPlaces(String URI, boolean obtainParentLocation, boolean isNextPageRequest) {
		PlacesSearchResponse response;
		int tries = nextPageSearchTries;
		loop: do {
			--tries;
			String responseJson = executeRequestWithRetriesOnError(URI);
			PlacesSearchStatus status = responseParser.extractStatus(responseJson);
			response = PlacesSearchResponse.builder()
					.nextPageToken(responseParser.extractNextPageToken(responseJson))
					.places(responseParser.extractPlaces(responseJson))
					.build();

			switch (status) {
				case INVALID_REQUEST:
					// next page request can return 'invalid request' when page's data not ready yet, should wait and retry
					if (isNextPageRequest && tries > 0) {
						try {
							Thread.sleep(nextPageSearchSleepMs);
							continue;
						} catch (InterruptedException ie) {
							throw new GooglePlacesApiException("Interrupted while retrieving places");
						}
					}
				case OVER_QUERY_LIMIT:
				case REQUEST_DENIED:
				case UNKNOWN_ERROR:
				default:
					throw new GooglePlacesApiException("Error while retrieving places: " + status.description);
				case NOT_FOUND:
				case ZERO_RESULTS:
				case OK:
					break loop;
			}
		} while (true);

		if (obtainParentLocation) {
			response.getPlaces().stream().parallel().forEach(
					p -> p.setParentLocation(obtainParentLocation(p.getGooglePlaceId())));
		}

		return response;
	}

	private String obtainParentLocation(String googlePlaceId) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(uriFindPlaceById)
				.queryParam("placeid", googlePlaceId)
				.queryParam("fields", "address_component")
				.queryParam("key", apiKey);

		log.info("obtain parent location for place: " + googlePlaceId);
		String placeDetailsResponse = executeRequestWithRetriesOnError(uriBuilder.build().toString());
		PlacesSearchStatus status = responseParser.extractStatus(placeDetailsResponse);
		switch (status) {
			case OVER_QUERY_LIMIT:
			case REQUEST_DENIED:
			case INVALID_REQUEST:
			case UNKNOWN_ERROR:
			default:
				throw new GooglePlacesApiException("Error while retrieving place details: " + status.description);
			case NOT_FOUND:
			case ZERO_RESULTS:
				throw new PlaceNotFoundException("Place id: " + googlePlaceId + " not found");
			case OK:
				// acceptable status
				break;
		}
		return responseParser.extractParentLocation(placeDetailsResponse);
	}

	private String executeRequestWithRetriesOnError(String URI) {
		int tries = retriesOnErrors;
		do {
			--tries;
			ResponseEntity<String> responseEntity;
			try {
				responseEntity = httpClient.exchange(URI, HttpMethod.GET, null, String.class);
				return responseEntity.getBody();
			} catch (HttpClientErrorException | ResourceAccessException e) {
				if (tries == 0 || e instanceof HttpClientErrorException && ((HttpClientErrorException)e).getRawStatusCode() < 500 ) {
					throw new GooglePlacesApiException("Error while making http request to GooglePlaces", e);
				}
			}
		} while (true);
	}
	//</editor-fold>
}
