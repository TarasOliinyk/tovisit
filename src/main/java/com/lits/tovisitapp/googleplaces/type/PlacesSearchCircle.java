package com.lits.tovisitapp.googleplaces.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacesSearchCircle {
	private double latitude;
	private double longitude;
	private int radius;

	private static final double LAT_MIN = -85D;
	private static final double LAT_MAX = 85D;

	private static final double LNG_MIN = -180D;
	private static final double LNG_MAX = +180D;

	private static final int RADIUS_MIN = 1;
	private static final int RADIUS_MAX = 50_000;


	public static class Converter implements org.springframework.core.convert.converter.Converter<String, PlacesSearchCircle> {

		@Override
		public PlacesSearchCircle convert(String source) {
			String errMessage =
					"Circle argument must include three comma-separated values: " +
							"latitude (double), longitude (double), radius_m (int). " +
							"Example: circle=40.7276491,-73.9838429,1000";
			String[] parts = source.split(",");
			if (parts.length != 3) {
				throw new IllegalArgumentException(errMessage);
			}
			double lat;
			double lng;
			int radius;
			try {
				lat = Double.parseDouble(parts[0]);
				if (lat < LAT_MIN || lat > LAT_MAX) {
					throw new IllegalArgumentException("Latitude must be between " + LAT_MIN + " and " + LAT_MAX);
				}
				lng = Double.parseDouble(parts[1]);
				if (lng < LNG_MIN || lng > LNG_MAX) {
					throw new IllegalArgumentException("Longitude must be between " + LNG_MIN + " and " + LNG_MAX);
				}
				radius = Integer.parseInt(parts[2]);
				if (radius < RADIUS_MIN || radius > RADIUS_MAX) {
					throw new IllegalArgumentException("Radius must be between " + RADIUS_MIN + " and " + RADIUS_MAX);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(errMessage);
			}
			return PlacesSearchCircle.builder().latitude(lat).longitude(lng).radius(radius).build();
		}
	}
}
