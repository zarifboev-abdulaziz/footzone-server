package com.footzone.footzone.entity.sms;


//Asliddin Kenjaev, created: May, 13 2022 10:38 AM


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${app.domain}" + "/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;
//
//    @GetMapping("/{phoneNumber}")
//    public HttpEntity<?> sendSms(@PathVariable String phoneNumber) {
//        ApiResponse apiResponse = smsService.sendSms(phoneNumber);
//        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
//    }
//
//    @PostMapping
//    public HttpEntity<?> checkCode(@RequestBody SmsDto smsDto) {
//        ApiResponse apiResponse = smsService.checkSmsCode(smsDto);
//        return ResponseEntity.status(apiResponse.isSuccess() ? 202 : 409).body(apiResponse);
//    }

    @GetMapping("/send/forRegister/{phoneNumber}")
    public HttpEntity<?> sendSmsForUserRegistration(@PathVariable String phoneNumber) {
        return smsService.sendSmsForUserRegistration(phoneNumber);
    }

    @GetMapping("/send/forLogin/{phoneNumber}")
    public HttpEntity<?> sendSmsForUserLogin(@PathVariable String phoneNumber) {
        return smsService.sendSmsForUserLogin(phoneNumber);
    }

    @PostMapping("/validate/forRegister")
    public HttpEntity<?> validateSmsForUserRegistration(@Valid @RequestBody SmsDto smsDto) {
        return smsService.validateSmsForUserRegistration(smsDto);
    }


}
