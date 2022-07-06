package com.footzone.footzone.entity.device;

import com.footzone.footzone.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/device")
public class DeviceController {
    public final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<?> saveDevice(@RequestBody Device device) {
        device.setUser(new User());
        return deviceService.saveDevice(device);
    }

    @DeleteMapping("{/deviceId}")
    public ResponseEntity<?> deleteDevice(@PathVariable UUID deviceId) {
        return deviceService.deleteDevice(deviceId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> devicesByUserId(@PathVariable UUID userId) {
//        UUID userId = null;
        return deviceService.deviceByUserId(userId);
    }
}
