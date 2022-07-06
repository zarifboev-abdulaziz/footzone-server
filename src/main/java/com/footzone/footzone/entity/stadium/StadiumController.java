package com.footzone.footzone.entity.stadium;

import com.footzone.footzone.entity.attachment.AttachmentService;
import com.footzone.footzone.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/stadium")
public class StadiumController {

    private final StadiumService stadiumService;

    private final AttachmentService attachmentService;


    @PostMapping
    public ResponseEntity<?> addStadium(
            @RequestPart("stadium") StadiumDto stadiumDto,
            @RequestPart("files") List<MultipartFile> files) {
        return stadiumService.addStadium(stadiumDto, files);
    }

    @DeleteMapping("/edit/photo/delete/{stadiumId}/{photoId}")
    public HttpEntity<?> deleteStadiumPhoto(
            @PathVariable UUID stadiumId,
            @PathVariable UUID photoId) {
        return attachmentService.deleteStadiumPhoto(stadiumId, photoId);
    }

    @PostMapping("/edit/photo/add/{stadiumId}")
    public HttpEntity<?> addPhotoToStadium(
            @RequestPart("file") MultipartFile file,
            @PathVariable UUID stadiumId) {
        return attachmentService.addPhotoToStadium(stadiumId, file);
    }

    @PutMapping("/edit/content/{stadiumId}")
    public HttpEntity<?> editStadiumContent(@PathVariable UUID stadiumId, @RequestBody StadiumDto stadiumDto) {
        return stadiumService.editStadiumContent(stadiumId, stadiumDto);
    }

    @GetMapping("/{stadiumId}")
    public HttpEntity<?> getStadiumByID(@PathVariable UUID stadiumId) {
        return stadiumService.getStadiumById(stadiumId);
    }

    @DeleteMapping("/{stadiumId}")
    public ResponseEntity<?> deleteStadium(@PathVariable UUID stadiumId) {
        return stadiumService.deleteStadium(stadiumId);
    }

    @PostMapping("/viewNearStadiums")
    public HttpEntity<?> viewNearStadiums(@RequestBody UserLongLat userLongLat) {
        return stadiumService.viewNearStadiums(userLongLat);
    }

    @GetMapping("/open")
    public ResponseEntity<?> getOpenStadiums() {
        return stadiumService.getOpenStadiums();
    }

    @GetMapping("/holder/{userId}")
    public HttpEntity<?> viewStadiumsHolderId(@PathVariable UUID userId) {
        return stadiumService.viewStadiumsHolderId(userId);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAllStadiums(
            @RequestParam(defaultValue = "") String search
    ) {
        return stadiumService.getAllStadiums(search);
    }

    @GetMapping("/all/brief/{stadiumId}")
    public HttpEntity<?> getAllStadiumsBrief(@PathVariable UUID stadiumId) {
        return stadiumService.getStadiumByIdBrief(stadiumId);
    }

    @GetMapping("/search")
    public HttpEntity<?> searchStadiums(@RequestParam(defaultValue = "") String search) {
        return stadiumService.searchStadiums(search);
    }

    @GetMapping("/history")
    public HttpEntity<?> getPlayedStadiums(@AuthenticationPrincipal User currentUser) {
        return stadiumService.getPlayedStadiums(currentUser);
    }
}
