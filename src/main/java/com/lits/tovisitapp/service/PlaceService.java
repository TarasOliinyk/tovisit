package com.lits.tovisitapp.service;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import com.lits.tovisitapp.googleplaces.type.SearchableType;

public interface PlaceService {
	PlacesSearchResponse findNearby(PlacesSearchCircle circle, SearchableType type, boolean obtainParentLocation);
	PlacesSearchResponse findByText(String query, PlacesSearchCircle circle, SearchableType type, boolean obtainParentLocation);
	PlacesSearchResponse findNextPage(String nextPageToken, boolean obtainParentLocation);

	PlaceDTO savePlace(PlaceDTO submittedPlace);
	void deletePlace(long placeId);
}
