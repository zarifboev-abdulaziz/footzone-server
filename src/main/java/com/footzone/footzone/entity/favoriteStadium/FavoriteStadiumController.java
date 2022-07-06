package com.footzone.footzone.entity.favoriteStadium;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;

@RequestMapping("${app.domain}" + "/favorites")
@RestController
@RequiredArgsConstructor
@Transactional
public class FavoriteStadiumController {
    private final FavoriteStadiumService favoriteStadiumService;

    @PostMapping()
    public HttpEntity<?> addOrRemoveFavoriteStadium(@RequestBody FavoriteStadiumDto favoriteStadiumDto) {
        return favoriteStadiumService.addOrRemoveFavoriteStadium(favoriteStadiumDto);

    }






/*    @GetMapping()
    public HttpEntity<?> getFavoriteStadiumAll(
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "search", defaultValue = "") String search
    ) {
        return favoriteStadiumService.getFavoriteStadiumAll(
                page,
                size,
                search
        );

    }*/


    @GetMapping("/{userId}")
    public HttpEntity<?> getFavouriteStadiumByUserId(@PathVariable UUID userId) {
        return favoriteStadiumService.getFavouriteStadiumByUserId(userId);

    }


    @GetMapping("/list/{userId}")
    public HttpEntity<?> getStadiumIds(@PathVariable UUID userId) {
        return favoriteStadiumService.getStadiumIds(userId);
    }

}
