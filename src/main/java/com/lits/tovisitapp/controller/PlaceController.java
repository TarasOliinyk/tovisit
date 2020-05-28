package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.annotation.NotBlankOrNull;
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
import java.util.List;

import static com.lits.tovisitapp.config.PlaceConstants.PAGE_TOKEN_NOT_BLANK_OR_NULL;
import static com.lits.tovisitapp.config.PlaceConstants.SEARCH_QUERY_NOT_BLANK_OR_NULL;

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
			@RequestParam(required = false) PlacesSearchCircle circle,
			@RequestParam(required = false) SearchableType type,
			@RequestParam(required = false) @NotBlankOrNull(message = PAGE_TOKEN_NOT_BLANK_OR_NULL) String pageToken,
			@RequestParam(required = false, defaultValue = "true") boolean obtainParentLocation,
			final HttpServletResponse response) {
		PlacesSearchResponse placeSearchResponse =
				placeService.findNearby(circle, type, pageToken, obtainParentLocation);
		response.setHeader("nextPageToken", placeSearchResponse.getNextPageToken());
		return placeSearchResponse.getPlaces();
	}

	@GetMapping("/byText")
	public List<PlaceDTO> findByText(
			@RequestParam(required = false) @NotBlankOrNull(message = SEARCH_QUERY_NOT_BLANK_OR_NULL) String query,
			@RequestParam(required = false) SearchableType type,
			@RequestParam(required = false) @NotBlankOrNull(message = PAGE_TOKEN_NOT_BLANK_OR_NULL)String pageToken,
			@RequestParam(required = false, defaultValue = "true") boolean obtainParentLocation,
			final HttpServletResponse response) {
		PlacesSearchResponse placeSearchResponse =
				placeService.findByText(query, type, pageToken, obtainParentLocation);
		response.setHeader("nextPageToken", placeSearchResponse.getNextPageToken());
		return placeSearchResponse.getPlaces();
	}
}
