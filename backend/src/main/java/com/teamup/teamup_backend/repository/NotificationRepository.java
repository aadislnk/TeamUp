package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Notification;
import com.teamup.teamup_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    long countByUserAndIsReadFalse(User user);

    List<Notification> findByUserAndIsReadFalse(User user);

    Optional<Notification> findByIdAndUser(Long id, User user);

    boolean existsByIdAndUser(Long id, User user);


}