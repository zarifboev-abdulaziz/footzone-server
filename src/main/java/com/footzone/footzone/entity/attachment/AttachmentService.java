package com.footzone.footzone.entity.attachment;

import com.footzone.footzone.common.ApiResponse;
import com.footzone.footzone.entity.stadium.Stadium;
import com.footzone.footzone.entity.stadium.StadiumRepository;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.footzone.footzone.util.AppConstants.UPLOAD_PATH_STADIUM;
import static com.footzone.footzone.util.AppConstants.UPLOAD_PATH_USER;

@Service
@RequiredArgsConstructor
public class AttachmentService {
    private static final String uploadPathStadium = UPLOAD_PATH_STADIUM;
    private static final String uploadPathUser = UPLOAD_PATH_USER;
    private final AttachmentRepository attachmentRepository;

    private final StadiumRepository stadiumRepository;
    private final UserRepository userRepository;


    public ResponseEntity<?> savePhoto(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();
        while (fileNames.hasNext()) {
            MultipartFile file = request.getFile(fileNames.next());
            if (file != null) {
                Attachment attachment = new Attachment();
                attachment.setSize(file.getSize());
                attachment.setContentType(file.getContentType());
                attachment.setOriginalName(file.getOriginalFilename());
                String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
                String name = UUID.randomUUID() + "." + split[split.length - 1];
                attachment.setName(name);
                attachmentRepository.save(attachment);
                Path path = Paths.get(uploadPathStadium + "/" + name);
                try {
                    Files.copy(file.getInputStream(), path);
                } catch (IOException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(409).body("something error");
                }
            }
        }
        return ResponseEntity.ok("successfully upload");
    }


    public ResponseEntity<?> downloadFile(UUID fileId, HttpServletResponse response) throws IOException {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(fileId);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + attachment.getContentType() + "\"");
            response.setContentType(attachment.getContentType());
            FileInputStream fileInputStream = new FileInputStream(uploadPathStadium + "/" + attachment.getName());
            FileCopyUtils.copy(fileInputStream, response.getOutputStream());
        }
        return ResponseEntity.ok("downloading");
    }

    public ResponseEntity<?> deletePhoto(UUID fileId, String deletePath) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(fileId);
        if (optionalAttachment.isPresent()) {
            Attachment attachment = optionalAttachment.get();
            attachmentRepository.deleteById(fileId);
            if (!attachment.getName().equals("default-profile-pic.jpg")) {
                Path path = Paths.get(deletePath + "/" + attachment.getName());
                try {
                    Files.deleteIfExists(path);
                    return new ResponseEntity<>(new ApiResponse("deleted", true), HttpStatus.OK);
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>(new ApiResponse("done", true), HttpStatus.OK);
            }
        } else {
            ApiResponse apiResponse = new ApiResponse("photo not found", false);
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }


    public Attachment saveAttachment(MultipartFile file) {
        Attachment attachment = new Attachment();
        attachment.setSize(file.getSize());
        attachment.setContentType(file.getContentType());
        attachment.setOriginalName(file.getOriginalFilename());
        String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String name = UUID.randomUUID() + "." + split[split.length - 1];
        attachment.setName(name);
        Attachment saved = attachmentRepository.save(attachment);
        Path path = Paths.get(uploadPathStadium + "/" + name);
        try {
            Files.copy(file.getInputStream(), path);
            return saved;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpEntity<?> changePhoto(MultipartFile file, UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent())
            return new ResponseEntity<>(new ApiResponse("user not found", false), HttpStatus.NOT_FOUND);

        User user = optionalUser.get();
        UUID lastPhoto = null;
        if (user.getPhoto() != null) {
            lastPhoto = user.getPhoto().getId();
        }

        Attachment attachment = new Attachment();
        attachment.setSize(file.getSize());
        attachment.setContentType(file.getContentType());
        attachment.setOriginalName(file.getOriginalFilename());
        String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String name = UUID.randomUUID() + "." + split[split.length - 1];
        attachment.setName(name);

        File convFile = new File(uploadPathUser + "/" + name);

        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("something went wrong", false), HttpStatus.CONFLICT);
        }

        Attachment savedPhoto = attachmentRepository.save(attachment);
        user.setPhoto(savedPhoto);
        userRepository.save(user);

        if (lastPhoto != null) {
            deletePhoto(lastPhoto, UPLOAD_PATH_USER);
        }

        return new ResponseEntity<>(new ApiResponse("saved successfully", true), HttpStatus.CREATED);
    }

    public HttpEntity<?> deleteStadiumPhoto(UUID stadiumId, UUID photoId) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(stadiumId);
        if (!optionalStadium.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("stadium not found", false), HttpStatus.NOT_FOUND);
        }

        Stadium stadium = optionalStadium.get();
        List<Attachment> photos = stadium.getPhotos();
        photos.removeIf(photo -> photo.getId().equals(photoId));
        stadium.setPhotos(photos);
        stadiumRepository.save(stadium);
        return deletePhoto(photoId, UPLOAD_PATH_STADIUM);
    }

    public HttpEntity<?> addPhotoToStadium(UUID stadiumId, MultipartFile file) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(stadiumId);
        if (!optionalStadium.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("stadium not found", false), HttpStatus.NOT_FOUND);
        }

        Stadium stadium = optionalStadium.get();
        List<Attachment> photos = stadium.getPhotos();
        Attachment attachment = saveAttachment(file);
        photos.add(attachment);
        stadium.setPhotos(photos);
        stadiumRepository.save(stadium);
        return new ResponseEntity<>(new ApiResponse("add success", true), HttpStatus.CREATED);
    }
}
