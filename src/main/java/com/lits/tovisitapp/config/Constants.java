package com.lits.tovisitapp.config;

public final class Constants {
	// dto.PlaceDTO
	public static final String ID_POSITIVE = "Id must be positive integer";
	public static final String GOOGLE_PLACE_PLACE_ID_NOT_BLANK = "GooglePlaceId cannot be blank";
	public static final String PARENT_LOCATION_NOT_BLANK_OR_NULL="ParentLocation cannot be blank or empty bu can be null";
	public static final String NAME_NOT_BLANK="Name cannot be blank";
	public static final String FORMATTED_ADDRESS_NOT_BLANK="FormattedAddress cannot be blank";
	public static final String LOCATION_LAN_NOT_NULL="LocationLat cannot be null";
	public static final String LOCATION_LNG_NOT_NULL="LocationLng cannot be null";
	public static final String PRICE_LEVEL_RANGE="PriceLevel must be from 1 (inclusive) to 5 (inclusive) or null";
	public static final String RATING_RANGE="Rating must be from 1.0 (inclusive) to 5.0 (inclusive) or null";
	public static final String TRIP_ID_POSITIVE="TripId must be positive";

	// controller.PlaceController
	public static final String SEARCH_QUERY_NOT_BLANK="Search query cannot be blank";
	public static final String PAGE_TOKEN_NOT_BLANK="Page token cannot be blank";

	// used by googleplaces.typePlacesSearchCircle
	public static final double CIRCLE_LAT_MIN = -85D;
	public static final double CIRCLE_LAT_MAX = 85D;
	public static final double CIRCLE_LNG_MIN = -180D;
	public static final double CIRCLE_LNG_MAX = +180D;
	public static final int CIRCLE_RADIUS_MIN = 1;
	public static final int CIRCLE_RADIUS_MAX = 50_000;
	public static final String ERR_MESSAGE =
			"Circle argument must include three comma-separated values: " +
			"latitude (double), longitude (double), radius_m (int). " +
			"Example: circle=40.7276491,-73.9838429,1000";


}
