package com.lits.tovisitapp.controller;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.service.PlaceService;
import com.lits.tovisitapp.googleplaces.type.SearchableType;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchResponse;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/places")
@Validated
public class PlaceController {

	private PlaceService placeService;

	@Autowired
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
		response.setHeader("pageToken", placeSearchResponse.getNextPageToken());
		return placeSearchResponse.getPlaces();
	}

	@GetMapping("/byText")
	public List<PlaceDTO> findByText(
			@RequestParam @NotBlank(message = "{searchQueryNotBlank}") String query,
			@RequestParam(required = false) PlacesSearchCircle circle,
			@RequestParam(required = false) SearchableType type,
			@RequestParam(required = false, defaultValue = "true") boolean obtainParentLocation,
			final HttpServletResponse response) {
		PlacesSearchResponse placeSearchResponse = placeService.findByText(query, circle, type, obtainParentLocation);
		response.setHeader("pageToken", placeSearchResponse.getNextPageToken());
		return placeSearchResponse.getPlaces();
	}

	@GetMapping("/nextPage")
	public List<PlaceDTO> findNextPage(
			@RequestParam @NotBlank(message = "{pageTokenNotBlank}") String pageToken,
			@RequestParam(required = false, defaultValue = "true") boolean obtainParentLocation,
			final HttpServletResponse response) {
		PlacesSearchResponse placeSearchResponse = placeService.findNextPage(pageToken, obtainParentLocation);
		response.setHeader("pageToken", placeSearchResponse.getNextPageToken());
		return placeSearchResponse.getPlaces();
	}
}
