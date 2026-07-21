package com.teamup.teamup_backend.controller.notification;

import com.teamup.teamup_backend.dto.common.ApiResponse;
import com.teamup.teamup_backend.dto.response.NotificationResponse;
import com.teamup.teamup_backend.dto.response.UnreadCountResponse;
import com.teamup.teamup_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static com.teamup.teamup_backend.constant.ApiMessages.*;
import static com.teamup.teamup_backend.constant.ApiPaths.*;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(USERS_ME_NOTIFICATIONS)
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications() {

        List<NotificationResponse> notifications =
                notificationService.getMyNotifications();

        return ResponseEntity.ok(
                ApiResponse.success(
                        GET_NOTIFICATIONS_SUCCESS,
                        notifications
                )
        );
    }

    @GetMapping(USERS_ME_NOTIFICATIONS_UNREAD_COUNT)
    public ResponseEntity<ApiResponse<UnreadCountResponse>> getUnreadCount() {

        UnreadCountResponse unreadCount =
                notificationService.getUnreadCount();

        return ResponseEntity.ok(
                ApiResponse.success(
                        GET_UNREAD_NOTIFICATION_COUNT_SUCCESS,
                        unreadCount
                )
        );
    }

    @PatchMapping(NOTIFICATIONS_READ)
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @PathVariable Long id
    ) throws AccessDeniedException {

        notificationService.markAsRead(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        MARK_NOTIFICATION_READ_SUCCESS,
                        null
                )
        );
    }

    @PatchMapping(NOTIFICATIONS_READ_ALL)
    public ResponseEntity<ApiResponse<Void>> markAllAsRead() {

        notificationService.markAllAsRead();

        return ResponseEntity.ok(
                ApiResponse.success(
                        MARK_ALL_NOTIFICATIONS_READ_SUCCESS,
                        null
                )
        );
    }

}