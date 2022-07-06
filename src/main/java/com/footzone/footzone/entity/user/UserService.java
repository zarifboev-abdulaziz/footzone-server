package com.footzone.footzone.entity.user;

import com.footzone.footzone.common.ApiResponse;
import com.footzone.footzone.entity.stadium.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StadiumRepository stadiumRepository;


    public HttpEntity<?> getAllUsers(int page, int size, String search) {

        Pageable pageable = PageRequest.of(
                page-1,
                size
        );
        Page<AllUserProjection> all = userRepository.findAllUsersByPage(
                pageable,
                search);

        return ResponseEntity.ok(new ApiResponse("success", true, all));
    }

    public HttpEntity<?> editUser(UserEditDto userDTO, UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent())
            return new ResponseEntity<>(new ApiResponse("User not found", false), HttpStatus.NOT_FOUND);

        User user = optionalUser.get();
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        userRepository.save(user);
        return new ResponseEntity<>(new ApiResponse("success", true), HttpStatus.ACCEPTED);
    }

    public HttpEntity<?> getUserById(UUID userId) {
        Optional<UserProjection> optionalUser = userRepository.findUserById(userId);

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("User not found", false), HttpStatus.NOT_FOUND);
        }

        UserProjection user = optionalUser.get();


        return new ResponseEntity<>(new ApiResponse("success", true, user), HttpStatus.OK);
    }


    public HttpEntity<?> blockOrUnblock(BlockUnblockDto blockUnblockDto) {
        Optional<User> optionalUser = userRepository.findById(blockUnblockDto.getUserId());

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("user not found", false), HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        user.setEnabled(!blockUnblockDto.getMessage().equals("Block"));

        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse("success", true), HttpStatus.ACCEPTED);
    }

    @Transactional
    public HttpEntity<?> deleteUserById(UUID userId) {

        try {
            stadiumRepository.deleteByCreatedById(userId);
            userRepository.deleteById(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("something went wrong", false), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(new ApiResponse("success", false), HttpStatus.NO_CONTENT);
    }
}
