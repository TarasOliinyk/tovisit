package com.lits.tovisitapp.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

// Don't extend ResponseEntityExceptionHandler, this will result in validation messages ignored on @Valid object from POST or PUT
@ControllerAdvice
public class GlobalExceptionHandler {

	private ObjectMapper jackson;

	@Autowired
	public GlobalExceptionHandler(ObjectMapper jackson) {
		this.jackson = jackson;
	}

	/*
	 * This method converts ConstraintViolationsExceptions from 500 to 400
	 * It will be called when controller's validating annotation on path variables or requestParams triggered
	 * It will not be called when annotations on @Valid object from POST or PUT is triggered
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<String> handleException(ConstraintViolationException e, HttpServletRequest request) throws JsonProcessingException {
		String message = e.getConstraintViolations().iterator().next().getMessage();
		return buildJsonErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
	}

	/*
	 * This method will hide standard conversion error validation messages,
	 * Which usually displays java types and other unnecessary info
	 */
	@ExceptionHandler(value = ConversionFailedException.class)
	public ResponseEntity<String> handleException(ConversionFailedException e, HttpServletRequest request) throws JsonProcessingException {
		return buildJsonErrorResponse(HttpStatus.BAD_REQUEST, e.getCause().getMessage(), request.getRequestURI());
	}

	private ResponseEntity<String> buildJsonErrorResponse(HttpStatus status, String message, String path) throws JsonProcessingException {
		Map<String, Object> errAttribs = new LinkedHashMap<>();
		errAttribs.put("timestamp", Instant.now());
		errAttribs.put("status", status.value());
		errAttribs.put("message", message);
		errAttribs.put("path", path);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(jackson.writeValueAsString(errAttribs), headers, status);
	}
}
