package com.lits.tovisitapp.service.impl;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.exceptions.place.PlaceBadRequestException;
import com.lits.tovisitapp.exceptions.place.PlaceNotFoundException;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lits.tovisitapp.googleplaces.type.PlacesSearchStatus.*;

@Service
@PropertySource("classpath:googlePlaces.properties")
@Log
public class PlaceServiceImpl implements PlaceService {

	private PlaceRepository placeRepository;
	private TypeRepository typeRepository;
	private ModelMapper mapper;
	private HttpClient httpClient;
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
			HttpClient httpClient,
			GooglePlacesResponseParser responseParser) {
		this.mapper = mapper;
		this.placeRepository = placeRepository;
		this.typeRepository = typeRepository;
		this.httpClient = httpClient;
		this.responseParser = responseParser;
	}

	//<editor-fold desc="Exposed methods">
	@Override
	public PlacesSearchResponse findNearby(
			PlacesSearchCircle circle,
			SearchableType type,
			String pageToken,
			boolean obtainParentLocation) {
		if ((circle == null && !StringUtils.hasText(pageToken)) || (circle != null && StringUtils.hasText(pageToken))) {
			throw new PlaceBadRequestException("Search circle or page token required, but not both");
		}
		// construct URI
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uriFindPlacesNearby);
		if (circle != null) {
			uriBuilder = uriBuilder
					.queryParam("location", circle.getLatitude() + "," + circle.getLongitude())
					.queryParam("radius", circle.getRadius());
			if (type != null) {
				uriBuilder = uriBuilder.queryParam("type", type.name().toLowerCase());
			}
			log.info("find place nearby: circle: " + circle.toString()
					+ (type != null ? " and type: '" + type.name().toLowerCase() + "'" : ""));
		} else {
			if (type != null) {
				throw new PlaceBadRequestException("Type is forbidden when pageToken passed");
			}
			uriBuilder = uriBuilder.queryParam("pagetoken", pageToken);
			log.info("find place nearby: page token: " + pageToken);
		}
		uriBuilder = uriBuilder.queryParam("key", apiKey);

		return searchPlaces(uriBuilder.build(true).toUri(), obtainParentLocation, pageToken != null);
	}

	@Override
	public PlacesSearchResponse findByText(
			String query,
			SearchableType type,
			String pageToken,
			boolean obtainParentLocation) {
		if ((!StringUtils.hasText(query) && !StringUtils.hasText(pageToken))
				|| (StringUtils.hasText(query) && StringUtils.hasText(pageToken))) {
			throw new PlaceBadRequestException("Search query or page token required, but not both");
		}
		// construct URI
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uriFindPlacesByText);
		if (StringUtils.hasText(query)) {
			uriBuilder = uriBuilder.queryParam("query", query);
			if (type != null) {
				uriBuilder = uriBuilder.queryParam("type", type.name().toLowerCase());
			}
			log.info("find place by text: query: '" + query + "'"
					+ (type != null ? " and type: '" + type.name().toLowerCase() + "'" : ""));
		} else {
			if (type != null) {
				throw new PlaceBadRequestException("Type is forbidden when pageToken passed");
			}
			uriBuilder = uriBuilder.queryParam("pagetoken", pageToken);
			log.info("find place by text: page token: " + pageToken);
		}
		uriBuilder = uriBuilder.queryParam("key", apiKey);

		return searchPlaces(uriBuilder.build().toUri(), obtainParentLocation, pageToken != null);
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
							.findFirst().orElse(st))
					.collect(Collectors.toSet());
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
		if (!placeRepository.existsById(placeId)) {
			throw new PlaceNotFoundException("Place with id " + placeId + " not exists");
		}
		placeRepository.deleteById(placeId);
	}
	//</editor-fold>

	//<editor-fold desc="Inner methods">
	private PlacesSearchResponse searchPlaces(URI uri, boolean obtainParentLocation, boolean isNextPageRequest) {
		PlacesSearchResponse response;
		int tries = nextPageSearchTries;
		do {
			--tries;
			String responseJson = executeRequestWithRetriesOnError(uri);
			PlacesSearchStatus status = responseParser.extractStatus(responseJson);
			response = PlacesSearchResponse.builder()
					.nextPageToken(responseParser.extractNextPageToken(responseJson))
					.places(responseParser.extractPlaces(responseJson))
					.build();
			if (status == INVALID_REQUEST) {
				if (isNextPageRequest && tries > 0) {
					try {
						Thread.sleep(nextPageSearchSleepMs);
						continue;
					} catch (InterruptedException ie) {
						throw new GooglePlacesApiException("Interrupted while retrieving places");
					}
				} else {
					throw new GooglePlacesApiException("Error while retrieving places: " + status.description);
				}
			} else if (status != NOT_FOUND && status != ZERO_RESULTS && status != OK) {
				throw new GooglePlacesApiException("Error while retrieving places: " + status.description);
			}
			break;
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
		String placeDetailsResponse = executeRequestWithRetriesOnError(uriBuilder.build().toUri());
		PlacesSearchStatus status = responseParser.extractStatus(placeDetailsResponse);

		if (status == NOT_FOUND || status == ZERO_RESULTS) {
			throw new PlaceNotFoundException("Place id: " + googlePlaceId + " not found");
		} else if (status != OK) {
			throw new GooglePlacesApiException("Error while retrieving place details: " + status.description);
		}

		return responseParser.extractParentLocation(placeDetailsResponse);
	}

	private String executeRequestWithRetriesOnError(URI uri) {
		int tries = retriesOnErrors;
		do {
			--tries;
			HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
			HttpResponse<String> response;
			try {
				response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
				if (response.statusCode() >= 500) {
					throw new IOException(response.statusCode() + " while calling GooglePlaces");
				}
				if (response.statusCode() >= 400) {
					throw new GooglePlacesApiException(response.statusCode() + " while calling GooglePlaces");
				}
				return response.body();
			} catch (IOException e) {
				if (tries == 0) {
					throw new GooglePlacesApiException("Tries exhausted while calling GooglePlaces", e);
				}
				log.info("retrying request: " + uri.toString());
			} catch (InterruptedException e) {
				throw new RuntimeException("Interrupted while waiting for response from google", e);
			}
		} while (true);
	}
	//</editor-fold>
}
