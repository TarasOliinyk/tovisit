package com.lits.tovisitapp.config;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import com.lits.tovisitapp.googleplaces.type.SearchableType;
import com.lits.tovisitapp.model.Place;
import com.lits.tovisitapp.model.Type;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.http.HttpClient;

@Configuration
public class Config implements WebMvcConfigurer {

	@Bean
	public ModelMapper mapper() {
		ModelMapper modelMapper = new ModelMapper();

		// This mapping allows PlaceDTO to have List<String> instead of List<Type>
		modelMapper.typeMap(String.class, Type.class).setConverter(
				context -> { Type type = new Type(); type.setName(context.getSource()); return type; });
		modelMapper.typeMap(Type.class, String.class).setConverter(
				context -> context.getSource().getName());

		/*
		 * This mapping fixes situation when mapper can't resolve which getter to pick for setter PlaceDTO.setTripId
		 * It finds Place.getTripId(), Place.getTrip().getId() and Place.getTrip().getAccountId() and can't pick one
		 */
		modelMapper.addMappings(new PropertyMap<Place, PlaceDTO>() {
			protected void configure() {
				map().setTripId(source.getTripId());
			}
		});

		return modelMapper;
	}

	// required to call google places api
	@Bean
	public HttpClient httpClient() {
		return HttpClient.newHttpClient();
	}

	// allows to use PlacesSearchCircle and SearchableType enums as request params
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new PlacesSearchCircle.Converter());
		registry.addConverter(new SearchableType.Converter());
	}

}
