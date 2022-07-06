package com.footzone.footzone.notification;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.footzone.footzone.entity.footballSession.SessionDto2;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// Bahodir Hasanov 6/20/2022 1:23 PM
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    @JsonSerialize
    private String title;
    @JsonSerialize
    private String body;

    @JsonSerialize
    private SessionDto2 session;

    public Data(String title, String body) {
        this.title = title;
        this.body = body;
    }
}
