package com.footzone.footzone.authService;

import com.footzone.footzone.common.ApiResponse;
import com.footzone.footzone.entity.attachment.Attachment;
import com.footzone.footzone.entity.device.Device;
import com.footzone.footzone.entity.device.DeviceRepository;
import com.footzone.footzone.entity.role.Role;
import com.footzone.footzone.entity.role.RoleRepository;
import com.footzone.footzone.entity.sms.SmsService;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.entity.user.UserRepository;
import com.footzone.footzone.security.JwtProvider;
import com.footzone.footzone.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.footzone.footzone.util.encryption.Symmetric.*;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final SmsService smsService;
    private final DeviceRepository deviceRepository;


    public HttpEntity<?> registerUser(RegisterDto registerDto) {
        registerDto.setPhoneNumber(decrypt(registerDto.getPhoneNumber()));
        registerDto.setSmsCode(decrypt(registerDto.getSmsCode()));

        if (userRepository.existsByPhoneNumber(registerDto.getPhoneNumber()))
            return ResponseEntity.status(409).body(new ApiResponse("This user registered before", false));

        Set<Role> roleUser = new HashSet<>();

        Role userRole = roleRepository.findByName(AppConstants.USER).get();
        roleUser.add(userRole);

        if (registerDto.isStadiumHolder()) {
            Role holderRole = roleRepository.findByName(AppConstants.STADIUM_HOLDER).get();
            roleUser.add(holderRole);
        }

        User user = new User(
                registerDto.getFullName(),
                registerDto.getPhoneNumber(),
                passwordEncoder.encode(registerDto.getSmsCode()),
                roleUser,
                true,
                new Attachment(
                        "default-profile-pic.jpg",
                        241,
                        "jpg",
                        "default-profile-pic.jpg"
                )
        );

        user.setLanguage(registerDto.getLanguage());
        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("this user already exists", false), HttpStatus.CONFLICT);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                savedUser.getPhoneNumber(), registerDto.getSmsCode());
        authenticationManager.authenticate(authenticationToken);

        String token = jwtProvider.generateToken(user.getPhoneNumber(), user.getRoles());

        Device userDevice = new Device(registerDto.getDeviceName(), registerDto.getDeviceType(), registerDto.getDeviceToken(), savedUser);
        deviceRepository.save(userDevice);

        Map<String, Object> registerMap = new HashMap<>();
        registerMap.put("token", token);
        registerMap.put("user_id", savedUser.getId().toString());
        return ResponseEntity.status(200).body(new ApiResponse("Registered Successfully", true, registerMap));
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new UsernameNotFoundException(phoneNumber));
    }

    public HttpEntity<?> loginUser(@Valid LoginDto loginDto) {
        loginDto.setPhoneNumber(decrypt(loginDto.getPhoneNumber()));
        loginDto.setCodeSent(decrypt(loginDto.getCodeSent()));
        System.out.println(loginDto.getPhoneNumber());
        System.out.println(loginDto.getCodeSent());

        ApiResponse apiResponse = smsService.validateSmsCode(loginDto.getPhoneNumber(), loginDto.getCodeSent());
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(404).body(apiResponse);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getPhoneNumber(), loginDto.getCodeSent());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        User user = (User) authenticate.getPrincipal();
        String token = jwtProvider.generateToken(user.getPhoneNumber(), user.getRoles());

        checkUserDevice(user, loginDto.getDeviceName(), loginDto.getDeviceType(), loginDto.getDeviceToken());
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put("token", token);
        loginMap.put("user_id", user.getId().toString());
        loginMap.put("stadiumHolder", user.getRoles().size() > 1);

        return ResponseEntity.status(200).body(new ApiResponse("Successful signed in.", true, loginMap));
    }

    private void checkUserDevice(User user, String deviceName, String deviceType, String deviceToken) {
        boolean deviceExists = deviceRepository.existsByDeviceTokenAndUserId(deviceToken, user.getId());
        if (deviceExists) return;
        Device userDevice = new Device(deviceName, deviceType, deviceToken, user);
        deviceRepository.save(userDevice);
    }

}
