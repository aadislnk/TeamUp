package com.teamup.teamup_backend.service;

import com.teamup.teamup_backend.dto.response.NotificationResponse;
import com.teamup.teamup_backend.dto.response.UnreadCountResponse;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.NotificationType;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getMyNotifications();

    UnreadCountResponse getUnreadCount();

    void markAsRead(Long notificationId) throws AccessDeniedException;

    void markAllAsRead();

    void createNotification(
            User recipient,
            String title,
            String message,
            NotificationType type
    );

    void sendNotificationEmail(
            User recipient,
            NotificationType type,
            Object... templateData
    );

}