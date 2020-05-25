package com.lits.tovisitapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lits.tovisitapp.annotation.NotBlankOrNull;
import com.lits.tovisitapp.model.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL) // this will hide null variables from serialized JSON
public class PlaceDTO {
	@Positive(message = "{idPositive}")
	private Long id;

	@NotBlank(message = "{gPlaceIdNotBlank}")
	private String googlePlaceId;

	@NotBlankOrNull(message = "{parentLocationNotBlankOrNull}")
	private String parentLocation;

	@NotBlank(message = "{nameNotBlank}")
	private String name;

	@NotBlank(message = "{fAddressNotBlank}")
	private String formattedAddress;

	@NotNull(message = "{locationLatNotNull}")
	private Double locationLat;

	@NotNull(message = "{locationLngNotNull}")
	private Double locationLng;

	@Range(min=1, max=5, message = "{priceLevelRange}")
	private Integer priceLevel;

	@Range(min=0, max=5, message = "{ratingRange}")
	private Double rating;

	@NotNull(message = "{tripIdNotNull}")
	@Positive(message = "{tripIdPositive}")
	private Long tripId;

	@Builder.Default
	private List<String> types = new ArrayList<>();
}
