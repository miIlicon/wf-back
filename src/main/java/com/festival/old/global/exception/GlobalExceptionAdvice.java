package com.festival.old.global.exception;

import com.festival.old.domain.admin.exception.AdminNotFoundException;
import com.festival.old.domain.admin.exception.AdminNotMatchException;
import com.festival.old.domain.film.exception.FilmNotFoundException;
import com.festival.old.domain.fleaMarket.exception.FleaMarketNotFoundException;
import com.festival.old.domain.info.festivalEvent.exception.FestivalEventNotFoundException;
import com.festival.old.domain.info.festivalPub.exception.PubNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ErrorResponse> AdminNotFoundHandleException(AdminNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(AdminNotMatchException.class)
    public ResponseEntity<ErrorResponse> AdminNotMatchHandleException(AdminNotMatchException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(PubNotFoundException.class)
    public ResponseEntity<ErrorResponse> PubNotFoundHandleException(PubNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler({FestivalEventNotFoundException.class})
    public ResponseEntity<ErrorResponse> FestivalEventNotFoundHandleException(FestivalEventNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }
    @ExceptionHandler({FilmNotFoundException.class})
    public ResponseEntity<ErrorResponse> FilmNotFoundHandleException(FilmNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(FleaMarketNotFoundException.class)
    public ResponseEntity<ErrorResponse> FleaMarketNotFoundHandleException(FleaMarketNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }
}
