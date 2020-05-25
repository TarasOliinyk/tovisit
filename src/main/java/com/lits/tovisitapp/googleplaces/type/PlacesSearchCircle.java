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
				lng = Double.parseDouble(parts[1]);
				radius = Integer.parseInt(parts[2]);
				final int maxRadius = 50_000;
				if (radius > maxRadius) {
					throw new IllegalArgumentException("Radius cannot be more than " + maxRadius);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(errMessage);
			}
			return PlacesSearchCircle.builder().latitude(lat).longitude(lng).radius(radius).build();
		}
	}
}
