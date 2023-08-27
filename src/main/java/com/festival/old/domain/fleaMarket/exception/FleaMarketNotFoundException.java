package com.festival.old.domain.fleaMarket.exception;

import com.festival.old.global.exception.ErrorCode;
import com.festival.old.global.exception.baseException;
import org.springframework.http.HttpStatus;

public class FleaMarketNotFoundException extends baseException {

    private static final ErrorCode code = ErrorCode.FLEA_MARKET_NOT_FOUND;

    public FleaMarketNotFoundException(String message) {
        super(code, HttpStatus.BAD_REQUEST, message);
    }
}
