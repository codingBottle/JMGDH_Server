//
//package com.codingbottle.calendar.domain.auth.attribute;
//
//import lombok.Builder;
//import lombok.Getter;
//
//
//import java.util.Map;
//
//// 쓸지 말지 고민중
//@Getter
//@Builder
//public class OAuthAttributes {
//    private Map<String, Object> attributes;
//    private String nameAttributeKey;
//    private String name;
//    private String email;
//    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
//
//        if ("kakao".equals(registrationId)) {
//            return ofKakao("id", attributes);}
//
//        return ofGoogle(userNameAttributeName, attributes);
//    }
//
//
//    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
//        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> account = (Map<String, Object>) response.get("profile");
//
//
//        return OAuthAttributes.builder()
//                .name((String) account.get("nickname"))
//                .email((String) response.get("email"))
//                .attributes(response)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }
//
//    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
//        return OAuthAttributes.builder()
//                .name((String) attributes.get("name"))
//                .email((String) attributes.get("email"))
//                .attributes(attributes)
//                .nameAttributeKey(userNameAttributeName)
//                .build();
//    }
//}