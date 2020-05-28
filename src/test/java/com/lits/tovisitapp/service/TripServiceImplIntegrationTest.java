package com.lits.tovisitapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lits.tovisitapp.dto.CreatePlaceDto;
import com.lits.tovisitapp.dto.TripDto;
import com.lits.tovisitapp.dto.TripPlaceDto;
import com.lits.tovisitapp.utils.AssertTrips;
import com.lits.tovisitapp.utils.ParseDataUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TripServiceImplIntegrationTest {

    @Autowired
    private TripService tripService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AssertTrips assertTrips;

    @Test
    public void create_tripDto_tripDto() throws IOException, URISyntaxException {
        URI fileName = getClass().getClassLoader()
                .getResource("integration/service/trip/create/positive_data.sql").toURI();
        Files.lines(Paths.get(fileName)).forEach(line -> jdbcTemplate.update(line));
        TripDto newTripDto = ParseDataUtils
                .prepareData("integration/service/trip/create/positive_data.json", new TypeReference<>() {
                });
        TripDto expected = ParseDataUtils
                .prepareData("integration/service/trip/create/result.json", new TypeReference<>() {
                });

        TripDto actual = tripService.create(newTripDto);
        expected.setCreatedAt(actual.getCreatedAt());
        expected.setUpdatedAt(actual.getUpdatedAt());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void update_tripDto_tripDto() throws IOException, URISyntaxException {
        URI fileName = getClass().getClassLoader()
                .getResource("integration/service/trip/update/positive_data.sql").toURI();
        Files.lines(Paths.get(fileName)).forEach(line -> jdbcTemplate.update(line));
        TripDto newTripDto = ParseDataUtils
                .prepareData("integration/service/trip/update/positive_data.json", new TypeReference<>() {
                });
        TripDto expected = ParseDataUtils
                .prepareData("integration/service/trip/update/result.json", new TypeReference<>() {
                });

        TripDto actual = tripService.update(newTripDto);
        expected.setCreatedAt(actual.getCreatedAt());
        expected.setUpdatedAt(actual.getUpdatedAt());

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void delete_tripId_void() throws IOException, URISyntaxException {
        URI fileName = getClass().getClassLoader()
                .getResource("integration/service/trip/delete/positive_data.sql").toURI();
        Files.lines(Paths.get(fileName)).forEach(line -> jdbcTemplate.update(line));
        List<TripDto> trips = tripService.getAll();

        tripService.delete(1L);

        Assert.assertEquals(trips.size() - 1, tripService.getAll().size());
    }

    @Test
    public void getOne_tripId_tripDto() throws IOException, URISyntaxException {
        URI fileName = getClass().getClassLoader()
                .getResource("integration/service/trip/getOne/positive_data.sql").toURI();
        Files.lines(Paths.get(fileName)).forEach(line -> jdbcTemplate.update(line));
        TripPlaceDto expected = ParseDataUtils
                .prepareData("integration/service/trip/getOne/result.json", new TypeReference<>() {
                });

        TripPlaceDto actual = tripService.getOne(1L);
        expected.setCreatedAt(actual.getCreatedAt());
        expected.setUpdatedAt(actual.getUpdatedAt());

        assertTrips.assertTrips(expected, actual);
    }

    @Test
    public void getAll_void_tripDtos() throws IOException, URISyntaxException {
        URI fileName = getClass().getClassLoader()
                .getResource("integration/service/trip/getAll/positive_data.sql").toURI();
        Files.lines(Paths.get(fileName)).forEach(line -> jdbcTemplate.update(line));
        List<TripDto> expected = ParseDataUtils
                .prepareData("integration/service/trip/getAll/result.json", new TypeReference<>() {
                });

        List<TripDto> actual = tripService.getAll();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllByAccountId_accountId_tripDtos() throws IOException, URISyntaxException {
        URI fileName = getClass().getClassLoader()
                .getResource("integration/service/trip/getAllByAccountId/positive_data.sql").toURI();
        Files.lines(Paths.get(fileName)).forEach(line -> jdbcTemplate.update(line));
        List<TripDto> expected = ParseDataUtils
                .prepareData("integration/service/trip/getAllByAccountId/result.json", new TypeReference<>() {
                });

        List<TripDto> actual = tripService.getAllByAccountId(3L);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void addPlacesToTrip_tripIdPlaceDtos_tripPlaceDto() throws IOException, URISyntaxException {
        URI fileName = getClass().getClassLoader()
                .getResource("integration/service/trip/addPlacesToTrip/positive_data.sql").toURI();
        Files.lines(Paths.get(fileName)).forEach(line -> jdbcTemplate.update(line));
        List<CreatePlaceDto> newPlaces = ParseDataUtils
                .prepareData("integration/service/trip/addPlacesToTrip/positive_data_for_Place.json", new TypeReference<>() {
                });
        TripPlaceDto expected = ParseDataUtils
                .prepareData("integration/service/trip/addPlacesToTrip/result.json", new TypeReference<>() {
                });

        TripPlaceDto actual = tripService.addPlacesToTrip(1L, newPlaces);
        expected.setCreatedAt(actual.getCreatedAt());
        expected.setUpdatedAt(actual.getUpdatedAt());

        assertTrips.assertTrips(expected, actual);
    }

    @Test
    public void deletePlaceFromTrip_tripIdPlaceId_tripPlaceDto() throws IOException, URISyntaxException {
        URI fileName = getClass().getClassLoader()
                .getResource("integration/service/trip/deletePlaceFromTrip/positive_data.sql").toURI();
        Files.lines(Paths.get(fileName)).forEach(line -> jdbcTemplate.update(line));
        TripPlaceDto expected = ParseDataUtils
                .prepareData("integration/service/trip/deletePlaceFromTrip/result.json", new TypeReference<>() {
                });

        TripPlaceDto actual = tripService.deletePlaceFromTrip(1L, 3L);
        expected.setCreatedAt(actual.getCreatedAt());
        expected.setUpdatedAt(actual.getUpdatedAt());

        assertTrips.assertTrips(expected, actual);
    }

}
