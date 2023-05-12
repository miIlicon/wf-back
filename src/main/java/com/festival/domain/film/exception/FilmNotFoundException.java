package com.festival.domain.film.exception;

import com.festival.global.exception.ErrorCode;
import com.festival.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class FilmNotFoundException extends baseException {
    private static final ErrorCode code = ErrorCode.FILM_NOT_FOUND;

    public FilmNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}