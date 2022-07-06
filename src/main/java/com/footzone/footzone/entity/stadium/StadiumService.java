package com.footzone.footzone.entity.stadium;

import com.footzone.footzone.common.ApiResponse;
import com.footzone.footzone.entity.attachment.Attachment;
import com.footzone.footzone.entity.attachment.AttachmentService;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.entity.user.UserRepository;
import com.footzone.footzone.entity.workingDay.WorkingDay;
import com.footzone.footzone.entity.workingDay.WorkingDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StadiumService {

    private final StadiumRepository stadiumRepository;
    private final AttachmentService attachmentService;
    private final WorkingDayService workingDayService;


    private final UserRepository userRepository;

    public ResponseEntity<?> addStadium(
            StadiumDto stadiumDto,
            List<MultipartFile> fields) {

        try {
            List<Attachment> photos = new ArrayList<>();
            for (MultipartFile file : fields) {
                Attachment attachment = attachmentService.saveAttachment(file);
                if (attachment != null) {
                    photos.add(attachment);
                }
            }

            Optional<User> optionalUser = userRepository.findById(stadiumDto.getUserId());

            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>(new ApiResponse("user not found", false), HttpStatus.OK);
            }

            User user = optionalUser.get();

//          Saving stadium
            Stadium stadium = new Stadium(
                    stadiumDto.getName(),
                    stadiumDto.getNumber(),
                    stadiumDto.getHourlyPrice(),
                    stadiumDto.getAddress(),
                    true,
                    photos,
                    false,
                    stadiumDto.getLongitude(),
                    stadiumDto.getLatitude(),
                    null,
                    workingDayService.getWorkingDays(stadiumDto.workingDays)
            );
            stadium.setOwner(user);
            // TODO: 5/31/2022 authentication principles
            stadiumRepository.save(stadium);

            return new ResponseEntity<>("Successfully added!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Conflict!", HttpStatus.CONFLICT);
        }
    }

    public HttpEntity<?> editStadiumContent(UUID stadiumId, StadiumDto stadiumDto) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(stadiumId);

        if (!optionalStadium.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("stadium not found", false), HttpStatus.NOT_FOUND);
        }

        Stadium stadium = optionalStadium.get();
        stadium.setName(stadiumDto.getName());
        stadium.setNumber(stadiumDto.getNumber());
        stadium.setHourlyPrice(stadiumDto.getHourlyPrice());
        stadium.setAddress(stadiumDto.getAddress());
        stadium.setLatitude(stadiumDto.getLatitude());
        stadium.setLongitude(stadiumDto.getLongitude());
        stadium.setWorkingDay(workingDayService.getWorkingDays(stadiumDto.workingDays));

        stadiumRepository.save(stadium);
        return new ResponseEntity<>(new ApiResponse("success", true), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<?> deleteStadium(UUID stadiumId) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(stadiumId);
        if (optionalStadium.isPresent()) {
            optionalStadium.get().setDeleted(true);
            stadiumRepository.save(optionalStadium.get());
            return ResponseEntity.ok(new ApiResponse("success", true));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public HttpEntity<?> viewNearStadiums(UserLongLat userLongLat) {
        List<StadiumProjection> allStadiums = stadiumRepository.findNearStadium(userLongLat.getLatitude(), userLongLat.getLongitude());
        return new ResponseEntity<>(new ApiResponse("success", true, allStadiums), HttpStatus.OK);
    }


    public ResponseEntity<?> getOpenStadiums() {
        List<OpenStadiumProjection> openStadiums = stadiumRepository.getOpenStadiums();
        return ResponseEntity.ok(new ApiResponse("open stadium", true, openStadiums));
    }

    public HttpEntity<?> viewStadiumsHolderId(UUID userId) {
        List<StadiumProjection> allByOwnerId = stadiumRepository.findAllStadiumByOwnerId(userId);
        return new ResponseEntity<>(new ApiResponse("success", true, allByOwnerId), HttpStatus.OK);
    }

    public HttpEntity<?> getStadiumById(UUID stadiumId) {
        Optional<StadiumProjection2> optionalStadium = stadiumRepository.findStadiumById(stadiumId);

        if (!optionalStadium.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("stadium not found", false), HttpStatus.NOT_FOUND);
        }

        StadiumProjection2 stadium = optionalStadium.get();
        return new ResponseEntity<>(new ApiResponse("success", true, stadium), HttpStatus.OK);
    }

    public IsOpen isOpen(UUID stadiumId) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(stadiumId);

        if (!optionalStadium.isPresent()) {
            return null;
        }

        Stadium stadium = optionalStadium.get();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
        WorkingDay sWorkingDay = null;
        for (WorkingDay workingDay : stadium.getWorkingDay()) {
            if (now.getDayOfWeek().toString()
                    .equals(workingDay.getDayName().name())) {
                sWorkingDay = workingDay;
                break;
            }
        }

        if (sWorkingDay != null) {
            LocalTime startTime = sWorkingDay.getStartTime().getTime();
            LocalTime endTime = sWorkingDay.getEndTime().getTime();

            boolean isOpen;

            if (endTime.isBefore(LocalTime.of(23, 59, 59)) &&
                    endTime.isAfter(startTime)) {
                isOpen = startTime.isBefore(now.toLocalTime()) &&
                        endTime.isAfter(now.toLocalTime());
            } else {
                isOpen = startTime.isBefore(now.toLocalTime()) &&
                        endTime.isBefore(now.toLocalTime());
            }

            if (isOpen) {
                return new IsOpen(true, endTime);
            }

            return new IsOpen(false, startTime);
        } else {
            return new IsOpen(false, null);
        }
    }

    public HttpEntity<?> getAllStadiums(String search) {
        List<StadiumProjection3> allStadium = stadiumRepository.getAllStadium(search);
        return new ResponseEntity<>(new ApiResponse("success", true, allStadium), HttpStatus.OK);
    }

    public HttpEntity<?> getStadiumByIdBrief(UUID stadiumId) {
        Optional<StadiumProjection> stadiumByIdBrief = stadiumRepository.findStadiumByIdBrief(stadiumId);
        if (!stadiumByIdBrief.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("stadium not found", false), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ApiResponse("success", true, stadiumByIdBrief.get()), HttpStatus.OK);
    }


    public HttpEntity<?> searchStadiums(String search) {
        List<StadiumProjection> stadiumProjections = stadiumRepository.searchByName(search);
        return new ResponseEntity<>(new ApiResponse("success", true, stadiumProjections), HttpStatus.OK);
    }

    public HttpEntity<?> getPlayedStadiums(User currentUser) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(currentUser.getPhoneNumber());
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("user not found", false), HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        List<StadiumProjection> playedStadiums = stadiumRepository.getPlayedStadiums(user.getId());

        return new ResponseEntity<>(new ApiResponse("success", true, playedStadiums), HttpStatus.OK);
    }
}

