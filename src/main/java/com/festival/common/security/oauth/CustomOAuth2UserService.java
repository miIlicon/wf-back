package com.festival.common.security.oauth;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        /**
         * OAuth2 서비스 id(구글, 카카오, 네이버)
         * OAuth2 로그인 진행 시 키가 되는 필드 값(PK)
         */
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String attributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();


        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(provider, attributeKey, oAuth2User.getAttributes());
        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_MEMBER")),
                memberAttribute, "email");
    }
}

