package com.codingbottle.calendar.domain.schedule.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class GoogleCalendarApiLocalController {

    private static final String APPLICATION_NAME = "Your_Application_Name";
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);
    private static final String REDIRECT_URI = "http://localhost:8888/Callback"; // This should be the same as the one in the Google Cloud Console

    @GetMapping("/authorize-google")
    public String authorizeGoogle(@AuthenticationPrincipal String email) throws IOException {
        // HTTP Transport 초기화
        NetHttpTransport httpTransport = new NetHttpTransport();

        // JSON Factory 초기화
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // OAuth 2.0 인증 플로우 생성
        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(
                com.google.api.client.auth.oauth2.BearerToken.authorizationHeaderAccessMethod(),
                httpTransport,
                jsonFactory,
                new com.google.api.client.http.GenericUrl("https://accounts.google.com/o/oauth2/token"),
                new com.google.api.client.auth.oauth2.ClientParametersAuthentication(clientId, clientSecret),
                clientId,
                "https://accounts.google.com/o/oauth2/auth?access_type=offline")
                .setScopes(SCOPES)
                .build();

        // 로컬 서버를 사용하여 사용자에게 권한을 부여하고 인증 코드를 받는다
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize(email);


        // Calendar 서비스 초기화
        Calendar service = new Calendar.Builder(
                httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        // service 객체를 사용하여 Google Calendar API에 액세스할 수 있다
        String pageToken = null;
        do {
            Events events = service.events().list("primary").setPageToken(pageToken).execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                System.out.println(event.getSummary());
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        return "Success";
    }
}