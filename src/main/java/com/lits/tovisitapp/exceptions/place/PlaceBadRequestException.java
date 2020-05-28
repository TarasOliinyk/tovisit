package com.lits.tovisitapp.exceptions.place;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PlaceBadRequestException extends RuntimeException {
	public PlaceBadRequestException(String message) {
		super(message);
	}
}
