package com.footzone.footzone.entity.sms;
//Asliddin Kenjaev, created: May, 13 2022 12:04 PM

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    Optional<SmsCode> findByPhoneNumber(String phoneNumber);
}
