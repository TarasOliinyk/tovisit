package com.lits.tovisitapp.dto;

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
public class PlaceDTO {
	@Positive
	private Long id;

	@NotBlank
	private String googlePlaceId;

	@NotBlank
	private String parentLocation;

	@NotBlank
	private String name;

	@NotBlank
	private String formattedAddress;

	@NotNull
	private BigDecimal locationLat;

	@NotNull
	private BigDecimal locationLng;

	@Range(min=1, max=5)
	private Integer priceLevel;

	@Range(min=0, max=5)
	private BigDecimal rating;

	@Positive
	private Long tripId;

	@Builder.Default
	private List<Type> types = new ArrayList<>();
}
