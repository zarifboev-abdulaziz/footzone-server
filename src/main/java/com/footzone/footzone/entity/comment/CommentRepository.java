package com.footzone.footzone.entity.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {


    @Query(nativeQuery = true,
            value = "select cast(c.id as varchar)         as commentId,\n" +
                    "       c.rate                        as rate,\n" +
                    "       c.text                        as text,\n" +
                    "       cast(c.stadium_id as varchar) as stadiumId,\n" +
                    "       u.full_name                   as userFullName,\n" +
                    "       c.created_at                  as createdAt,\n" +
                    "       cast(a.name as varchar)       as userAttachmentName\n" +
                    "from comments c\n" +
                    "         join stadiums s on s.id = c.stadium_id\n" +
                    "         join users u on c.created_by_id = u.id\n" +
                    "         join attachments a on u.photo_id = a.id\n" +
                    "where s.id = :stadiumId")
    List<CommentProjection> findAllCommentsByPage(UUID stadiumId);


    @Query(nativeQuery = true,
            value = "select cast(c.id as varchar) as commentId,\n" +
                    "    c.rate  as rate,\n" +
                    "       c.text as text,\n" +
                    "       cast( c.stadium_id as varchar) as stadiumId\n" +
                    "from comments c\n" +
                    "join stadiums s on s.id = c.stadium_id\n" +
                    "where lower(c.text) like lower(concat('%',:search,'%'))")
    Page<CommentProjection> findAllCommentsByPage(Pageable pageable, String search);

    @Query(
            value = "select count(c.stadium.id) as number,\n" +
                    "       c.rate              as rate\n" +
                    "from comments c\n" +
                    "where c.stadium.id = :stadiumId\n" +
                    "group by c.rate")
    List<CommentProjection2> countAllComments(UUID stadiumId);

}
