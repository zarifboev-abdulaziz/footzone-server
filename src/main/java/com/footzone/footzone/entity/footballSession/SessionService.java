package com.footzone.footzone.entity.footballSession;


//Asliddin Kenjaev, created: May, 27 2022 5:06 PM 

import com.footzone.footzone.common.ApiResponse;
import com.footzone.footzone.entity.date.Date;
import com.footzone.footzone.entity.date.DateRepository;
import com.footzone.footzone.entity.device.DeviceService;
import com.footzone.footzone.entity.stadium.Stadium;
import com.footzone.footzone.entity.stadium.StadiumRepository;
import com.footzone.footzone.entity.time.Time;
import com.footzone.footzone.entity.time.TimeRepository;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.entity.user.UserRepository;
import com.footzone.footzone.entity.workingDay.WorkingDay;
import com.footzone.footzone.enums.Status;
import com.footzone.footzone.notification.Data;
import com.footzone.footzone.notification.NotificationForSent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    private final DateRepository dateRepository;

    private final TimeRepository timeRepository;

    private final UserRepository userRepository;

    private final StadiumRepository stadiumRepository;

    private final RestTemplate restTemplate;

    private final DeviceService deviceService;

    @Value("${AUTHORIZATION_KEY_FOR_NOTIFICATION}")
    String notificationHeader;

    public HttpEntity<?> getSessionRequests(String status, User currentUser) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(currentUser.getPhoneNumber());
        //TODO change static phoneNumber to Current User;
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("user not found", false), HttpStatus.OK);
        }

        List<SessionProjection> sessionList;

        if (status.equals("PENDING")) {
            sessionList = sessionRepository.getPendingSessionsForOwner(Status.PENDING.toString(), optionalUser.get().getId());
        } else if (status.equals("PLAYED")) {
            sessionList = sessionRepository.getPlayedSessionsForOwner(Status.PLAYED.toString(), optionalUser.get().getId());
        } else {
            sessionList = sessionRepository.getNotificationSessionsForOwner(optionalUser.get().getId());
        }

        return ResponseEntity.status(200).body(new ApiResponse("Ok", true, sessionList));
    }


    public HttpEntity<?> createSession(SessionDto sessionDto, User currentUser) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(sessionDto.getStadiumId());
        Optional<User> optionalUser = userRepository.findByPhoneNumber(currentUser.getPhoneNumber());
        //TODO change static phoneNumber to Current User;

        if (!optionalStadium.isPresent() && !optionalUser.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("user or stadium not found", false, null), HttpStatus.NOT_FOUND);
        }

        Session session = new Session();
        LocalDate date;
        LocalTime sessionStartTime;
        LocalTime sessionEndTime;

        Optional<Date> optionalDate = dateRepository.findByLocalDate(sessionDto.getStartDate());
        if (optionalDate.isPresent()) {
            session.setStartDate(optionalDate.get());
            date = optionalDate.get().getLocalDate();
        } else {
            session.setStartDate(dateRepository.save(new Date(sessionDto.getStartDate())));
            date = sessionDto.getStartDate();
        }

        Optional<Time> startTime = timeRepository.findByTime(sessionDto.getStartTime());
        if (startTime.isPresent()) {
            session.setStartTime(startTime.get());
            sessionStartTime = startTime.get().getTime();
        } else {
            session.setStartTime(timeRepository.save(new Time(sessionDto.getStartTime())));
            sessionStartTime = sessionDto.getStartTime();
        }

        Optional<Time> endTime = timeRepository.findByTime(sessionDto.getEndTime());
        if (endTime.isPresent()) {
            session.setEndTime(endTime.get());
            sessionEndTime = endTime.get().getTime();
        } else {
            session.setEndTime(timeRepository.save(new Time(sessionDto.getEndTime())));
            sessionEndTime = sessionDto.getEndTime();
        }

        session.setStatus(Status.PENDING);
        session.setStadium(optionalStadium.get());
        session.setUser(optionalUser.get());
        session.setCreatedBy(optionalUser.get());
        Session savedSession = sessionRepository.save(session);

        notificationThread(optionalStadium.get().getOwner().getId(),
                Status.PENDING.toString(),
                new SessionDto2(
                        savedSession.getId().toString(),
                        date.toString(),
                        sessionStartTime.toString(),
                        sessionEndTime.toString(),
                        optionalStadium.get().getName(),
                        true
                ));
        return new ResponseEntity<>(new ApiResponse("saved successfully", true, true), HttpStatus.CREATED);
    }

    public HttpEntity<?> acceptOrDeclineSession(AcceptDeclineDto acceptDeclineDto) {
        Optional<Session> optionalSession = sessionRepository.findById(acceptDeclineDto.getSessionId());

        if (!optionalSession.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("Session not found", false, false), HttpStatus.NOT_FOUND);
        }

        Session session = optionalSession.get();

        if (acceptDeclineDto.getIsAccepted()) {
            session.setStatus(Status.ACCEPTED);
            Session savedSession = sessionRepository.save(session);
            notificationThread(session.getUser().getId(), Status.ACCEPTED.toString(),
                    new SessionDto2(
                            savedSession.getId().toString(),
                            savedSession.getStartDate().getLocalDate().toString(),
                            savedSession.getStartTime().getTime().toString(),
                            savedSession.getEndTime().getTime().toString(),
                            savedSession.getStadium().getName(),
                            false
                    ));

        } else {
            session.setStatus(Status.DECLINED);
            Session savedSession = sessionRepository.save(session);
            notificationThread(session.getUser().getId(), Status.DECLINED.toString(),
                    new SessionDto2(
                            savedSession.getId().toString(),
                            savedSession.getStartDate().getLocalDate().toString(),
                            savedSession.getStartTime().getTime().toString(),
                            savedSession.getEndTime().getTime().toString(),
                            savedSession.getStadium().getName(),
                            false
                    ));
        }

        return new ResponseEntity<>(new ApiResponse("success", true, false), HttpStatus.OK);
    }

    public HttpEntity<?> deleteSession(UUID sessionId) {
        boolean existsById = sessionRepository.existsById(sessionId);

        if (!existsById) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
        }

        sessionRepository.deleteById(sessionId);

        return new ResponseEntity<>(new ApiResponse("success", true), HttpStatus.NO_CONTENT);
    }

    public HttpEntity<?> editSession(SessionDto sessionDto, UUID sessionId) {
        Optional<Session> optionalSession = sessionRepository.findById(sessionId);

        if (!optionalSession.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
        }

        Session editingSession = optionalSession.get();

        editingSession.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        editingSession.setStartDate(
                dateRepository.save(new Date(sessionDto.getStartDate()))
        );

        editingSession.setStartTime(
                timeRepository.save(new Time(sessionDto.getStartTime()))
        );

        editingSession.setEndTime(
                timeRepository.save(new Time(sessionDto.getEndTime()))
        );

        return new ResponseEntity<>(new ApiResponse("edited successfully", true), HttpStatus.ACCEPTED);
    }

    public HttpEntity<?> getSessionsForSpecificDay(UUID stadiumId, LocalDate date) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(stadiumId);
        if (!optionalStadium.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("stadium not found", false, null), HttpStatus.NOT_FOUND);
        }
        Stadium selectedStadium = optionalStadium.get();

        if (!dateRepository.existsByLocalDate(date)) {
            dateRepository.save(new Date(date));
        }
        Optional<Date> optionalDate = dateRepository.findByLocalDate(date);
        Date sessionDate = optionalDate.get();

        List<Session> sessionsForSpecificDay = sessionRepository.getSessionsForSpecificDay(sessionDate.getId(), stadiumId);
        Map<String, Object> sessionTimesMap = new HashMap<>();
        WorkingDay selectedWorkingDay = new WorkingDay();
        for (WorkingDay workingDay : selectedStadium.getWorkingDay()) {
            if (workingDay.getDayName().name().equals(sessionDate.getLocalDate().getDayOfWeek().name())) {
                selectedWorkingDay = workingDay;
            }
        }

        sessionTimesMap.put("workingStartTime", selectedWorkingDay.getStartTime().getTime());
        sessionTimesMap.put("workingEndTime", selectedWorkingDay.getEndTime().getTime());
        ArrayList<Map<String, Object>> timesList = new ArrayList<>();

        for (Session session : sessionsForSpecificDay) {
            Map<String, Object> timeMap = new HashMap<>();
            timeMap.put("startTime", session.getStartTime().getTime());
            timeMap.put("endTime", session.getEndTime().getTime());
            timesList.add(timeMap);
        }
        sessionTimesMap.put("sessionTimes", timesList);

        return ResponseEntity.ok(new ApiResponse("ok", true, sessionTimesMap));
    }

    public ResponseEntity<?> getSessionsOfStadiumsPlayingSoon(UUID userId) {
        List<SessionProjection> sessionStadiumsPlayingSoon = sessionRepository.getSessionsOfStadiumsPlayingSoon(userId);
        return new ResponseEntity<>(new ApiResponse("success", true, sessionStadiumsPlayingSoon), HttpStatus.OK);
    }

    public HttpEntity<?> viewPlayedSessionsOfStadiums(UUID userId) {
        List<SessionProjection> sessions = sessionRepository.findPlayedNotPlayedByCreatedById(userId);
        return new ResponseEntity<>(new ApiResponse("success", true, sessions), HttpStatus.OK);
    }

    public void sendNotification(List<String> tokens, String message, SessionDto2 sessionDto2) {
        String url = "https://fcm.googleapis.com/fcm/send";
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.set("Authorization", notificationHeader);
//        // request body parameters
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("notification",new Data("Food Zone", "accepted"));
//
//        String [] str = new String[]{"f9uJZWJ7RUe7itFQWbsJu4:APA91bExnKu2-tor58d_gP89DwLE0HNDAFSD9peJ6kLUOvcdB8bGT1ADZQrrH_2CRFVxtYesaUzwwQPd322QcCiPVdJk_N_S-ggcVaPBX2UVoMjw4wDq0cItV0oKmXbvdhUXBuESoQoq"};
//
//        map.put("registration_ids", str);
//
//        HttpEntity<?> entity2 = new HttpEntity<>(map, headers);

//        List<String> list = new ArrayList();
//        list.add("f9uJZWJ7RUe7itFQWbsJu4:APA91bExnKu2-tor58d_gP89DwLE0HNDAFSD9peJ6kLUOvcdB8bGT1ADZQrrH_2CRFVxtYesaUzwwQPd322QcCiPVdJk_N_S-ggcVaPBX2UVoMjw4wDq0cItV0oKmXbvdhUXBuESoQoq");

        Data footZone;
        if (sessionDto2 == null) {
            footZone = new Data("FootZone", message);
        } else {
            footZone = new Data("FootZone", message, sessionDto2);
        }
        NotificationForSent notification = new NotificationForSent(footZone, tokens);

        HttpEntity<?> entity = new HttpEntity<>(notification, headers);

        restTemplate.postForEntity(url, entity, Void.class);
    }

    public void notificationThread(UUID userId, String message, SessionDto2 session) {
        List<String> deviceTokens = deviceService.deviceTokensByUserId(userId);
        String sentMessage = "yangi xabar";
        if (message.equals(Status.ACCEPTED.toString())) {
            sentMessage = "So'rovingiz qabul qilindi!";
        } else if (message.equals(Status.DECLINED.toString())) {
            sentMessage = "So'rovingiz qabul qilinmadi.";
        } else if (message.equals(Status.PENDING.toString())) {
            sentMessage = "Sizda yangi so'rov bor!";
        } else if (message.equals(Status.PLAYED.toString())) {
            sentMessage = "Band qilingan maydonda o'yin bo'lib o'tdi !";
        } else if (message.equals(Status.NOT_PLAYED.toString())) {
            sentMessage = "Sizda o'tkazib yuborilgan o'yin";
        }
        String finalSentMessage = sentMessage;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendNotification(deviceTokens, finalSentMessage, session);
            }
        });
        thread.start();
    }

    public HttpEntity<?> hasNotification(User user) {
        if (user == null) {
            return new ResponseEntity<>(new ApiResponse("user not found", false), HttpStatus.NOT_FOUND);
        }

        int roleSize = user.getRoles().size();

        int count;
        if (roleSize == 1) {
            count = sessionRepository.hasNotificationForUser(user.getId());
        } else {
            count = sessionRepository.hasNotificationForAdmin(user.getId());
        }
        return new ResponseEntity<>(new ApiResponse("success", true, count != 0), HttpStatus.OK);
    }
}
