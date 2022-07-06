package com.footzone.footzone.entity.comment;


import com.footzone.footzone.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.PackagePrivate;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("${app.domain}" + "/comment")
@RequiredArgsConstructor
@PackagePrivate
public class CommentController {

    private final CommentService commentService;

/*    @GetMapping("/{id}")
    public HttpEntity<?> getCommentById(@PathVariable UUID id) {
        return commentService.getCommentById(id);

    }*/

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteCommentById(@PathVariable UUID id) {
        return commentService.deleteCommentById(id);

    }


    @GetMapping("/{stadiumId}")
    public HttpEntity<?> getCommentAllByStadiumId(@PathVariable UUID stadiumId) {
        return commentService.getCommentAll(stadiumId);
    }

    @PostMapping()
    public HttpEntity<?> addComment(@RequestBody CommentDto commentDto, @AuthenticationPrincipal User user) {
        return commentService.addComment(commentDto, user);

    }

    @PutMapping("/{id}")
    public HttpEntity<?> editComment(@RequestBody CommentDto commentDto, @PathVariable UUID id) {
        return commentService.editComment(commentDto, id);

    }


}
