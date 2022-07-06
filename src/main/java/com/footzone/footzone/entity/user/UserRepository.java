package com.footzone.footzone.entity.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<UserProjection> findUserById(UUID id);

    @Query(nativeQuery = true,
            value = "select cast(u.id as varchar) as id,\n" +
                    "       u.full_name           as fullName,\n" +
                    "       u.phone_number        as phoneNumber,\n" +
                    "       a.name                as photoName,\n" +
                    "       u.enabled             as enabled\n" +
                    "from users u\n" +
                    "         join attachments a on u.photo_id = a.id\n" +
                    "where lower(u.full_name) like lower(concat('%', :search, '%'))")
    Page<AllUserProjection> findAllUsersByPage(Pageable page, String search);
}
