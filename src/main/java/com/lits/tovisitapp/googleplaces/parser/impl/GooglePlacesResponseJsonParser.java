package com.lits.tovisitapp.googleplaces.parser.impl;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.googleplaces.parser.GooglePlacesResponseParser;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchStatus;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class GooglePlacesResponseJsonParser implements GooglePlacesResponseParser {

	// configuration that makes jsonPath return null when property not found
	private Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

	public PlacesSearchStatus extractStatus(String placesSearchResponseJson) {
		System.out.println("json: " + placesSearchResponseJson);
		return PlacesSearchStatus.valueOf(
				JsonPath.using(conf).parse(placesSearchResponseJson).read("$.status", String.class));
	}

	public String extractNextPageToken(String placesSearchResponseJson) {
		return JsonPath.using(conf).parse(placesSearchResponseJson).read("$.next_page_token", String.class);
	}

	public List<PlaceDTO> extractPlaces(String placesSearchResponseJson) {
		DocumentContext rawPlacesContext = JsonPath.using(conf).parse(placesSearchResponseJson);

		List<PlaceDTO> result = new ArrayList<>();

		JSONArray rawPlaces = rawPlacesContext.read("$.results", JSONArray.class);
		if (rawPlaces == null) {
			return result;
		}
		for (Object rawPlace : rawPlaces) {
			DocumentContext rawPlaceContext = JsonPath.using(conf).parse(rawPlace);
			PlaceDTO place = PlaceDTO.builder()
					.googlePlaceId(rawPlaceContext.read("$.place_id", String.class))
					.name(rawPlaceContext.read("$.name", String.class))
					.formattedAddress(rawPlaceContext.read("$.formatted_address", String.class))
					.types(Arrays.asList(rawPlaceContext.read("$.types", String[].class)))
					.locationLat(rawPlaceContext.read("$.geometry.location.lat", Double.class))
					.locationLng(rawPlaceContext.read("$.geometry.location.lng", Double.class))
					.priceLevel(rawPlaceContext.read("$.price_level", Integer.class))
					.rating(rawPlaceContext.read("$.rating", Double.class))
					.build();
			result.add(place);
		}
		return result;
	}

	public String extractParentLocation(String placeDetailsSearchResponseJson) {
		boolean isInsideLocality = false;
		String localityName = null;
		String regionName = null;
		String countryName = null;
		DocumentContext documentContext = JsonPath.using(conf).parse(placeDetailsSearchResponseJson);
		JSONArray rawAddressComponents = documentContext.read("$.result.address_components", JSONArray.class);
		if (rawAddressComponents == null) return null; // in case if 'address_components' is missing

		for (Object rawAddressComponent : rawAddressComponents) {
			DocumentContext rawAddressComponentContext = JsonPath.using(conf).parse(rawAddressComponent);
			String[] typesArray = rawAddressComponentContext.read("$.types", String[].class);
			if (typesArray == null) return null; // in case if 'types' is missing
			List<String> types = Arrays.asList(typesArray);
			if (types.contains("postal_code")) continue;

			// if location has address component that is not political, then it is part of address inside locality (except for postal code)
			// if location has address component that has a 'sublocality*' type then is it inside locality
			if (!types.contains("political")
					|| Stream.of(types).anyMatch(t -> t.contains("sublocality"))) {
				isInsideLocality = true;
				continue;
			}

			if (types.contains("locality")) {
				localityName = rawAddressComponentContext.read("$.long_name", String.class);
				continue;
			}
			if (types.contains("administrative_area_level_1")) {
				regionName = rawAddressComponentContext.read("$.long_name", String.class);
				continue;
			}
			if (types.contains("country")) {
				countryName = rawAddressComponentContext.read("$.long_name", String.class);
			}
		}
		// place is inside locality, parent location is locality
		if (isInsideLocality && localityName != null) {
			return localityName;
		}

		// place is locality, parent location is region
		if (localityName != null && regionName != null) {
			return regionName;
		}

		// place is a locality or a region, parent location is country
		if ((localityName != null || regionName != null) && countryName != null) {
			return countryName;
		}

		return null;
	}
}
