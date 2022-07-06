package com.footzone.footzone.entity.user;


//Asliddin Kenjaev, created: May, 28 2022 11:11 AM 

import com.footzone.footzone.entity.attachment.AttachmentService;
import com.footzone.footzone.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("${app.domain}" + "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AttachmentService attachmentService;

    @GetMapping()
    public HttpEntity<?> getAllUsers(
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "search", defaultValue = "") String search
    ) {
        return userService.getAllUsers(
                page,
                size,
                search
        );

    }

    @GetMapping("/{userId}")
    public HttpEntity<?> getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    @PostMapping("/blockOrUnBlock")
    public HttpEntity<?> blockUser(@RequestBody BlockUnblockDto blockUnblockDto) {
        return userService.blockOrUnblock(blockUnblockDto);
    }

    @PutMapping("/edit/{userId}")
    public HttpEntity<?> editUser(@RequestBody UserEditDto userEditDto, @PathVariable UUID userId) {
        return userService.editUser(userEditDto, userId);
    }

    @PostMapping("/changeProfilePicture/{userId}")
    public HttpEntity<?> changePhoto(@RequestParam("file") MultipartFile file, @PathVariable UUID userId) {
        return attachmentService.changePhoto(file, userId);
    }

    @DeleteMapping("/{userId}")
    public HttpEntity<?> deleteUserById(@PathVariable UUID userId) {
        return userService.deleteUserById(userId);
    }

}

