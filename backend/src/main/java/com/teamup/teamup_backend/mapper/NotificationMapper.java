package com.teamup.teamup_backend.mapper;

import com.teamup.teamup_backend.dto.response.NotificationResponse;
import com.teamup.teamup_backend.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationMapper {

    public NotificationResponse toNotificationResponse(Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public List<NotificationResponse> toNotificationResponseList(
            List<Notification> notifications
    ) {

        return notifications.stream()
                .map(this::toNotificationResponse)
                .toList();
    }

}