package com.lits.tovisitapp.exceptions.trip;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TripNotFoundException extends RuntimeException {

    public TripNotFoundException(String msg) {
        super(msg);
    }
}
