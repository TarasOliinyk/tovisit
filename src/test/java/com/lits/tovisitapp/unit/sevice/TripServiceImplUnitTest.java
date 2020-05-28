package com.lits.tovisitapp.unit.sevice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lits.tovisitapp.dto.FullTripDTO;
import com.lits.tovisitapp.dto.ShortTripDTO;
import com.lits.tovisitapp.dto.TripDTO;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.repository.TripRepository;
import com.lits.tovisitapp.service.PlaceService;
import com.lits.tovisitapp.service.TripService;
import com.lits.tovisitapp.service.impl.TripServiceImpl;
import com.lits.tovisitapp.utils.ParseDataUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TripServiceImplUnitTest {

    private TripService tripService;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private PlaceService placeService;
    @InjectMocks
    private ModelMapper modelMapper;

    @Before
    public void init() {
        tripService = new TripServiceImpl(modelMapper, tripRepository, placeService);
    }

    @Test
    public void create_trip_tripDto() throws IOException {
        //Arrange
        TripDTO tripDto = ParseDataUtils
                .prepareData("unit/service/trip/create/positive_data.json", new TypeReference<>() {
                });
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/create/positive_data_for_Trip.json", new TypeReference<>() {
                });
        TripDTO expected = ParseDataUtils
                .prepareData("unit/service/trip/create/result.json", new TypeReference<>() {
                });
        when(tripRepository.save(trip)).thenReturn(trip);

        //Act
        TripDTO actual = tripService.create(tripDto);

        //Assert
        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void update_trip_tripDto() throws IOException {
        //Arrange
        TripDTO tripDto = ParseDataUtils
                .prepareData("unit/service/trip/update/positive_data.json", new TypeReference<>() {
                });
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/update/positive_data_for_Trip.json", new TypeReference<>() {
                });
        TripDTO expected = ParseDataUtils
                .prepareData("unit/service/trip/update/result.json", new TypeReference<>() {
                });
        when(tripRepository.save(trip)).thenReturn(trip);

        //Act
        TripDTO actual = tripService.update(tripDto);

        //Assert
        Assert.assertTrue(expected.equals(actual));
    }

    @Test
    public void delete_tripId_void() {
        //Arrange
        doNothing().when(tripRepository).deleteById(eq(1L));

        //Act
        tripService.delete(1L);

        //Assert
        verify(tripRepository, times(1)).deleteById(eq(1L));
    }

    @Test
    public void getOne_tripId_fullTripDto() throws IOException {
        //Arrange
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/getOne/positive_data_for_Trip.json", new TypeReference<>() {
                });
        FullTripDTO expected = ParseDataUtils
                .prepareData("unit/service/trip/getOne/result.json", new TypeReference<>() {
                });
        when(tripRepository.findById(eq(5L))).thenReturn(Optional.of(trip));

        //Act
        FullTripDTO actual = tripService.getOne(5L);

        //Assert
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        Assert.assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        Assert.assertEquals(expected.getName(), actual.getName());

        assertPlaces(expected.getPlaces(), actual.getPlaces());
    }

    @Test
    public void getAll_void_shortTripDtos() throws IOException {
        //Arrange
        List<Trip> trips = ParseDataUtils
                .prepareData("unit/service/trip/getAll/positive_data_for_Trip.json", new TypeReference<>() {
                });
        List<ShortTripDTO> expected = ParseDataUtils
                .prepareData("unit/service/trip/getAll/result.json", new TypeReference<>() {
                });
        when(tripRepository.findAll()).thenReturn(trips);

        //Act
        List<ShortTripDTO> actual = tripService.getAll();

        //Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllByAccountId_accountId_shortTripDtos() throws IOException {
        //Arrange
        List<Trip> trips = ParseDataUtils
                .prepareData("unit/service/trip/getAllByAccountId/positive_data_for_Trip.json", new TypeReference<>() {
                });
        List<ShortTripDTO> expected = ParseDataUtils
                .prepareData("unit/service/trip/getAllByAccountId/result.json", new TypeReference<>() {
                });
        when(tripRepository.findAllByUserId(eq(1L))).thenReturn(trips);

        //Act
        List<ShortTripDTO> actual = tripService.getAllByUserId(1L);

        //Assert
        Assert.assertEquals(expected, actual);
    }

//    @Test
//    public void addPlacesToTrip_tripIdPlaces_fullTripDto() throws IOException {
//        //Arrange
//        List<PlaceForTripDTO> newPlaces = ParseDataUtils
//                .prepareData("unit/service/trip/addPlacesToTrip/positive_data.json", new TypeReference<>() {
//                });
//        Trip trip = ParseDataUtils
//                .prepareData("unit/service/trip/addPlacesToTrip/positive_data_for_Trip.json", new TypeReference<>() {
//                });
//        FullTripDTO expected = ParseDataUtils
//                .prepareData("unit/service/trip/addPlacesToTrip/result.json", new TypeReference<>() {
//                });
//        when(tripRepository.findById(eq(1L))).thenReturn(Optional.of(trip));
//        when(tripRepository.save(trip)).thenReturn(trip);
//
//        //Act
//        FullTripDTO actual = tripService.addPlacesToTrip(1L, newPlaces);
//
//        //Assert
//        Assert.assertEquals(expected.getId(), actual.getId());
//        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
//        Assert.assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
//        Assert.assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
//        Assert.assertEquals(expected.getName(), actual.getName());
//
//        assertPlaces(expected.getPlaces(), actual.getPlaces());
//    }

    @Test
    public void deletePlaceFromTrip_tripIdPlaceId_fullTripDto() throws IOException {
        //Arrange
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/deletePlaceFromTrip/positive_data_for_Trip.json", new TypeReference<>() {
                });
        FullTripDTO expected = ParseDataUtils
                .prepareData("unit/service/trip/deletePlaceFromTrip/result.json", new TypeReference<>() {
                });
        when(tripRepository.findById(eq(1L))).thenReturn(Optional.of(trip));
        when(tripRepository.save(trip)).thenReturn(trip);

        //Act
        FullTripDTO actual = tripService.deletePlaceFromTrip(1L, 2L);

        //Assert
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        Assert.assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        Assert.assertEquals(expected.getName(), actual.getName());

        assertPlaces(expected.getPlaces(), actual.getPlaces());


    }

    private void assertPlaces(List<Place> expected, List<Place> actual) {
        Assert.assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i).getId(), actual.get(i).getId());
            Assert.assertEquals(expected.get(i).getGooglePlaceId(), actual.get(i).getGooglePlaceId());
            Assert.assertEquals(expected.get(i).getParentLocation(), actual.get(i).getParentLocation());
            Assert.assertEquals(expected.get(i).getName(), actual.get(i).getName());
            Assert.assertEquals(expected.get(i).getFormattedAddress(), actual.get(i).getFormattedAddress());

            Assert.assertEquals(expected.get(i).getLocationLat(), actual.get(i).getLocationLat());
            Assert.assertEquals(expected.get(i).getLocationLng(), actual.get(i).getLocationLng());
            Assert.assertEquals(expected.get(i).getPriceLevel(), actual.get(i).getPriceLevel());
            Assert.assertEquals(expected.get(i).getRating(), actual.get(i).getRating());
            Assert.assertEquals(expected.get(i).getTypes(), actual.get(i).getTypes());
        }
    }

}
