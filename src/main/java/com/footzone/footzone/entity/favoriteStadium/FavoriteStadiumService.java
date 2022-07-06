package com.footzone.footzone.entity.favoriteStadium;


import com.footzone.footzone.common.ApiResponse;
import com.footzone.footzone.entity.stadium.Stadium;
import com.footzone.footzone.entity.stadium.StadiumProjection;
import com.footzone.footzone.entity.stadium.StadiumRepository;
import com.footzone.footzone.entity.user.User;
import com.footzone.footzone.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoriteStadiumService {

    private final FavoriteStadiumRepository favoriteStadiumRepository;
    private final StadiumRepository stadiumRepository;
    private final UserRepository userRepository;


    public HttpEntity<?> addOrRemoveFavoriteStadium(FavoriteStadiumDto favoriteStadiumDto) {

        Optional<Stadium> stadiumOptional = stadiumRepository.findById(favoriteStadiumDto.getStadiumId());
        //todo abdulaziz qiladi userni
        if (!stadiumOptional.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
        }
        Optional<User> optionalUser = userRepository.findById(favoriteStadiumDto.getUserId());

        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("user not found", false), HttpStatus.NOT_FOUND);
        }

        FavoriteStadium favoriteStadium = new FavoriteStadium();
        favoriteStadium.setStadium(stadiumOptional.get());
        favoriteStadium.setCreatedBy(optionalUser.get());
        FavoriteStadium favoriteStadium1 = favoriteStadiumRepository.findByCreatedByIdAndStadiumId(favoriteStadiumDto.getUserId(), favoriteStadiumDto.getStadiumId());
        boolean b = favoriteStadiumRepository.existsByCreatedByIdAndStadiumId(optionalUser.get().getId(), stadiumOptional.get().getId());
        if (b) {
            favoriteStadiumRepository.deleteById(favoriteStadium1.getId());
            return new ResponseEntity<>(new ApiResponse("delete success", true, true), HttpStatus.OK);
        } else {
            favoriteStadiumRepository.save(favoriteStadium);
            return new ResponseEntity<>(new ApiResponse("add success", true, true), HttpStatus.OK);
        }

    }

/*    public HttpEntity<?> getFavoriteStadiumAll(int page, int size,String search) {

        Pageable pageable = PageRequest.of(
                page - 1,
                size
        );
        Page<FavoriteStadiumProjection> all = favoriteStadiumRepository.getFavoriteStadiumByPages(
                pageable,search);

        return ResponseEntity.ok(new ApiResponse("success", true, all));

    }*/


    public HttpEntity<?> getFavouriteStadiumByUserId(UUID userId) {

        List<StadiumProjection> all = favoriteStadiumRepository.findStadiumIdByUserId(userId);
        return ResponseEntity.ok(new ApiResponse("success", true, all));

    }

    public HttpEntity<?> getStadiumIds(UUID userId) {
        List<String> idsByUserId = favoriteStadiumRepository.getIdsByUserId(userId);
        return new ResponseEntity<>(new ApiResponse("success", true, idsByUserId), HttpStatus.OK);
    }
}
