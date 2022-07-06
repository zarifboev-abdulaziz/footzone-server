package com.footzone.footzone.notification;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

// Bahodir Hasanov 6/20/2022 1:25 PM
@NoArgsConstructor
@AllArgsConstructor
@lombok.Data
public class NotificationForSent {
    private Data data;
    List<String> registration_ids;
}
