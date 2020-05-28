package com.lits.tovisitapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PlaceNotFoundException extends RuntimeException {

    public PlaceNotFoundException(String msg) {
        super(msg);
    }
}
