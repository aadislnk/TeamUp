package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification,Long> {
}
