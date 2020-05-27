package com.lits.tovisitapp.service;

import com.lits.tovisitapp.ToVisitApplication;
import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.exception.place.PlaceBadRequestException;
import com.lits.tovisitapp.googleplaces.exception.GooglePlacesApiException;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.repository.TypeRepository;
import com.lits.tovisitapp.util.PlacesTestUtil;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToVisitApplication.class)
// this disables database to speed up context building
@EnableAutoConfiguration(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})
public class PlacesServiceImpUnitTest {
	@MockBean
	private PlaceRepository placeRepository;
	@MockBean
	private TypeRepository typeRepository;
	@MockBean
	private HttpClient httpClient;

	@Autowired
	private PlaceService placeService;

	private PlacesTestUtil util = new PlacesTestUtil("googlePlaces.properties");
	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Before
	public void init() {
		ModelMapper mapper = new ModelMapper();
		mapper.addMappings(new PropertyMap<Place, PlaceDTO>() {
			protected void configure() {
				map().setTripId(source.getTripId());
			}
		});
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
	@SneakyThrows
	public void findByText_OkQuery_ServiceUnavailable() {
		// prepare
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(util.getProperty("uri.findPlacesByText"))
				.queryParam("query", "coffee in lviv")
				.queryParam("key", util.getProperty("apiKey"))
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		when(httpClient.send(eq(request), any())).thenThrow(IOException.class);

		Assertions.assertThatThrownBy(
				() -> placeService.findByText("coffee in lviv", null, null, false))
				.isInstanceOf(GooglePlacesApiException.class);
	}

	@Test
	@SneakyThrows
	public void findByText_EmptyResultQuery_NoObtainParent_NoCircle_NoType_EmptyResults() {
		// prepare
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(util.getProperty("uri.findPlacesByText"))
				.queryParam("query", "kawabunga")
				.queryParam("key", util.getProperty("apiKey"))
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		String responseBody = util.textFileToString("places/googleResponses/byText/emptyResults.json");
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri, responseBody);
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

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
	@SneakyThrows
	public void findByText_OkQuery_NoObtainParent_NoCircle_NoType_Success() {
		// prepare
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(util.getProperty("uri.findPlacesByText"))
				.queryParam("query", "coffee in lviv")
				.queryParam("key", util.getProperty("apiKey"))
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		String responseBody = util.textFileToString("places/googleResponses/byText/coffeInLviv_NoCircle_NoType_Page1.json");
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri, responseBody);
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

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
	@SneakyThrows
	public void findByText_OkQuery_ObtainParent_NoCircle_NoType_Success() {
		// prepare
		// mock initial request
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(util.getProperty("uri.findPlacesByText"))
				.queryParam("query", "coffee in lviv")
				.queryParam("key", util.getProperty("apiKey"))
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		String responseBody = util.textFileToString("places/googleResponses/byText/coffeInLviv_NoCircle_NoType_Page1.json");
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri, responseBody);
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		// mock places details requests
		for (var entry : util.parentCallResponses().entrySet()) {
			URI pdMockedUri = UriComponentsBuilder
					.fromHttpUrl(util.getProperty("uri.findPlaceById"))
					.queryParam("placeid", entry.getKey())
					.queryParam("fields", "address_component")
					.queryParam("key", util.getProperty("apiKey"))
					.build().toUri();
			HttpRequest pdRequest = HttpRequest.newBuilder(pdMockedUri).GET().build();
			HttpResponse<Object> pdResponse = util.buildResponse(pdRequest, pdMockedUri, entry.getValue());
			when(httpClient.send(eq(pdRequest), any())).thenReturn(pdResponse);
		}

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
		[V] find by text: +q google returned error

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
		[ ] find nearby: +circle google returned error

	positive:
		[ ] find nearby: +circle -type -parent
		[ ] find nearby: +circle +type -parent
		[ ] find nearby: +circle -type +parent

	find next page:
	---------------
	negative:
		[ ] find next page: +token null
		[ ] find next page: +token empty
		[ ] find next page: +token google returned error

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
		[ ] place not found
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
