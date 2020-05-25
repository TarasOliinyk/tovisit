package com.lits.tovisitapp.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class Mapper {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

