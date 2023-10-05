package com.festival.common.util;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {
    /**
     * @Description
     * Bearer토큰의 여부에 대해 검증한 뒤 토큰을 반환합니다.
     */
    public static String extractBearerToken(String token) {
        if(token != null){
            if(!token.startsWith("Bearer"))
                throw new BadRequestException(ErrorCode.INVALID_TYPE_TOKEN);
            return token.split(" ")[1].trim();
        }
        return null;
    }

}
