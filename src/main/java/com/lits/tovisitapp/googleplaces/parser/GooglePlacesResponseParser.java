package com.lits.tovisitapp.googleplaces.parser;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchStatus;

import java.util.List;

public interface GooglePlacesResponseParser {
	PlacesSearchStatus extractStatus(String placesSearchResponseJson);
	String extractNextPageToken(String placesSearchResponseJson);
	List<PlaceDTO> extractPlaces(String placesSearchResponseJson);
	String extractParentLocation(String placeDetailsSearchResponseJson);
}
