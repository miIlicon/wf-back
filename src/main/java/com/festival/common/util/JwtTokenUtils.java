package com.festival.common.util;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.BadRequestException;
import com.festival.common.redis.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final RedisService redisService;

    @Value("${custom.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;
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

    public boolean isLogin(String username){
        return redisService.getData("Login_" + username) != null;
    }
    public void setRefreshToken(String username, String refreshToken){
        redisService.setData("Login_" + username, refreshToken, refreshExpirationTime, TimeUnit.SECONDS);
    }
    public void rotateRefreshToken(String name, String refreshToken) {
        setRefreshToken(name, refreshToken);
    }

    public void deleteRefreshToken(String name) {
        redisService.deleteData("Login_" + name);
    }

    public String getRefreshToken(String name) {
        return redisService.getData("Login_" + name).toString();
    }

}
