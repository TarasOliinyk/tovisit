package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.exception.place.PlaceBadRequestException;
import com.lits.tovisitapp.googleplaces.exception.GooglePlacesApiException;
import com.lits.tovisitapp.googleplaces.parser.impl.GooglePlacesResponseJsonParser;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.repository.TypeRepository;
import com.lits.tovisitapp.service.impl.PlaceServiceImpl;
import com.lits.tovisitapp.util.PlacesTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlacesServiceImpUnitTest {

	private PlacesTestUtil util = new PlacesTestUtil("googlePlaces.properties");
	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Mock
	private PlaceRepository placeRepository;
	@Mock
	private TypeRepository typeRepository;
	@Mock
	private RestTemplate httpClient;

	private PlaceService placeService;

	@Before
	public void init() {
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(new PropertyMap<Place, PlaceDTO>() {
			protected void configure() {
				map().setTripId(source.getTripId());
			}
		});

		placeService = new PlaceServiceImpl(
				mapper, placeRepository, typeRepository, httpClient, new GooglePlacesResponseJsonParser());

		// Inject @Values
		ReflectionTestUtils.setField(placeService, "uriFindPlacesNearby",
				util.getProperty("uri.findPlacesNearby"));
		ReflectionTestUtils.setField(placeService, "uriFindPlacesByText",
				util.getProperty("uri.findPlacesByText"));
		ReflectionTestUtils.setField(placeService, "uriFindPlaceById",
				util.getProperty("uri.findPlaceById"));
		ReflectionTestUtils.setField(placeService, "apiKey", util.getProperty("apiKey"));
		ReflectionTestUtils.setField(placeService, "retriesOnErrors",
				Integer.parseInt(util.getProperty("retriesOnErrors")));
		ReflectionTestUtils.setField(placeService, "nextPageSearchTries",
				Integer.parseInt(util.getProperty("nextPageSearch.tries")));
		ReflectionTestUtils.setField(placeService, "nextPageSearchSleepMs",
				Integer.parseInt(util.getProperty("nextPageSearch.sleepMs")));
	}

	// findByText - negative cases

	@Test
	public void findByText_NullQuery_BadRequest() {
		Assertions.assertThatThrownBy(
				() -> placeService.findByText(null, null, null, false))
				.isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void findByText_BlankQuery_BadRequest() {
		Assertions.assertThatThrownBy(
				() -> placeService.findByText(" ", null, null, false))
				.isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void findByText_OkQuery_ServiceUnavailable() {
		// prepare
		String mockedUri = UriComponentsBuilder
				.fromHttpUrl(util.getProperty("uri.findPlacesByText"))
				.queryParam("query", "coffee in lviv")
				.queryParam("key", util.getProperty("apiKey"))
				.build().toString();
		when(httpClient.exchange(eq(mockedUri), eq(HttpMethod.GET), eq(null), eq(String.class)))
				.thenThrow(HttpClientErrorException.class);

		Assertions.assertThatThrownBy(
				() -> placeService.findByText("coffee in lviv", null, null, false))
				.isInstanceOf(GooglePlacesApiException.class);
	}

	@Test
	public void findByText_EmptyResultQuery_NoObtainParent_NoCircle_NoType_EmptyResults() {
		// prepare
		String mockedUri = UriComponentsBuilder
				.fromHttpUrl(util.getProperty("uri.findPlacesByText"))
				.queryParam("query", "kawabunga")
				.queryParam("key", util.getProperty("apiKey"))
				.build().toString();
		ResponseEntity<String> mockedResponse = new ResponseEntity<>(
				util.textFileToString("places/googleResponses/byText/emptyResults.json"),
				new HttpHeaders(), HttpStatus.OK);
		when(httpClient.exchange(eq(mockedUri), eq(HttpMethod.GET), eq(null), eq(String.class)))
				.thenReturn(mockedResponse);

		// execute
		PlacesSearchResponse response =
				placeService.findByText("kawabunga", null, null, false);

		// assert
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNull();
		Assertions.assertThat(response.getPlaces()).isEmpty();
	}

	// findByText - positive cases

	@Test
	public void findByText_OkQuery_NoObtainParent_NoCircle_NoType_Success() {
		// prepare
		String mockedUri = UriComponentsBuilder
				.fromHttpUrl(util.getProperty("uri.findPlacesByText"))
				.queryParam("query", "coffee in lviv")
				.queryParam("key", util.getProperty("apiKey"))
				.build().toString();
		ResponseEntity<String> mockedResponse = new ResponseEntity<>(
				util.textFileToString("places/googleResponses/byText/coffeInLviv_NoCircle_NoType_Page1.json"),
				new HttpHeaders(), HttpStatus.OK);
		when(httpClient.exchange(eq(mockedUri), eq(HttpMethod.GET), eq(null), eq(String.class)))
				.thenReturn(mockedResponse);

		// execute
		PlacesSearchResponse response =
				placeService.findByText("coffee in lviv", null, null, false);

		// assert
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> {
			Assertions.assertThat(validator.validate(place)).isEmpty();
			Assertions.assertThat(place.getParentLocation()).isNull();
		});
	}

	@Test
	public void findByText_OkQuery_ObtainParent_NoCircle_NoType_Success() {
		// prepare
		// mock initial request
		String mockedUri = UriComponentsBuilder.fromHttpUrl(util.getProperty("uri.findPlacesByText"))
				.queryParam("query", "coffee in lviv")
				.queryParam("key", util.getProperty("apiKey"))
				.build().toString();
		ResponseEntity<String> mockedResponse = new ResponseEntity<>(
				util.textFileToString("places/googleResponses/byText/coffeInLviv_NoCircle_NoType_Page1.json"),
				new HttpHeaders(), HttpStatus.OK);
		when(httpClient.exchange(eq(mockedUri), eq(HttpMethod.GET), eq(null), eq(String.class)))
				.thenReturn(mockedResponse);
		// mock places details requests
		util.parentCallResponses().forEach((googleId, responseJson) -> {
			String pCallMockedUri = UriComponentsBuilder
					.fromHttpUrl(util.getProperty("uri.findPlaceById"))
					.queryParam("placeid", googleId)
					.queryParam("fields", "address_component")
					.queryParam("key", util.getProperty("apiKey"))
					.build().toString();
			ResponseEntity<String> pCallMockedResponse =
					new ResponseEntity<>(responseJson, new HttpHeaders(), HttpStatus.OK);
			when(httpClient.exchange(eq(pCallMockedUri), eq(HttpMethod.GET), eq(null), eq(String.class)))
					.thenReturn(pCallMockedResponse);
		});

		// execute
		PlacesSearchResponse response =
				placeService.findByText("coffee in lviv", null, null, true);

		// assert
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> {
			Assertions.assertThat(validator.validate(place)).isEmpty();
			Assertions.assertThat(place.getParentLocation()).isNotEmpty();
		});
	}

	/*
	find by text:
	-------------
	negative:
		[V] find by text: +q null
		[V] find by text: +q empty
		[V] find by text: +q empty results
		[V] find by text: +q google returned 500

	positive:
		[V] find by text: +q -circle -type -parent
		[V] find by text: +q -circle -type +parent
		[ ] find by text: +q -circle +type -parent
		[ ] find by text: +q +circle -type -parent

	find nearby:
	------------
	negative:
		[ ] find nearby: +circle null
		[ ] find nearby: +circle empty result
		[ ] find nearby: +circle google returned 500

	positive:
		[ ] find nearby: +circle -type -parent
		[ ] find nearby: +circle +type -parent
		[ ] find nearby: +circle -type +parent

	find next page:
	---------------
	negative:
		[ ] find next page: +token null
		[ ] find next page: +token empty
		[ ] find next page: +token google returned 500

	positive:
		[ ] find next page: +token -parent
		[ ] find next page: +token +parent

	save place:
	-----------
	negative:
		[ ] null place
	positive:
		[ ] save(create) place
		[ ] save(update) place

	get place by id:
	----------------
	negative:
		[ ] bad id
	positive:
		[ ] place found

	delete place:
	-------------
	negative:
		[ ] bad id
		[ ] place not found
	positive:
		[ ] deleted
	*/
}
