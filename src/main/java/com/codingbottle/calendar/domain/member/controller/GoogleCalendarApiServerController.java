package com.codingbottle.calendar.domain.member.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("google")
public class GoogleCalendarApiServerController {

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    private static final String APPLICATION_NAME = "CalendarApplication";
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);
    private static final String REDIRECT_URI = "https://calendars2.duckdns.org/google/callback"; // 적절한 콜백 URL로 변경

    private HttpTransport httpTransport = new NetHttpTransport();
    private  JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();


    @GetMapping("/authorize")
    public String authorizeGoogle() throws IOException {
        AuthorizationCodeFlow flow = getAuthorizationCodeFlow();

        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI);

        // 사용자에게 권한 부여 URL을 전달하고, 사용자가 브라우저에서 이 URL로 이동하여 권한 부여를 합니다.
        return authorizationUrl.build();
    }

    @GetMapping("/callback")
    public ResponseEntity callback(@RequestParam("code") String code) throws IOException {
        AuthorizationCodeFlow flow = getAuthorizationCodeFlow();

        // 사용자로부터 받은 인증 코드로 액세스 토큰 및 리프레시 토큰을 요청합니다.
        AuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code)
                .setRedirectUri(REDIRECT_URI);
        Credential credential = flow.createAndStoreCredential(tokenRequest.execute(), null);

        // Calendar 서비스 초기화
        Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // service 객체를 사용하여 Google Calendar API에 액세스할 수 있다
        String pageToken = null;
        Events events = service.events().list("primary").setPageToken(pageToken).execute();
        List<Event> items = events.getItems();

        do {
            for (Event event : items) {
                System.out.println(event.getSummary());
                System.out.println(event.getStart());
                System.out.println(event.getEnd());
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        return new ResponseEntity(items, HttpStatus.OK);
    }

    // OAuth 2.0 인증 플로우 생성
    private AuthorizationCodeFlow getAuthorizationCodeFlow() {

        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
                com.google.api.client.auth.oauth2.BearerToken.authorizationHeaderAccessMethod(),
                httpTransport,
                jsonFactory,
                new com.google.api.client.http.GenericUrl("https://oauth2.googleapis.com/token"),
                new com.google.api.client.auth.oauth2.ClientParametersAuthentication(clientId, clientSecret),
                clientId,
                "https://accounts.google.com/o/oauth2/auth?access_type=offline")
                .setScopes(SCOPES)
                .build();
        return flow;
    }
}
