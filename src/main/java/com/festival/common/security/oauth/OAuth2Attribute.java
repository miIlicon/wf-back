package com.festival.common.security.oauth;

import com.festival.common.exception.ErrorCode;
import com.festival.common.exception.custom_exception.NotFoundException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Builder(access = AccessLevel.PRIVATE)
@SuppressWarnings("unchecked")
public class OAuth2Attribute {

    private Map<String, Object> attributes; // 소셜 로그인 사용자의 속성 정보를 담는 Map
    private String attributeKey; // 사용자 속성의 키 값
    private String email; // 이메일
    private SocialCode socialCode;


    /**
     * @Description
     * 해당 로그인인 서비스가 kakao인지 google인지 구분하여, 알맞게 매핑을 해주도록 합니다.
     * 여기서 attributeKey는 OAuth2 로그인을 처리한 서비스 명("google","kakao","naver"..)이 되고,
     * userNameAttributeName은 해당 서비스의 map의 키값이 되는 값이됩니다. {google="sub", kakao="id", naver="response"}
     */
    static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
        // 각 플랫폼 별로 제공해주는 데이터가 조금씩 다르기 때문에 분기 처리함.
        return switch (provider) {
            case "kakao" -> kakao(attributeKey, attributes);
            default -> throw new NotFoundException(ErrorCode.NOT_FOUND_OAUTH_PROVIDER);
        };
    }

    private static OAuth2Attribute kakao(String attributeKey, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        return OAuth2Attribute.builder()
                .email((String) kakaoAccount.get("email"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .socialCode(SocialCode.KAKAO)
                .build();
    }

    // OAuth2Attribute -> Map<String, Object>
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attributeKey);
        map.put("key", attributeKey);
        map.put("email", email);
        map.put("socialCode", socialCode);

        return map;
    }
}
