package com.footzone.footzone.notification;

import com.footzone.footzone.entity.footballSession.SessionService;
import com.footzone.footzone.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Bahodir Hasanov 6/20/2022 2:56 PM
@RestController
@RequestMapping("${app.domain}/notification")
public class NotificationController {
    @Autowired
    SessionService sessionService;

    @GetMapping
    public HttpEntity<?> sendNotification(@AuthenticationPrincipal User user) {
        return sessionService.hasNotification(user);
    }
}
