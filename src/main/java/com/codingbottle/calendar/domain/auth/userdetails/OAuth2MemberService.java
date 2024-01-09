//package com.codingbottle.calendar.domain.auth.userdetails;
//
//import com.codingbottle.calendar.domain.auth.attribute.OAuthAttributes;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//
//// 쓸지 말지 고민중
//@Component
//public class OAuth2MemberService extends DefaultOAuth2UserService {
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
//        OAuth2User oauthMember = delegate.loadUser(userRequest);
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//
//        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauthMember.getAttributes());
//
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority("USER")),
//                attributes.getAttributes(),
//                "email");
//
//    }
//}
