package com.lits.tovisitapp.config;

import com.lits.tovisitapp.dto.PlaceDTO;
import com.lits.tovisitapp.googleplaces.type.PlacesSearchCircle;
import com.lits.tovisitapp.googleplaces.type.SearchableType;
import com.lits.tovisitapp.model.Place;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@PropertySource("classpath:googlePlaces.properties")
public class Config implements WebMvcConfigurer {

	@Bean
	public ModelMapper mapper() {
		ModelMapper modelMapper = new ModelMapper();

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

	// required to make http calls to GooglePlaces API
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// to use validation messages from properties file
	@Bean
	public LocalValidatorFactoryBean getValidator() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:validationMessages");
		messageSource.setDefaultEncoding("UTF-8");

		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource);
		return bean;
	}

	// allows to use PlacesSearchCircle and SearchableType enums as request params
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new PlacesSearchCircle.Converter());
		registry.addConverter(new SearchableType.Converter());
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}


}
