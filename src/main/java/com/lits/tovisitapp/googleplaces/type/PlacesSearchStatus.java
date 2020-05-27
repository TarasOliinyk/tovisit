package com.lits.tovisitapp.googleplaces.type;

public enum PlacesSearchStatus {
	OK("OK"),
	ZERO_RESULTS("No results"),
	OVER_QUERY_LIMIT("Account quota is probably exhausted"),
	REQUEST_DENIED("Probably bad API key"),
	INVALID_REQUEST("Probably bad request params or next page not ready yet"),
	UNKNOWN_ERROR("Unexpected error, trying again may be successful"),
	NOT_FOUND("Place not found");

	public final String description;
	PlacesSearchStatus(String description) {
		this.description = description;
	}
}
