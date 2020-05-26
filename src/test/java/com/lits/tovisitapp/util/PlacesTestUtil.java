package com.lits.tovisitapp.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlacesTestUtil {
	private Properties props;

	public PlacesTestUtil(String propertiesFile) {
		props = new Properties();
		var propsStream = PlacesTestUtil.class.getClassLoader().getResourceAsStream(propertiesFile);
		if (propsStream == null) {
			throw new RuntimeException("could not load " + propertiesFile);
		}
		try {
			props.load(propsStream);
		} catch (IOException e) {
			throw new RuntimeException("could not load " + propertiesFile, e);
		}
	}

	public String getProperty(String propertyName) {
		return props.getProperty(propertyName);
	}

	public String textFileToString(String path) {
		try {
			URI uri = ClassLoader.getSystemResource(path).toURI();
			return Files.readString(Paths.get(uri));
		} catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, String> parentCallResponses() {
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
						s -> textFileToString("places/googleResponses/details/" + s + ".json")));
	}
}
