package com.footzone.footzone.entity.sms;


//Asliddin Kenjaev, created: May, 13 2022 10:48 AM 

import com.footzone.footzone.common.ApiResponse;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.entity.user.UserRepository;
import com.messagebird.MessageBirdClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.footzone.footzone.util.encryption.Symmetric.*;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsCodeRepository smsCodeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final MessageBirdClient messageBirdClient;

    public Integer sendSms(String phoneNumber) {

//      creating code
        int code = (int) ((Math.random() * (999999 - 100000)) + 100000);
        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(50);


//        sending sms VIA MESSAGEBIRD
//        MessageBirdService messageBirdService = new MessageBirdServiceImpl(MESSAGE_BIRD_API_KEY);
//        MessageBirdClient messageBirdClient = new MessageBirdClient(messageBirdService);

//        List<BigInteger> phones = new ArrayList<>();
//        phones.add(new BigInteger(phoneNumber));

//        try {
//            MessageResponse response = messageBirdClient.sendMessage("FootZone", "Your code is: " + code, phones);
//        } catch (UnauthorizedException | GeneralException ex) {
//            ex.printStackTrace();
//            return null;
//        }

        //TWILIO
//        Twilio.init(ACCOUNT_SID_TWILIO, AUTH_TOKEN_TWILIO);
//        Message message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber("+14094027971"), "FootZone\nYour code is: " + code).create();
//
//        System.out.println(message.getSid());

//      saving code to db
        Optional<SmsCode> optionalByPhoneNumber = smsCodeRepository.findByPhoneNumber(phoneNumber);
        if (optionalByPhoneNumber.isPresent()) {
            SmsCode smsCode = optionalByPhoneNumber.get();
            smsCode.setCode(code);
            smsCode.setExpirationDate(expirationDate);
            smsCodeRepository.save(smsCode);
        } else {
            smsCodeRepository.save(
                    new SmsCode(
                            phoneNumber,
                            code,
                            expirationDate
                    )
            );
        }
        return code;
    }

    public ApiResponse validateSmsCode(String phoneNumber, String code) {
        Optional<SmsCode> optionalByPhoneNumber = smsCodeRepository.findByPhoneNumber(phoneNumber);

        if (!optionalByPhoneNumber.isPresent()) return new ApiResponse("There is no code with this number", false);

        SmsCode smsCode = optionalByPhoneNumber.get();

        if (smsCode.getExpirationDate().isBefore(LocalDateTime.now()))
            return new ApiResponse("This code is invalid", false);

        if (code.equals(smsCode.getCode()))
            return new ApiResponse("Wrong code", false);
        return new ApiResponse("Success", true);
    }

    public HttpEntity<?> sendSmsForUserRegistration(String phoneNumber) {
        phoneNumber = decrypt(phoneNumber);
//        if (phoneNumber.length() != 13)
//            return ResponseEntity.status(401).body(new ApiResponse("Invalid phone number structure", false));

        boolean phoneNumberExists = userRepository.existsByPhoneNumber(phoneNumber);
        if (phoneNumberExists)
            return ResponseEntity.status(400).body(new ApiResponse("This phone number already exists", false));

        Integer smsCode = sendSms(phoneNumber);
        if (smsCode == null)
            return ResponseEntity.status(409).body(new ApiResponse("Something went wrong !!!, Please try again", false));
        return ResponseEntity.status(200).body(new ApiResponse("SMS successfully sent", true, encrypt(smsCode.toString())));
    }


    public HttpEntity<?> sendSmsForUserLogin(String phoneNumber) {
        phoneNumber = decrypt(phoneNumber);
//        if (phoneNumber.length() != 13)
//            return ResponseEntity.status(401).body(new ApiResponse("Invalid phone number structure", false));

        boolean phoneNumberExists = userRepository.existsByPhoneNumber(phoneNumber);
        if (!phoneNumberExists)
            return ResponseEntity.status(404).body(new ApiResponse("User with this phone number does not exist", false));

        Integer smsCode = sendSms(phoneNumber);
        if (smsCode == null)
            return ResponseEntity.status(409).body(new ApiResponse("Something went wrong !!!, Please try again", false));

        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if (optionalUser.isPresent()) {
            optionalUser.get().setSmsCode(passwordEncoder.encode(smsCode.toString()));
            userRepository.save(optionalUser.get());
        }
        return ResponseEntity.status(200).body(new ApiResponse("SMS successfully sent", true, encrypt(smsCode.toString())));
    }

    public HttpEntity<?> validateSmsForUserRegistration(SmsDto smsDto) {
        smsDto.setPhoneNumber(decrypt(smsDto.getPhoneNumber()));
        String codeSent = decrypt(smsDto.getCodeSent().toString());
        ApiResponse apiResponse = validateSmsCode(smsDto.getPhoneNumber(), codeSent);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

}
