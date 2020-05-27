package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.googleplaces.type.SearchableType;

public interface PlaceService {
	PlacesSearchResponse findNearby(PlacesSearchCircle circle, SearchableType type, String pageToken, boolean obtainParentLocation);
	PlacesSearchResponse findByText(String query, SearchableType type, String pageToken, boolean obtainParentLocation);

	PlaceDTO findPlaceById(long placeId);
	PlaceDTO savePlace(PlaceDTO submittedPlace);
	void deletePlace(long placeId);
}
