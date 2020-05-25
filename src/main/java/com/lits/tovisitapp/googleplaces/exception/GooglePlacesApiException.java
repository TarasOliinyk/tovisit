package com.lits.tovisitapp.googleplaces.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class GooglePlacesApiException extends RuntimeException {
	public GooglePlacesApiException(String message) {
		super(message);
	}
	public GooglePlacesApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
