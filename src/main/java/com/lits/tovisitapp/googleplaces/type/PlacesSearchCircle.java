package com.lits.tovisitapp.googleplaces.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lits.tovisitapp.config.Constants.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacesSearchCircle {
	private double latitude;
	private double longitude;
	private int radius;

	public static class Converter implements org.springframework.core.convert.converter.Converter<String, PlacesSearchCircle> {
		@Override
		public PlacesSearchCircle convert(String source) {
			String[] parts = source.split(",");
			if (parts.length != 3) {
				throw new IllegalArgumentException(ERR_MESSAGE);
			}
			double lat;
			double lng;
			int radius;
			try {
				lat = Double.parseDouble(parts[0]);
				if (lat < CIRCLE_LAT_MIN || lat > CIRCLE_LAT_MAX) {
					throw new IllegalArgumentException("Latitude must be between " + CIRCLE_LAT_MIN + " and " + CIRCLE_LAT_MAX);
				}
				lng = Double.parseDouble(parts[1]);
				if (lng < CIRCLE_LNG_MIN || lng > CIRCLE_LNG_MAX) {
					throw new IllegalArgumentException("Longitude must be between " + CIRCLE_LNG_MIN + " and " + CIRCLE_LNG_MAX);
				}
				radius = Integer.parseInt(parts[2]);
				if (radius < CIRCLE_RADIUS_MIN || radius > CIRCLE_RADIUS_MAX) {
					throw new IllegalArgumentException("Radius must be between " + CIRCLE_RADIUS_MIN + " and " + CIRCLE_RADIUS_MAX);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_MESSAGE);
			}
			return PlacesSearchCircle.builder().latitude(lat).longitude(lng).radius(radius).build();
		}
	}
}
