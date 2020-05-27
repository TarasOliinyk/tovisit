package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.googleplaces.type.SearchableType;
import com.lits.tovisitapp.service.PlaceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.lits.tovisitapp.config.Constants.PAGE_TOKEN_NOT_BLANK;
import static com.lits.tovisitapp.config.Constants.SEARCH_QUERY_NOT_BLANK;

@RestController
@RequestMapping("/places")
@Validated
public class PlaceController {

	private PlaceService placeService;

	public PlaceController(PlaceService placeService) {
		this.placeService = placeService;
	}

	@GetMapping("/nearby")
	public List<PlaceDTO> findNearby(
			@RequestParam PlacesSearchCircle circle,
			@RequestParam(required = false) SearchableType type,
			@RequestParam(required = false, defaultValue = "true") boolean obtainParentLocation,
			final HttpServletResponse response) {
		PlacesSearchResponse placeSearchResponse =
				placeService.findNearby(circle, type, obtainParentLocation);
		response.setHeader("nextPageToken", placeSearchResponse.getNextPageToken());
		return placeSearchResponse.getPlaces();
	}

	@GetMapping("/byText")
	public List<PlaceDTO> findByText(
			@RequestParam @NotBlank(message = SEARCH_QUERY_NOT_BLANK) String query,
			@RequestParam(required = false) PlacesSearchCircle circle,
			@RequestParam(required = false) SearchableType type,
			@RequestParam(required = false, defaultValue = "true") boolean obtainParentLocation,
			final HttpServletResponse response) {
		PlacesSearchResponse placeSearchResponse = placeService.findByText(query, circle, type, obtainParentLocation);
		response.setHeader("nextPageToken", placeSearchResponse.getNextPageToken());
		return placeSearchResponse.getPlaces();
	}

	@GetMapping("/nextPage")
	public List<PlaceDTO> findNextPage(
			@RequestParam @NotBlank(message = PAGE_TOKEN_NOT_BLANK) String pageToken,
			@RequestParam(required = false, defaultValue = "true") boolean obtainParentLocation,
			final HttpServletResponse response) {
		PlacesSearchResponse placeSearchResponse = placeService.findNextPage(pageToken, obtainParentLocation);
		response.setHeader("nextPageToken", placeSearchResponse.getNextPageToken());
		return placeSearchResponse.getPlaces();
	}
}
