package com.codingbottle.calendar.domain.schedule.service;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.service.MemberService;
import com.codingbottle.calendar.domain.schedule.dto.ScheduleCreateReqDto;
import com.codingbottle.calendar.domain.schedule.entity.CalendarApiIntegration;
import com.codingbottle.calendar.global.utils.GoogleCalendarConverter;
import com.codingbottle.calendar.global.utils.Rfc3339DateConverter;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CalendarApiIntegrationService {
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    private static final String APPLICATION_NAME = "CalendarApplication";
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY, "profile", "email"); // 스코프 설정을 안하면 권한이 없어 정보를 가져올 수 없다
    private static final String REDIRECT_URI = "https://calendars2.duckdns.org/google/callback"; // 적절한 콜백 URL로 변경
    private static final String LOCAL_REDIRECT_URI = "http://localhost:8080/google/callback"; // 적절한 콜백 URL로 변경
    private final HttpTransport httpTransport = new NetHttpTransport();
    private final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    private final MemberService memberService;
    private final ScheduleService scheduleService;
    private final GoogleCalendarConverter googleCalendarConverter;

    public CalendarApiIntegrationService(MemberService memberService, ScheduleService scheduleService, GoogleCalendarConverter googleCalendarConverter) {
        this.memberService = memberService;
        this.scheduleService = scheduleService;
        this.googleCalendarConverter = googleCalendarConverter;
    }

    public void scheduleIntegration(String code) throws IOException {
        Credential credential = getCredential(code);
        // 구글 캘린더 서비스 초기화
        Calendar calendarService = getCalendar(credential);
        // 구글 프로필 서비스 초기화
        PeopleService peopleService = getPeople(credential);

        // 구글 프로필 이메일 가져오기
        Person profile = getProfileFromPeopleService(peopleService);
        // 이메일을 통해 member 불러오기()
        Member member = memberService.getByEmail(profile.getEmailAddresses().get(0).getValue());

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusYears(2);
        LocalDate endDate = now.plusYears(2);

        // 마지막으로 연동된 페이지 토큰 불러오기
        String lastPageToken = member.getCalendarApiIntegration().getLastPageToken();
        // 마지막으로 연동된 이벤트 아이디 불러오기
        String lastEventId = member.getCalendarApiIntegration().getLastEventId();

        // 연동 시작
        do {
            Events events = calendarService.events()
                    // 대표 캘린더
                    .list("primary")
                    // 연동 시작하는 날짜로 부터 +-2년 불러오도록 설정
                    .setTimeMin(DateTime.parseRfc3339(Rfc3339DateConverter.convertNowLocalDateToRfc3339(startDate)))
                    .setTimeMax(DateTime.parseRfc3339(Rfc3339DateConverter.convertNowLocalDateToRfc3339(endDate)))
                    // 반복 이벤트를 싱글 이벤트로 반환되도록 설정
                    .set("singleEvents", true)
                    // 마지막으로 연동했던 페이지 토큰 이후로 불러오도록 설정
                    .setPageToken(lastPageToken)
                    .execute();

            List<Event> items = events.getItems();

            boolean foundStartEvent = lastEventId == null;

            for (Event event : items) {
                if (event.getCreated() == null) {
                    continue;
                }
                if (foundStartEvent) {
                    lastEventId = integrationSchedule(event, member);
                } else if (event.getId().equals(lastEventId)) {
                    foundStartEvent = true;
                }
            }
            lastPageToken = events.getNextPageToken();
        } while (lastPageToken != null);

        CalendarApiIntegration updatedIntegration = member.getCalendarApiIntegration().toBuilder()
                .lastPageToken(lastPageToken)
                .lastEventId(lastEventId)
                .build();
        Member updatedMember = member.toBuilder()
                .calendarApiIntegration(updatedIntegration)
                .build();

        memberService.saveMember(updatedMember);
        log.info("Google 캘린더 API 연동 완료");
    }

    // 이벤트를 우리 서비스에 맞는 스케쥴로 변경 후 저장
    private String integrationSchedule(Event event, Member member) {
        LocalDate startDate;
        LocalDate endDate;
        LocalTime startDateTime = null;
        LocalTime endDateTime = null;
        boolean isAllDay = event.getCreated().isDateOnly();

        // Date = null, DateTime == not null
        if (event.getStart().getDate() == null) {
            startDate = googleCalendarConverter.convertDateTimeToLocalDate(event.getStart().getDateTime());
            endDate = googleCalendarConverter.convertDateTimeToLocalDate(event.getEnd().getDateTime());
            startDateTime = googleCalendarConverter.convertDateTimeToLocalTime(event.getStart().getDateTime());
            endDateTime = googleCalendarConverter.convertDateTimeToLocalTime(event.getEnd().getDateTime());

            LocalDateTime checkStartDateTime = LocalDateTime.of(startDate, startDateTime);
            LocalDateTime checkEndDateTime = LocalDateTime.of(endDate, endDateTime);

            // 두 시간의 차이가 24시간을 초과할 때 종일 일정으로 일정 추가
            if (checkStartDateTime.plusHours(24).isBefore(checkEndDateTime)) {
                startDateTime = null;
                endDateTime = null;
                isAllDay = true;
            }
        }
        // Date == not null, DateTime == null
        else {
            startDate = googleCalendarConverter.convertDateTimeToLocalDate(event.getStart().getDate());
            endDate = googleCalendarConverter.convertDateTimeToLocalDate(event.getEnd().getDate());
            // 구글 캘린더에서 종일 일정의 endDate를 +1해서 넘겨주기에 하루 빼서 저장한다.
            endDate = endDate.minusDays(1);
            isAllDay = true;
        }

        ScheduleCreateReqDto scheduleCreateReqDto = new ScheduleCreateReqDto(
                event.getSummary() == null ? "제목 없음" : event.getSummary(),
                startDate,
                endDate,
                isAllDay,
                startDateTime,
                endDateTime,
                event.getColorId(),
                null,
                null
        );
        scheduleService.create(scheduleCreateReqDto, member.getId());
        return event.getId();
    }

    public String getRequestUrl() {
        AuthorizationCodeFlow flow = getAuthorizationCodeFlow();

        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                .setRedirectUri(REDIRECT_URI);

        // 사용자에게 권한 부여 URL을 전달하고, 사용자가 브라우저에서 이 URL로 이동하여 권한 부여를 합니다.
        return authorizationUrl.build();
    }

    public Credential getCredential(String code) throws IOException {
        AuthorizationCodeFlow flow = getAuthorizationCodeFlow();
        // 사용자로부터 받은 인증 코드로 액세스 토큰 및 리프레시 토큰을 요청합니다.
        AuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code)
                .setRedirectUri(REDIRECT_URI);
        Credential credential = flow.createAndStoreCredential(tokenRequest.execute(), null);

        return credential;
    }

    public Calendar getCalendar(Credential credential) {
        // Calendar 서비스 초기화
        com.google.api.services.calendar.Calendar calendarService = new com.google.api.services.calendar.Calendar.Builder(
                httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return  calendarService;
    }

    public PeopleService getPeople(Credential credential) {
        // People 서비스 초기화
        PeopleService peopleService = new PeopleService.Builder(
                httpTransport, jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return peopleService;
    }

    public Person getProfileFromPeopleService(PeopleService peopleService) throws IOException {
        // 로그인 된 사용자 프로필 가져오기(scope에 profile, email 설정해야 정보를 가져올 수 있음)
        Person profile = peopleService.people().get("people/me")
                .setPersonFields("emailAddresses")
                .execute();

        return  profile;
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
