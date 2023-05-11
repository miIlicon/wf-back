package com.festival.global.exception;

import com.festival.domain.admin.exception.AdminNotFoundException;
import com.festival.domain.admin.exception.AdminNotMatchException;
import com.festival.domain.info.festivalEvent.exception.FestivalEventImageNotFoundException;
import com.festival.domain.info.festivalEvent.exception.FestivalEventNotFoundException;
import com.festival.domain.info.festivalPub.exception.PubNotFoundException;
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

    @ExceptionHandler({PubNotFoundException.class, FestivalEventNotFoundException.class})
    public ResponseEntity<ErrorResponse> EntityNotFoundHandleException(PubNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }
    @ExceptionHandler({FestivalEventImageNotFoundException.class})
    public ResponseEntity<ErrorResponse> ImageNotFoundHandleException(PubNotFoundException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("[exceptionHandle] ex", e);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), e.getMessage());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

}
