package com.lits.tovisitapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lits.tovisitapp.dto.TripDto;
import com.lits.tovisitapp.dto.TripPlaceDto;
import com.lits.tovisitapp.model.Trip;
import com.lits.tovisitapp.service.TripService;
import com.lits.tovisitapp.utils.ParseDataUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TripController.class)
public class TripControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private TripService tripService;

    @Test
    public void create_trip_returnTrip() throws Exception {
        TripDto tripDto = ParseDataUtils
                .prepareData("controller/trip/create/positive_data.json", new TypeReference<>() {
                });
        Trip expected = ParseDataUtils
                .prepareData("controller/trip/create/positive_data_for_Trip.json", new TypeReference<>() {
                });
        when(tripService.create(tripDto)).thenReturn(tripDto);

        URI fileName = getClass().getClassLoader()
                .getResource("controller/trip/create/positive_data.json").toURI();
        String content = Files.readString(Paths.get(fileName));
        String result = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/trips/trip")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Trip actual = objectMapper.readValue(result, new TypeReference<>() {
        });
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getOne_tripId_returnTrip() throws Exception {
        TripPlaceDto tripDto = ParseDataUtils
                .prepareData("controller/trip/getOne/positive_data.json", new TypeReference<>() {
                });
        Trip expected = ParseDataUtils
                .prepareData("controller/trip/getOne/positive_data_for_Trip.json", new TypeReference<>() {
                });
        when(tripService.getOne(eq(1L))).thenReturn(tripDto);

        URI fileName = getClass().getClassLoader()
                .getResource("controller/trip/getOne/positive_data.json").toURI();
        String content = Files.readString(Paths.get(fileName));
        String result = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/trips/trip/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Trip actual = objectMapper.readValue(result, new TypeReference<>() {
        });

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAll_void_returnTrips() throws Exception {
        List<TripDto> tripDtos = ParseDataUtils
                .prepareData("controller/trip/getAll/positive_data.json", new TypeReference<>() {
                });
        List<Trip> expected = ParseDataUtils
                .prepareData("controller/trip/getAll/positive_data_for_Trip.json", new TypeReference<>() {
                });
        when(tripService.getAll()).thenReturn(tripDtos);

        URI fileName = getClass().getClassLoader()
                .getResource("controller/trip/getAll/positive_data.json").toURI();
        String content = Files.readString(Paths.get(fileName));
        String result = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/trips"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Trip> actual = objectMapper.readValue(result, new TypeReference<>() {
        });

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllByAccountId_accountId_returnTrips() throws Exception {
        List<TripDto> tripDtos = ParseDataUtils
                .prepareData("controller/trip/getAllByAccountId/positive_data.json", new TypeReference<>() {
                });
        List<Trip> expected = ParseDataUtils
                .prepareData("controller/trip/getAllByAccountId/positive_data_for_Trip.json", new TypeReference<>() {
                });
        when(tripService.getAllByAccountId(eq(1L))).thenReturn(tripDtos);

        URI fileName = getClass().getClassLoader()
                .getResource("controller/trip/getAllByAccountId/positive_data.json").toURI();
        String content = Files.readString(Paths.get(fileName));
        String result = this.mockMvc
                .perform(MockMvcRequestBuilders.get("/trips/account/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Trip> actual = objectMapper.readValue(result, new TypeReference<>() {
        });

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void update_trip_returnTrip() throws Exception {
        TripDto tripDto = ParseDataUtils
                .prepareData("controller/trip/update/positive_data.json", new TypeReference<>() {
                });
        Trip expected = ParseDataUtils
                .prepareData("controller/trip/update/positive_data_for_Trip.json", new TypeReference<>() {
                });
        when(tripService.update(tripDto)).thenReturn(tripDto);

        URI fileName = getClass().getClassLoader()
                .getResource("controller/trip/update/positive_data.json").toURI();
        String content = Files.readString(Paths.get(fileName));
        String result = this.mockMvc
                .perform(MockMvcRequestBuilders.put("/trips/trip/5")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Trip actual = objectMapper.readValue(result, new TypeReference<>() {
        });
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void delete_tripId_void() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/trips/trip/{tripId}", "5")).andExpect(status().isOk());
    }

    @Test
    public void delete_tripIdPlaceId_void() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/trips/trip/{tripId}/places/{placeId}", "5", "5")).andExpect(status().isOk());
    }

}
