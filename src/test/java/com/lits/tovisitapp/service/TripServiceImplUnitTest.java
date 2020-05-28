package com.lits.tovisitapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lits.tovisitapp.dto.TripDto;
import com.lits.tovisitapp.dto.TripPlaceDto;
import com.lits.tovisitapp.model.Account;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.repository.AccountRepository;
import com.lits.tovisitapp.repository.PlaceRepository;
import com.lits.tovisitapp.repository.TripRepository;
import com.lits.tovisitapp.service.impl.TripServiceImpl;
import com.lits.tovisitapp.utils.AssertTrips;
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
    private AccountRepository accountRepository;
    @Mock
    private PlaceRepository placeRepository;
    @InjectMocks
    private ModelMapper modelMapper;
    @InjectMocks
    private AssertTrips assertTrips;

    @Before
    public void init() {
        tripService = new TripServiceImpl(modelMapper, tripRepository, accountRepository, placeRepository);
    }

    @Test
    public void create_tripDto_tripDto() throws IOException {
        TripDto tripDto = ParseDataUtils
                .prepareData("unit/service/trip/create/positive_data.json", new TypeReference<>() {
                });
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/create/positive_data_for_Trip.json", new TypeReference<>() {
                });
        Account account = ParseDataUtils
                .prepareData("unit/service/trip/create/positive_data_for_Account.json", new TypeReference<>() {
                });
        TripDto expected = ParseDataUtils
                .prepareData("unit/service/trip/create/result.json", new TypeReference<>() {
                });
        when(tripRepository.save(trip)).thenReturn(trip);
        when(accountRepository.findById(tripDto.getAccountId())).thenReturn(Optional.of(account));

        TripDto actual = tripService.create(tripDto);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void update_tripDto_tripDto() throws IOException {
        TripDto tripDto = ParseDataUtils
                .prepareData("unit/service/trip/update/positive_data.json", new TypeReference<>() {
                });
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/update/positive_data_for_Trip.json", new TypeReference<>() {
                });
        Account account = ParseDataUtils
                .prepareData("unit/service/trip/update/positive_data_for_Account.json", new TypeReference<>() {
                });
        TripDto expected = ParseDataUtils
                .prepareData("unit/service/trip/update/result.json", new TypeReference<>() {
                });
        when(tripRepository.findById(tripDto.getId())).thenReturn(Optional.of(trip));
        when(tripRepository.save(trip)).thenReturn(trip);
        when(accountRepository.findById(tripDto.getAccountId())).thenReturn(Optional.of(account));

        TripDto actual = tripService.update(tripDto);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void delete_tripId_void() throws IOException {
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/delete/positive_data_for_Trip.json", new TypeReference<>() {
                });
        doNothing().when(tripRepository).deleteById(eq(5L));
        when(tripRepository.findById(eq(5L))).thenReturn(Optional.of(trip));

        tripService.delete(5L);

        verify(tripRepository, times(1)).deleteById(eq(5L));
    }

    @Test
    public void getOne_tripId_tripDto() throws IOException {
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/getOne/positive_data_for_Trip.json", new TypeReference<>() {
                });
        TripPlaceDto expected = ParseDataUtils
                .prepareData("unit/service/trip/getOne/result.json", new TypeReference<>() {
                });
        when(tripRepository.findById(eq(5L))).thenReturn(Optional.of(trip));

        TripPlaceDto actual = tripService.getOne(5L);

        assertTrips.assertTrips(expected, actual);
    }

    @Test
    public void getAll_void_tripDtos() throws IOException {
        List<Trip> trips = ParseDataUtils
                .prepareData("unit/service/trip/getAll/positive_data_for_Trip.json", new TypeReference<>() {
                });
        List<TripDto> expected = ParseDataUtils
                .prepareData("unit/service/trip/getAll/result.json", new TypeReference<>() {
                });
        when(tripRepository.findAll()).thenReturn(trips);

        List<TripDto> actual = tripService.getAll();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllByAccountId_accountId_tripDtos() throws IOException {
        List<Trip> trips = ParseDataUtils
                .prepareData("unit/service/trip/getAllByAccountId/positive_data_for_Trip.json", new TypeReference<>() {
                });
        List<TripDto> expected = ParseDataUtils
                .prepareData("unit/service/trip/getAllByAccountId/result.json", new TypeReference<>() {
                });
        Account account = ParseDataUtils
                .prepareData("unit/service/trip/getAllByAccountId/positive_data_for_Account.json", new TypeReference<>() {
                });
        when(tripRepository.findAllByAccountId(eq(1L))).thenReturn(trips);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        List<TripDto> actual = tripService.getAllByAccountId(1L);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deletePlaceFromTrip_tripIdPlaceId_tripPlaceDto() throws IOException {
        Trip trip = ParseDataUtils
                .prepareData("unit/service/trip/deletePlaceFromTrip/positive_data_for_Trip.json", new TypeReference<>() {
                });
        TripPlaceDto expected = ParseDataUtils
                .prepareData("unit/service/trip/deletePlaceFromTrip/result.json", new TypeReference<>() {
                });
        Place place = ParseDataUtils
                .prepareData("unit/service/trip/deletePlaceFromTrip/positive_data_for_Place.json", new TypeReference<>() {
                });
        when(tripRepository.findById(eq(1L))).thenReturn(Optional.of(trip));
        when(placeRepository.findById(eq(2L))).thenReturn(Optional.of(place));
        when(tripRepository.save(trip)).thenReturn(trip);

        TripPlaceDto actual = tripService.deletePlaceFromTrip(1L, 2L);

        assertTrips.assertTrips(expected, actual);
    }

}
