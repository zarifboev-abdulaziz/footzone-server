package com.footzone.footzone.entity.footballSession;


//Asliddin Kenjaev, created: May, 27 2022 5:08 PM 

import com.footzone.footzone.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("${app.domain}" + "/session")
@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/day/{stadiumId}/{date}")
    public HttpEntity<?> getSessionsForSpecificDay(@PathVariable UUID stadiumId, @PathVariable String date) {
        return sessionService.getSessionsForSpecificDay(stadiumId, LocalDate.parse(date));
    }

    /*
    Session Creation
    User can send request for Stadium owner through this method
     */
    @PostMapping
    public HttpEntity<?> createRequestForStadium(@RequestBody SessionDto sessionDto, @AuthenticationPrincipal User user) {
        return sessionService.createSession(sessionDto, user);
    }

    @GetMapping("/requests/{status}")
    public HttpEntity<?> getSessionRequests(@PathVariable String status, @AuthenticationPrincipal User user) {
        return sessionService.getSessionRequests(status, user);
    }


    @PostMapping("/acceptOrDecline")
    public HttpEntity<?> acceptOrDeclineSession(@RequestBody AcceptDeclineDto acceptDeclineDto) {
        return sessionService.acceptOrDeclineSession(acceptDeclineDto);
    }

    @PutMapping("/{sessionId}")
    public HttpEntity<?> editSession(@RequestBody SessionDto sessionDto, @PathVariable UUID sessionId) {
        return sessionService.editSession(sessionDto, sessionId);
    }

    @DeleteMapping("/{sessionId}")
    public HttpEntity<?> deleteSession(@PathVariable UUID sessionId) {
        return sessionService.deleteSession(sessionId);
    }

    @GetMapping("/playing/soon")
    public ResponseEntity<?> getSessionsOfStadiumsPlayingSoon(@AuthenticationPrincipal User user) {
        return sessionService.getSessionsOfStadiumsPlayingSoon(user.getId());
    }

    @GetMapping("/history/{userId}")
    public HttpEntity<?> viewPlayedSessionsOfStadiums(@PathVariable UUID userId) {
        return sessionService.viewPlayedSessionsOfStadiums(userId);
    }
}
