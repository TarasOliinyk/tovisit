package com.lits.tovisitapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lits.tovisitapp.annotation.NotBlankOrNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

import static com.lits.tovisitapp.config.PlaceConstants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PlaceDTO {
	@Positive(message = ID_POSITIVE)
	private Long id;

	@NotBlank(message = GOOGLE_PLACE_PLACE_ID_NOT_BLANK)
	private String googlePlaceId;

	@NotBlankOrNull(message = PARENT_LOCATION_NOT_BLANK_OR_NULL)
	private String parentLocation;

	@NotBlank(message = NAME_NOT_BLANK)
	private String name;

	@NotBlank(message = FORMATTED_ADDRESS_NOT_BLANK)
	private String formattedAddress;

	@NotNull(message = LOCATION_LAN_NOT_NULL)
	private Double locationLat;

	@NotNull(message = LOCATION_LNG_NOT_NULL)
	private Double locationLng;

	@Range(min=1, max=5, message = PRICE_LEVEL_RANGE)
	private Integer priceLevel;

	@Range(min=0, max=5, message = RATING_RANGE)
	private Double rating;

	@Positive(message = TRIP_ID_POSITIVE)
	private Long tripId;

	@Builder.Default
	private List<String> types = new ArrayList<>();
}
