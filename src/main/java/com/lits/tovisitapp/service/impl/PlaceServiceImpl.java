package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.exception.place.PlaceBadRequestException;
import com.lits.tovisitapp.googleplaces.exception.GooglePlacesApiException;
import com.lits.tovisitapp.exception.place.PlaceNotFoundException;
import com.lits.tovisitapp.googleplaces.parser.GooglePlacesResponseParser;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Type;
import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.repository.TypeRepository;
import com.lits.tovisitapp.service.PlaceService;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchStatus;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import com.lits.tovisitapp.googleplaces.type.SearchableType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@PropertySource("googlePlaces.properties")
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

	@PostConstruct
	private void init() {
		uriFindPlacesNearby = uriFindPlacesNearby.replace("%apiKey%", apiKey);
		uriFindPlacesByText = uriFindPlacesByText.replace("%apiKey%", apiKey);
		uriFindPlaceById = uriFindPlaceById.replace("%apiKey%", apiKey);
	}

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
		String params = "location=" + circle.getLatitude() + "," + circle.getLongitude() + "&radius=" + circle.getRadius();
		if (type != null) {
			params += "&type=" + type.name().toLowerCase();
		}
		String URI = uriFindPlacesNearby.replace("%params%", params);

		return searchPlaces(URI, obtainParentLocation, false);
	}

	//<editor-fold desc="Exposed methods">
	@Override
	public PlacesSearchResponse findByText(
			String query,
			PlacesSearchCircle circle,
			SearchableType type,
			boolean obtainParentLocation) {
		if (query == null || query.isBlank()) {
			throw new PlaceBadRequestException("Search query cannot be null");
		}
		String params = "query=" + query;
		if (circle != null) {
			params += "&location=" + circle.getLatitude() + "," + circle.getLongitude() + "&radius=" + circle.getRadius();
		}
		if (type != null) {
			params += "&type=" + type.name().toLowerCase();
		}
		String URI = uriFindPlacesByText.replace("%params%", params);

		return searchPlaces(URI, obtainParentLocation, false);
	}

	@Override
	public PlacesSearchResponse findNextPage(String pageToken, boolean obtainParentLocation) {
		if (pageToken == null || pageToken.isBlank()) {
			throw new PlaceBadRequestException("Next page token cannot be null");
		}
		String URI = uriFindPlacesByText.replace("%params%", "pagetoken=" + pageToken);
		return searchPlaces(URI, obtainParentLocation, true);
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
		if (placeId <= 0) {
			throw new PlaceBadRequestException("Place id must be positive");
		}
		placeRepository.deleteById(placeId);
	}
	//</editor-fold>

	//<editor-fold desc="inner methods">
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
		String URI = uriFindPlaceById.replace("%placeId%", googlePlaceId);

		String placeDetailsResponse = executeRequestWithRetriesOnError(URI);
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
