package com.lits.tovisitapp.unit.sevice;

import com.lits.tovisitapp.ToVisitApplication;
import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.exceptions.place.PlaceBadRequestException;
import com.lits.tovisitapp.exceptions.place.PlaceNotFoundException;
import com.lits.tovisitapp.googleplaces.exception.GooglePlacesApiException;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.googleplaces.type.SearchableType;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Type;
import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.repository.TripRepository;
import com.lits.tovisitapp.repository.TypeRepository;
import com.lits.tovisitapp.repository.UserRepository;
import com.lits.tovisitapp.service.PlaceService;
import com.lits.tovisitapp.service.UserService;
import com.lits.tovisitapp.utils.PlaceTestUtil;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ToVisitApplication.class)
@EnableAutoConfiguration(exclude = {
		// disable database on unit test to speed up context building
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		})
@PropertySource("classpath:googlePlaces.properties")
public class PlaceServiceImpUnitTest {
	@MockBean
	private PlaceRepository placeRepository;
	@MockBean
	private TypeRepository typeRepository;
	@MockBean
	private HttpClient httpClient;

	@MockBean
	private UserService userService;
	@MockBean
	private UserRepository userRepository;
	@MockBean
	private TripRepository tripRepository;

	@Autowired
	private ModelMapper mapper;
	@Autowired
	private PlaceService placeService;

	@Value("${uri.findPlacesNearby}")
	private String uriFindPlacesNearby;
	@Value("${uri.findPlacesByText}")
	private String uriFindPlacesByText;
	@Value("${uri.findPlaceById}")
	private String uriFindPlaceById;
	@Value("${apiKey}")
	private String apiKey;

	// small helpers to make this class look better
	private PlaceTestUtil util = new PlaceTestUtil();

	// to test whether DTO's are valid
	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	//<editor-fold desc="FindByText - Negative cases">
	@Test
	@SneakyThrows
	public void findByText_OkQuery_NoType_NoPageToken_NoParentSearch_ServiceUnavailable() {
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesByText)
				.queryParam("query", "coffee in lviv")
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		when(httpClient.send(eq(request), any())).thenThrow(IOException.class);

