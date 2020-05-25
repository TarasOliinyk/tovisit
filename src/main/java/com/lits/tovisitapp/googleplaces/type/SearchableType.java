package com.lits.tovisitapp.googleplaces.type;


import java.util.stream.Stream;

/**
 * Types that google allows to be passed as a search argument for places search:<br/>
 * <a href="https://developers.google.com/places/web-service/supported_types">Google Places API</a>
 */
@SuppressWarnings("unused")
public enum SearchableType {
	ACCOUNTING,
	AIRPORT,
	AMUSEMENT_PARK,
	AQUARIUM,
	ART_GALLERY,
	ATM,
	BAKERY,
	BANK,
	BAR,
	BEAUTY_SALON,
	BICYCLE_STORE,
	BOOK_STORE,
	BOWLING_ALLEY,
	BUS_STATION,
	CAFE,
	CAMPGROUND,
	CAR_DEALER,
	CAR_RENTAL,
	CAR_REPAIR,
	CAR_WASH,
	CASINO,
	CEMETERY,
	CHURCH,
	CITY_HALL,
	CLOTHING_STORE,
	CONVENIENCE_STORE,
	COURTHOUSE,
	DENTIST,
	DEPARTMENT_STORE,
	DOCTOR,
	DRUGSTORE,
	ELECTRICIAN,
	ELECTRONICS_STORE,
	EMBASSY,
	FIRE_STATION,
	FLORIST,
	FUNERAL_HOME,
	FURNITURE_STORE,
	GAS_STATION,
	GYM,
	HAIR_CARE,
	HARDWARE_STORE,
	HINDU_TEMPLE,
	HOME_GOODS_STORE,
	HOSPITAL,
	INSURANCE_AGENCY,
	JEWELRY_STORE,
	LAUNDRY,
	LAWYER,
	LIBRARY,
	LIGHT_RAIL_STATION,
	LIQUOR_STORE,
	LOCAL_GOVERNMENT_OFFICE,
	LOCKSMITH,
	LODGING,
	MEAL_DELIVERY,
	MEAL_TAKEAWAY,
	MOSQUE,
	MOVIE_RENTAL,
	MOVIE_THEATER,
	MOVING_COMPANY,
	MUSEUM,
	NIGHT_CLUB,
	PAINTER,
	PARK,
	PARKING,
	PET_STORE,
	PHARMACY,
	PHYSIOTHERAPIST,
	PLUMBER,
	POLICE,
	POST_OFFICE,
	PRIMARY_SCHOOL,
	REAL_ESTATE_AGENCY,
	RESTAURANT,
	ROOFING_CONTRACTOR,
	RV_PARK,
	SCHOOL,
	SECONDARY_SCHOOL,
	SHOE_STORE,
	SHOPPING_MALL,
	SPA,
	STADIUM,
	STORAGE,
	STORE,
	SUBWAY_STATION,
	SUPERMARKET,
	SYNAGOGUE,
	TAXI_STAND,
	TOURIST_ATTRACTION,
	TRAIN_STATION,
	TRANSIT_STATION,
	TRAVEL_AGENCY,
	UNIVERSITY,
	VETERINARY_CARE,
	ZOO,
	;

	public static class Converter implements org.springframework.core.convert.converter.Converter<String, SearchableType> {
		@Override
		public SearchableType convert(String source) {
			return Stream.of(SearchableType.values())
					.filter(st -> st.name().equalsIgnoreCase(source))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("Type '" + source + "' is not supported. " +
							"See supported types here: https://developers.google.com/places/web-service/supported_types"));
		}
	}
}
