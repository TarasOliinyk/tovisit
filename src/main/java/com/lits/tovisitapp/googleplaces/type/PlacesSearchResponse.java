package com.lits.tovisitapp.googleplaces.type;


import com.lits.tovisitapp.dto.PlaceDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PlacesSearchResponse {
	@Builder.Default
	private List<PlaceDTO> places = new ArrayList<>();
	private String nextPageToken;
}