		Assertions.assertThatThrownBy(
				() -> placeService.findByText("coffee in lviv", null, null,false))
				.isInstanceOf(GooglePlacesApiException.class);
	}

	@Test
	public void findByText_NoQuery_NoType_NoPageToken_NoParentSearch_BadRequest() {
		Assertions.assertThatThrownBy(
				() -> placeService.findByText(null,  null, null,false))
				.isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void findByText_HasQuery_NoType_HasPageToken_NoParentSearch_BadRequest() {
		Assertions.assertThatThrownBy(
				() -> placeService.findByText("coffee in lviv",  null, "token",false))
				.isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void findByText_NoQuery_HasType_HasPageToken_NoParentSearch_BadRequest() {
		Assertions.assertThatThrownBy(
				() -> placeService.findByText(null,  SearchableType.CAFE, "token",false))
				.isInstanceOf(PlaceBadRequestException.class);
	}
	//</editor-fold>

	//<editor-fold desc="FindByText - Positive cases">
	@Test
	@SneakyThrows
	public void findByText_HasQuery_NoType_NoPageToken_NoParentSearch_EmptyResults() {
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesByText)
				.queryParam("query", "kawabunga")
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		String responseBody = util.textFileToString("unit/service/place/googleResponses/byText/emptyResults.json");
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri, responseBody);
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		PlacesSearchResponse response =
				placeService.findByText("kawabunga", null, null,false);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNull();
		Assertions.assertThat(response.getPlaces()).isEmpty();
	}

	@Test
	@SneakyThrows
	public void findByText_HasQuery_NoType_NoPageToken_NoParentSearch_Success() {
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesByText)
				.queryParam("query", "coffee in lviv")
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		String responseBody = util.textFileToString("unit/service/place/googleResponses/byText/coffeInLviv_NoType_Page1.json");
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri, responseBody);
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		PlacesSearchResponse response =
				placeService.findByText("coffee in lviv", null, null,false);

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
	public void findByText_HasQuery_HasType_NoPageToken_NoParentSearch_Success() {
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesByText)
				.queryParam("query", "coffee in lviv")
				.queryParam("type", "cafe")
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		String responseBody = util.textFileToString("unit/service/place/googleResponses/byText/coffeInLviv_CafeType_Page1.json");
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri, responseBody);
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		PlacesSearchResponse response =
				placeService.findByText("coffee in lviv", SearchableType.CAFE, null,false);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> {
			Assertions.assertThat(validator.validate(place)).isEmpty();
			Assertions.assertThat(place.getParentLocation()).isNull();
			Assertions.assertThat(place.getTypes()).contains("cafe");
		});
	}

	@Test
	@SneakyThrows
	public void findByText_HasQuery_HasType_NoPageToken_ParentSearch_Success() {
		// mock initial request
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesByText)
				.queryParam("query", "coffee in lviv")
				.queryParam("type", "cafe")
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		String responseBody = util.textFileToString("unit/service/place/googleResponses/byText/coffeInLviv_CafeType_Page1.json");
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri, responseBody);
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		// mock places details requests
		for (var entry : util.detailsCoffeeInLvivNoType().entrySet()) {
			URI pdMockedUri = UriComponentsBuilder
					.fromHttpUrl(uriFindPlaceById)
					.queryParam("placeid", entry.getKey())
					.queryParam("fields", "address_component")
					.queryParam("key", apiKey)
					.build().toUri();
			HttpRequest pdRequest = HttpRequest.newBuilder(pdMockedUri).GET().build();
			HttpResponse<Object> pdResponse = util.buildResponse(pdRequest, pdMockedUri, entry.getValue());
			when(httpClient.send(eq(pdRequest), any())).thenReturn(pdResponse);
		}

		PlacesSearchResponse response =
				placeService.findByText("coffee in lviv", SearchableType.CAFE, null,true);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> {
			Assertions.assertThat(validator.validate(place)).isEmpty();
			Assertions.assertThat(place.getParentLocation()).isNotEmpty();
			Assertions.assertThat(place.getTypes()).contains("cafe");
		});
	}

	@Test
	@SneakyThrows
	public void findByText_NoQuery_NoType_HasPageToken_NoParentSearch_Success() {
		// mock initial request
		String pageToken = "token";
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesByText)
				.queryParam("pagetoken", pageToken)
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		String responseBody = util.textFileToString("unit/service/place/googleResponses/byText/coffeInLviv_NoType_Page2.json");
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri, responseBody);
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		PlacesSearchResponse response =
				placeService.findByText(null, null, pageToken,false);

		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> {
			Assertions.assertThat(validator.validate(place)).isEmpty();
			Assertions.assertThat(place.getParentLocation()).isNull();
		});
	}
	//</editor-fold>

	//<editor-fold desc="FindNearby - Negative cases">
	@Test
	@SneakyThrows
	public void findNearby_HasCircle_NoType_NoPageToken_NoParentSearch_ServiceUnavailable() {
		PlacesSearchCircle circle = PlacesSearchCircle
				.builder().latitude(49.839683).longitude(24.029717).radius(500).build();
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesNearby)
				.queryParam("location", circle.getLatitude() + "," + circle.getLongitude())
				.queryParam("radius", circle.getRadius())
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		when(httpClient.send(eq(request), any())).thenThrow(IOException.class);

		Assertions.assertThatThrownBy(
				() -> placeService.findNearby(circle, null, null,false))
				.isInstanceOf(GooglePlacesApiException.class);
	}

	@Test
	public void findNearby_NoCircle_NoType_NoPageToken_NoParentSearch_BadRequest() {
		Assertions.assertThatThrownBy(
				() -> placeService.findNearby(null,  null, null,false))
				.isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void findNearby_HasCircle_NoType_HasPageToken_NoParentSearch_BadRequest() {
		PlacesSearchCircle circle = PlacesSearchCircle
				.builder().latitude(49.839683).longitude(24.029717).radius(1000).build();

		Assertions.assertThatThrownBy(
				() -> placeService.findNearby(circle,  null, "token",false))
				.isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void findNearby_NoCircle_HasType_HasPageToken_NoParentSearch_BadRequest() {
		Assertions.assertThatThrownBy(
				() -> placeService.findNearby(null,  SearchableType.CAFE, "token",false))
				.isInstanceOf(PlaceBadRequestException.class);
	}
	//</editor-fold>

	//<editor-fold desc="FindNearby - Positive cases">
	@Test
	@SneakyThrows
	public void findNearby_Circle_NoType_NoPageToken_NoParentSearch_Success() {
		// prepare
		PlacesSearchCircle circle = PlacesSearchCircle
				.builder().latitude(49.839683).longitude(24.029717).radius(500).build();
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesNearby)
				.queryParam("location", circle.getLatitude() + "," + circle.getLongitude())
				.queryParam("radius", circle.getRadius())
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri,
				util.textFileToString("unit/service/place/googleResponses/nearby/LvivCenter500m_NoTypes_Page1.json"));
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		// execute
		PlacesSearchResponse response =
				placeService.findNearby(circle, null, null,false);

		// assert
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> {
			Assertions.assertThat(validator.validate(place)).isEmpty();
			Assertions.assertThat(place.getParentLocation()).isNull();
			double distance = util.metersBetweenCoordinates(circle.getLatitude(), circle.getLongitude(), place.getLocationLat(), place.getLocationLng());
			Assertions.assertThat(distance).isLessThanOrEqualTo(circle.getRadius());
		});
	}

	@Test
	@SneakyThrows
	public void findNearby_Circle_HasType_NoPageToken_NoParentSearch_Success() {
		// prepare
		PlacesSearchCircle circle = PlacesSearchCircle
				.builder().latitude(49.839683).longitude(24.029717).radius(500).build();
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesNearby)
				.queryParam("location", circle.getLatitude() + "," + circle.getLongitude())
				.queryParam("radius", circle.getRadius())
				.queryParam("type", SearchableType.CAFE.name().toLowerCase())
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri,
				util.textFileToString("unit/service/place/googleResponses/nearby/LvivCenter500m_CafeType_Page1.json"));
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		// execute
		PlacesSearchResponse response =
				placeService.findNearby(circle, SearchableType.CAFE, null,false);

		// assert
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> {
			Assertions.assertThat(validator.validate(place)).isEmpty();
			Assertions.assertThat(place.getParentLocation()).isNull();
			Assertions.assertThat(place.getTypes()).contains("cafe");
			double distance = util.metersBetweenCoordinates(circle.getLatitude(), circle.getLongitude(), place.getLocationLat(), place.getLocationLng());
			Assertions.assertThat(distance).isLessThanOrEqualTo(circle.getRadius());
		});
	}

	@Test
	@SneakyThrows
	public void findNearby_Circle_HasType_NoPageToken_ParentSearch_Success() {
		// prepare
		// initial request
		PlacesSearchCircle circle = PlacesSearchCircle
				.builder().latitude(49.839683).longitude(24.029717).radius(500).build();
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesNearby)
				.queryParam("location", circle.getLatitude() + "," + circle.getLongitude())
				.queryParam("radius", circle.getRadius())
				.queryParam("type", SearchableType.CAFE.name().toLowerCase())
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri,
				util.textFileToString("unit/service/place/googleResponses/nearby/LvivCenter500m_CafeType_Page1.json"));
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		// places details requests
		for (var entry : util.detailsLvivCenter500mNoTypes().entrySet()) {
			URI pdMockedUri = UriComponentsBuilder
					.fromHttpUrl(uriFindPlaceById)
					.queryParam("placeid", entry.getKey())
					.queryParam("fields", "address_component")
					.queryParam("key", apiKey)
					.build().toUri();
			System.out.println("test uri: " + pdMockedUri.toString());
			HttpRequest pdRequest = HttpRequest.newBuilder(pdMockedUri).GET().build();
			HttpResponse<Object> pdResponse = util.buildResponse(pdRequest, pdMockedUri, entry.getValue());
			when(httpClient.send(eq(pdRequest), any())).thenReturn(pdResponse);
		}

		// execute
		PlacesSearchResponse response =
				placeService.findNearby(circle, SearchableType.CAFE, null,true);

		// assert
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> {
			Assertions.assertThat(validator.validate(place)).isEmpty();
			Assertions.assertThat(place.getParentLocation()).isNotEmpty();
			Assertions.assertThat(place.getTypes()).contains("cafe");
			double distance = util.metersBetweenCoordinates(circle.getLatitude(), circle.getLongitude(), place.getLocationLat(), place.getLocationLng());
			Assertions.assertThat(distance).isLessThanOrEqualTo(circle.getRadius());
		});
	}

	@Test
	@SneakyThrows
	public void findNearby_NoCircle_NoType_HasPageToken_NoParentSearch_Success() {
		// prepare
		URI mockedUri = UriComponentsBuilder
				.fromHttpUrl(uriFindPlacesNearby)
				.queryParam("pagetoken", "token")
				.queryParam("key", apiKey)
				.build().toUri();
		HttpRequest request = HttpRequest.newBuilder(mockedUri).GET().build();
		HttpResponse<Object> mockedResponse = util.buildResponse(request, mockedUri,
				util.textFileToString("unit/service/place/googleResponses/nearby/LvivCenter500m_CafeType_Page2.json"));
		when(httpClient.send(eq(request), any())).thenReturn(mockedResponse);

		// execute
		PlacesSearchResponse response =
				placeService.findNearby(null, null, "token",false);

		// assert
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getNextPageToken()).isNotBlank();
		Assertions.assertThat(response.getPlaces()).hasSize(20);
		response.getPlaces().forEach(place -> Assertions.assertThat(validator.validate(place)).isEmpty());
	}
	//</editor-fold>

	//<editor-fold desc="Find place by id">
	@Test
	public void getPlaceById_BadId() {
		Assertions.assertThatThrownBy(
				() -> placeService.findPlaceById(-1)).isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void getPlaceById_NotFound() {
		when(placeRepository.findById(1L)).thenReturn(Optional.empty());

		Assertions.assertThatThrownBy(
				() -> placeService.findPlaceById(1)).isInstanceOf(PlaceNotFoundException.class);
	}

	@Test
	public void getPlaceById_Found() {
		Type cafeType = new Type();
			cafeType.setId(1L);
			cafeType.setName("CAFE");
		Place mockedPlace = new Place();
			mockedPlace.setId(1L);
			mockedPlace.setGooglePlaceId("ChIJ2QYZRGzdOkcR0rFWJPG9En4");
			mockedPlace.setParentLocation("L'viv");
			mockedPlace.setName("Masoch-cafe");
			mockedPlace.setFormattedAddress("Serbska St, 7, Lviv, Lviv Oblast, Ukraine, 79000");
			mockedPlace.setLocationLat(49.8409874);
			mockedPlace.setLocationLng(24.0331363);
			mockedPlace.setPriceLevel(2);
			mockedPlace.setRating(4.2);
			mockedPlace.setTripId(1L);
			mockedPlace.setTypes(Stream.of(cafeType).collect(Collectors.toSet()));
		when(placeRepository.findById(1L)).thenReturn(Optional.of(mockedPlace));

		PlaceDTO foundPlace = placeService.findPlaceById(1L);

		Assertions.assertThat(foundPlace).isNotNull();
	}
	//</editor-fold>

	//<editor-fold desc="Save place">
	@Test
	public void savePlace_NullPlace_BadRequest() {
		Assertions.assertThatThrownBy(
				() -> placeService.savePlace(null)).isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void savePlace_Success() {
		PlaceDTO submittedPlaceDTO = PlaceDTO.builder()
				.googlePlaceId("ChIJ2QYZRGzdOkcR0rFWJPG9En4")
				.parentLocation("L'viv")
				.name("Masoch-cafe")
				.formattedAddress("Serbska St, 7, Lviv, Lviv Oblast, Ukraine, 79000")
				.locationLat(49.8409874)
				.locationLng(24.0331363)
				.priceLevel(2)
				.rating(4.2)
				.tripId(1L)
				.types(Collections.singletonList("cafe"))
				.build();
		Place savedPlace = mapper.map(submittedPlaceDTO, Place.class);
		savedPlace.setId(1L);

		Type savedType = new Type();
		savedType.setId(1L);
		savedType.setName("cafe");
		when(typeRepository.findByNameIn(eq(Collections.singletonList("cafe"))))
				.thenReturn(Collections.singletonList(savedType));

		when(placeRepository
				.save(argThat((Place p) -> p.getGooglePlaceId().equals("ChIJ2QYZRGzdOkcR0rFWJPG9En4"))))
				.thenReturn(savedPlace);

		PlaceDTO savedPlaceDTO = placeService.savePlace(submittedPlaceDTO);

		Assertions.assertThat(savedPlaceDTO).isNotNull();
		Assertions.assertThat(savedPlaceDTO.getId()).isEqualTo(1);
	}
	//</editor-fold>

	//<editor-fold desc="Delete place">
	@Test
	public void deletePlace_BadId() {
		Assertions.assertThatThrownBy(
				() -> placeService.deletePlace(-1)).isInstanceOf(PlaceBadRequestException.class);
	}

	@Test
	public void deletePlace_NotFound() {
		when(placeRepository.findById(1L)).thenReturn(Optional.empty());
		Assertions.assertThatThrownBy(
				() -> placeService.deletePlace(1)).isInstanceOf(PlaceNotFoundException.class);
	}

	@Test
	public void deletePlace_Success() {
		when(placeRepository.existsById(eq(1L))).thenReturn(true);
		placeService.deletePlace(1);
		verify(placeRepository, times(1)).deleteById(1L);
	}
	//</editor-fold>
}
