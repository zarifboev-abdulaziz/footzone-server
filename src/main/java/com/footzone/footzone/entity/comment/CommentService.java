package com.footzone.footzone.entity.comment;

import com.footzone.footzone.common.ApiResponse;
import com.footzone.footzone.entity.stadium.Stadium;
import com.footzone.footzone.entity.stadium.StadiumRepository;
import com.footzone.footzone.entity.user.User;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final StadiumRepository stadiumRepository;

    public HttpEntity<?> deleteCommentById(UUID id) {
        List<Comment> commentList = commentRepository.findAll();
        for (Comment comment : commentList) {
            if (comment.getId().equals(id)) {
                commentRepository.deleteById(id);
                return new ResponseEntity<>(new ApiResponse("success", true, true), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
    }

/*    public HttpEntity<?> getCommentById(UUID id) {

        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NO_CONTENT);

        }
        Comment comment = optionalComment.get();

        return new ResponseEntity<>(new ApiResponse("success", true, comment), HttpStatus.OK);


    }*/

    public HttpEntity<?> addComment(CommentDto commentDto, User user) {
        Optional<Stadium> optionalStadium = stadiumRepository.findById(commentDto.getStadiumId());
        if (!optionalStadium.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
        }

        Comment comment = new Comment();
        comment.setRate(commentDto.getRate());
        comment.setText(commentDto.getText());
        Stadium stadium = optionalStadium.get();
        comment.setStadium(stadium);
        comment.setCreatedBy(user);
        commentRepository.save(comment);
        return new ResponseEntity<>(new ApiResponse("success", true, true), HttpStatus.OK);
    }

    public HttpEntity<?> getCommentAll(UUID stadiumId) {
        List<CommentProjection2> commentProjection2s = commentRepository.countAllComments(stadiumId);
        List<CommentProjection> allCommentsByPage = commentRepository.findAllCommentsByPage(stadiumId);
        GeneralStatisticProjection res = new GeneralStatisticProjection(commentProjection2s, allCommentsByPage);
        return ResponseEntity.ok(new ApiResponse("success", true, res));
    }

    public HttpEntity<?> editComment(CommentDto commentDto, UUID id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
        }
        Comment comment = optionalComment.get();
        comment.setRate(commentDto.getRate());
        comment.setText(commentDto.getText());
        Optional<Stadium> optionalStadium = stadiumRepository.findById(commentDto.getStadiumId());
        if (!optionalStadium.isPresent()) {
            return new ResponseEntity<>(new ApiResponse("wrong", false, false), HttpStatus.NOT_FOUND);
        }
        Stadium stadium = optionalStadium.get();
        comment.setStadium(stadium);
        commentRepository.save(comment);
        return new ResponseEntity<>(new ApiResponse("success", true, true), HttpStatus.OK);
    }


}
