package com.lits.tovisitapp.utils;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlaceTestUtil {

	/**
	 * To load JSON from file into a string
	 */
	public String textFileToString(String path) {
		try {
			URI uri = ClassLoader.getSystemResource(path).toURI();
			return Files.readString(Paths.get(uri));
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Place details responses, key are Google ids, Values are JSONs
	 */
	public Map<String, String> detailsCoffeeInLvivNoType() {
		return Stream.of(
				"ChIJ5U2t6mvdOkcRsQgxhqKvdgw",
				"ChIJ4WIU6G7dOkcRdId98AdAOEM",
				"ChIJVaQAVGzdOkcR9IjL6lVjkk4",
				"ChIJkdwvGnLdOkcRAd0WeWDFkIw",
				"ChIJJ5xc0m3dOkcRvy8HrBsJt8s",
				"ChIJV-EEvm3dOkcRnVLmQj7vMXE",
				"ChIJ6RUwuXHdOkcRG8iHFg79MJE",
				"ChIJUWut3nHdOkcRo-6XC3YvrzY",
				"ChIJtbGK2EDdOkcRcQ6C1IBpF-Y",
				"ChIJfbGqM2zdOkcRENjictwEkpE",
				"ChIJCznqLWzdOkcRcLFAoTrxcCM",
				"ChIJsY1uBm7dOkcRU2hzlVrdzt4",
				"ChIJgfKDEGzdOkcR6hbpazbHJb0",
				"ChIJdR6O7WvdOkcRWBDqy8ywoyc",
				"ChIJa_dIWWzdOkcRd1_SP-1nrV4",
				"ChIJD5mGWmzdOkcRfcwx9CM1rHY",
				"ChIJp0SYJ2zdOkcRx9e3scZ_Z5E",
				"ChIJf_2D-UHdOkcRIpNriRPS9jE",
				"ChIJrfN-u27dOkcRc9yYTzu3e-k",
				"ChIJD5h9sFzdOkcRT0ZKoAxDMyQ")
				.collect(Collectors.toMap(
						s -> s,
						s -> textFileToString("unit/service/place/googleResponses/details/coffeeInLviv_NoType/" + s + ".json")));
	}

	/**
	 * Place details responses, key are Google ids, Values are JSONs
	 */
	public Map<String, String> detailsLvivCenter500mNoTypes() {
		return Stream.of(
				"ChIJ39JkeG3dOkcRAOjUgCx4lwo",
				"ChIJCznqLWzdOkcRcLFAoTrxcCM",
				"ChIJeeKGRm7dOkcRvF2oZX__VlM",
				"ChIJfbGqM2zdOkcRENjictwEkpE",
				"ChIJgUQd1GjdOkcRr1SYSqLr1rE",
				"ChIJia2Ji27dOkcRr7kSnJwu-8Y",
				"ChIJJ5xc0m3dOkcRvy8HrBsJt8s",
				"ChIJK2CoImzdOkcRtY18SYV1LZ4",
				"ChIJk6WtXGzdOkcRGqLG3w2Q7Pg",
				"ChIJKwDQB27dOkcRKxLn9djaGps",
				"ChIJlb-OOG7dOkcR5eDP9ys5-Ws",
				"ChIJrfN-u27dOkcRc9yYTzu3e-k",
				"ChIJrWRVzW3dOkcR95jZRGCbtIs",
				"ChIJRxsA6G7dOkcRMRJZd9zZWNs",
				"ChIJs--2vW3dOkcRQpB3AkElOg0",
				"ChIJTRlLlG3dOkcRQwiyfGeiZU4",
				"ChIJvUvgu23dOkcRDY8RgeG9soI",
				"ChIJWVuDzW3dOkcRqmUAKw3mOhU",
				"ChIJyz0Wam_dOkcRr4q6xqfAU-Y",
				"ChIJZxADPGzdOkcR8F19RSXX8zM")
				.collect(Collectors.toMap(
						s -> s,
						s -> textFileToString("unit/service/place/googleResponses/details/LvivCenter500mCafeType/" + s + ".json")));
	}

	/**
	 * This method builds "complete" HttpResponse, didn't found more elegant way to do this
	 */
	public HttpResponse<Object> buildResponse(HttpRequest request, URI uri, String body) {
		return new HttpResponse<>() {
			@Override
			public int statusCode() {
				return 200;
			}
			@Override
			public HttpRequest request() {
				return request;
			}
			@Override
			public Optional<HttpResponse<Object>> previousResponse() {
				return Optional.empty();
			}
			@Override
			public HttpHeaders headers() {
				return null;
			}
			@Override
			public String body() {
				return body;
			}
			@Override
			public Optional<SSLSession> sslSession() {
				return Optional.empty();
			}
			@Override
			public URI uri() {
				return uri;
			}
			@Override
			public HttpClient.Version version() {
				return HttpClient.Version.HTTP_1_1;
			}
		};
	}

	/**
	 * Calculates distance in meters between two lat-lng points, used to check placeNearby results
	 */
	public double metersBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // Radius of the earth
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters
		distance = Math.pow(distance, 2);
		return Math.sqrt(distance);
	}
}
