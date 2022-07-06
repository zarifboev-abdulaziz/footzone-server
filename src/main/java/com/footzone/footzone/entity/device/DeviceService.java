package com.footzone.footzone.entity.device;

import com.footzone.footzone.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public ResponseEntity<?> saveDevice(Device device) {
        deviceRepository.save(device);
        return new ResponseEntity<>(new ApiResponse("saved", true), HttpStatus.OK);
    }

    public ResponseEntity<?> deleteDevice(UUID deviceId) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if (optionalDevice.isPresent()) {
            deviceRepository.deleteById(deviceId);
            return new ResponseEntity<>(new ApiResponse("device deleted", true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse("device not found", false), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> deviceByUserId(UUID userId) {
        List<DeviceProjection> deviceByUserId = deviceRepository.findDeviceByUserId(userId);
        return new ResponseEntity<>(new ApiResponse("success", true, deviceByUserId), HttpStatus.OK);
    }

    public List<String> deviceTokensByUserId(UUID userId) {
        return deviceRepository.findDeviceTokensByUserId(userId);
    }
}
