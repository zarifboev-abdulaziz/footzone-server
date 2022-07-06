package com.footzone.footzone.entity.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.footzone.footzone.util.AppConstants.UPLOAD_PATH_STADIUM;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}" + "/attachment")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<?> savePhoto(MultipartHttpServletRequest request) {
        return attachmentService.savePhoto(request);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<?> downloadPhoto(@PathVariable UUID fileId, HttpServletResponse response) throws IOException {
        return attachmentService.downloadFile(fileId, response);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deletePhoto(@PathVariable UUID fileId) {
        return attachmentService.deletePhoto(fileId, UPLOAD_PATH_STADIUM);
    }


}
