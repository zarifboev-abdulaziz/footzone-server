package com.footzone.footzone.entity.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    @Query(nativeQuery = true,
            value = "select cast(a.id as varchar) as id,\n" +
                    "       a.original_name       as orginalName,\n" +
                    "       a.size                as size,\n" +
                    "       a.content_type        as contentType,\n" +
                    "       a.name                as name\n" +
                    "from stadiums s\n" +
                    "         join stadiums_photos sp on s.id = sp.stadium_id\n" +
                    "         join attachments a on a.id = sp.photo_id\n" +
                    "where s.id = :stadiumId")
    List<AttachmentProjection> getAllStadiumPhotosNameByStadiumId(UUID stadiumId);
}
