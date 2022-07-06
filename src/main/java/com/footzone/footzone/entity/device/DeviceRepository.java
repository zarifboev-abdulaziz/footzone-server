package com.footzone.footzone.entity.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    List<DeviceProjection> findDeviceByUserId(UUID user_id);

    boolean existsByDeviceTokenAndUserId(String deviceToken, UUID user_id);

    @Query(nativeQuery = true,
            value = "select distinct devices.device_token\n" +
                    "from devices\n" +
                    "where devices.user_id = :user_id")
    List<String> findDeviceTokensByUserId(UUID user_id);
}
