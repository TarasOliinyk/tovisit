package com.lits.tovisitapp.utils;

import com.lits.tovisitapp.dto.TripPlaceDto;
import com.lits.tovisitapp.model.Place;
import org.junit.Assert;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssertTrips {

    public void assertTrips(TripPlaceDto expected, TripPlaceDto actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        Assert.assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        Assert.assertEquals(expected.getName(), actual.getName());

        List<Place> expectedPlaces = expected.getPlaces();
        List<Place> actualPlaces = actual.getPlaces();

        Assert.assertEquals(expectedPlaces.size(), actualPlaces.size());
        for (int i = 0; i < expectedPlaces.size(); i++) {
            Assert.assertEquals(expectedPlaces.get(i).getId(), actualPlaces.get(i).getId());
            Assert.assertEquals(expectedPlaces.get(i).getGooglePlaceId(), actualPlaces.get(i).getGooglePlaceId());
            Assert.assertEquals(expectedPlaces.get(i).getParentLocation(), actualPlaces.get(i).getParentLocation());
            Assert.assertEquals(expectedPlaces.get(i).getName(), actualPlaces.get(i).getName());
            Assert.assertEquals(expectedPlaces.get(i).getFormattedAddress(), actualPlaces.get(i).getFormattedAddress());

            Assert.assertEquals(expectedPlaces.get(i).getLocationLat(), actualPlaces.get(i).getLocationLat());
            Assert.assertEquals(expectedPlaces.get(i).getLocationLng(), actualPlaces.get(i).getLocationLng());
            Assert.assertEquals(expectedPlaces.get(i).getPriceLevel(), actualPlaces.get(i).getPriceLevel());
            Assert.assertEquals(expectedPlaces.get(i).getRating(), actualPlaces.get(i).getRating());
            Assert.assertEquals(expectedPlaces.get(i).getTypes(), actualPlaces.get(i).getTypes());
        }
    }

}
