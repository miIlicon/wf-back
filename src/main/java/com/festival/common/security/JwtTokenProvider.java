package com.festival.common.security;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.BadRequestException;
import com.festival.common.exception.custom_exception.InvalidException;
import com.festival.common.redis.RedisService;
import com.festival.common.security.dto.JwtTokenRes;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    private final RedisService redisService;
    private final Key key;

    @Value("${custom.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${custom.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    @Autowired
    public JwtTokenProvider(@Value("${custom.jwt.secret}") String secretKey, RedisService redisService) {
        this.redisService = redisService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * @Description
     * 토큰 발급 및 재발급 시 동작
     */
    public JwtTokenRes createJwtToken(String username, String authorities) {

        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", authorities);

        String accessToken = createAccessToken(claims, new Date(now.getTime() + accessExpirationTime));
        String refreshToken = createRefreshToken(claims, new Date(now.getTime() + refreshExpirationTime));

        return new JwtTokenRes("Bearer", accessToken, refreshToken);
    }

    public String createAccessToken(Claims claims, Date expiredDate){
        return  Jwts.builder()
                .setClaims(claims) // 아이디, 권한정보
                .setExpiration(expiredDate) // 만료기간
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
    public String createRefreshToken(Claims claims, Date expiredDate){
        String refreshToken = Jwts.builder()
                .setClaims(claims) // 아이디, 권한정보
                .setExpiration(expiredDate) // 만료기간
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        redisService.setRefreshToken(claims.getSubject(), refreshToken);
        return  refreshToken;
    }

    public Authentication getAuthenticationByAccessToken(String accessToken){
        Claims claims = parseAccessTokenClaims(accessToken);

        if (claims.get("roles") == null){
            throw new InvalidException(ErrorCode.EMPTY_AUTHORITY);
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Claims parseAccessTokenClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        }
        catch (ExpiredJwtException e) {
            throw new InvalidException(ErrorCode.EXPIRED_PERIOD_ACCESS_TOKEN);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
    public Authentication getAuthenticationByRefreshToken(String refreshToken){
        Claims claims = parseRefreshTokenClaims(refreshToken);

        if (claims.get("roles") == null){
            throw new InvalidException(ErrorCode.EMPTY_AUTHORITY);
        }
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Claims parseRefreshTokenClaims(String refreshToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken).getBody();
        }
        catch (ExpiredJwtException e) {
            throw new InvalidException(ErrorCode.EXPIRED_PERIOD_REFRESH_TOKEN);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    /**
     * @Description
     * 토큰의 만료여부와 유효성에 대해 검증합니다.
     */
    public boolean validateAccessToken(String accessToken) {
        try {
            parseToken(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            throw new InvalidException(ErrorCode.EXPIRED_PERIOD_ACCESS_TOKEN);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
    public boolean validateRefreshToken(String refreshToken) {
        try {
            parseToken(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            throw new InvalidException(ErrorCode.EXPIRED_PERIOD_REFRESH_TOKEN);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new InvalidException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    private Jws<Claims> parseToken(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public void checkLoginByAccessToken(String accessToken) {
        if (redisService.isLogin(parseAccessTokenClaims(accessToken).getSubject()) == false)
            throw new InvalidException(ErrorCode.LOGOUTED_TOKEN);
    }
    public void checkLoginByRefreshToken(String refreshToken) {
        if (redisService.isLogin(parseRefreshTokenClaims(refreshToken).getSubject()) == false)
            throw new InvalidException(ErrorCode.LOGOUTED_TOKEN);
    }
}
