package com.teamup.teamup_backend.service.impl;

import com.teamup.teamup_backend.constant.ApiMessages;
import com.teamup.teamup_backend.dto.response.NotificationResponse;
import com.teamup.teamup_backend.dto.response.UnreadCountResponse;
import com.teamup.teamup_backend.entity.Notification;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.enums.NotificationType;
import com.teamup.teamup_backend.exception.ResourceNotFoundException;
import com.teamup.teamup_backend.mapper.NotificationMapper;
import com.teamup.teamup_backend.repository.NotificationRepository;
import com.teamup.teamup_backend.service.CurrentUserService;
import com.teamup.teamup_backend.service.EmailService;
import com.teamup.teamup_backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static com.teamup.teamup_backend.mail.EmailSubjects.TEAM_INVITATION;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final CurrentUserService currentUserService;
    private final EmailService emailService;


    @Override
    public List<NotificationResponse> getMyNotifications() {

        User currentUser = currentUserService.getCurrentUser();

        List<Notification> notifications =
                notificationRepository.findByUserOrderByCreatedAtDesc(currentUser);

        return notificationMapper.toNotificationResponseList(notifications);
    }

    @Override
    public UnreadCountResponse getUnreadCount() {

        User currentUser = currentUserService.getCurrentUser();

        long unreadCount =
                notificationRepository.countByUserAndIsReadFalse(currentUser);

        return UnreadCountResponse.builder()
                .unreadCount(unreadCount)
                .build();
    }

    @Override
    public void markAsRead(Long notificationId) throws AccessDeniedException {

        User currentUser = currentUserService.getCurrentUser();

        Notification notification =
                getNotificationOrThrow(notificationId);

        validateNotificationOwnership(
                notification,
                currentUser
        );

        if (Boolean.TRUE.equals(notification.getIsRead())) {
            return;
        }

        notification.setIsRead(true);

        notificationRepository.save(notification);
    }
    @Override
    public void markAllAsRead() {

        User currentUser = currentUserService.getCurrentUser();

        List<Notification> unreadNotifications =
                notificationRepository.findByUserAndIsReadFalse(currentUser);

        if (unreadNotifications.isEmpty()) {
            return;
        }

        unreadNotifications.forEach(notification ->
                notification.setIsRead(true)
        );

        notificationRepository.saveAll(unreadNotifications);
    }

    @Override
    public void createNotification(
            User recipient,
            String title,
            String message,
            NotificationType type
    ) {

        Notification notification = Notification.builder()
                .user(recipient)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    public void sendNotificationEmail(
            User recipient,
            NotificationType type,
            Object... templateData
    ) {

        switch (type) {

            case JOIN_REQUEST_RECEIVED ->
                    emailService.sendJoinRequestReceivedEmail(
                            recipient.getEmail(),
                            (String) templateData[0], // leaderName
                            (String) templateData[1], // applicantName
                            (String) templateData[2], // teamName
                            (String) templateData[3]  // applicantRole
                    );

            case JOIN_REQUEST_ACCEPTED ->
                    emailService.sendJoinRequestAcceptedEmail(
                            recipient.getEmail(),
                            (String) templateData[0], // userName
                            (String) templateData[1], // teamName
                            (String) templateData[2]  // leaderName
                    );

            case JOIN_REQUEST_REJECTED ->
                    emailService.sendJoinRequestRejectedEmail(
                            recipient.getEmail(),
                            (String) templateData[0], // userName
                            (String) templateData[1], // teamName
                            (String) templateData[2]  // leaderName
                    );

            case TEAM_INVITATION ->
                    emailService.sendTeamInvitationEmail(
                            recipient.getEmail(),
                            (String) templateData[0], // userName
                            (String) templateData[1], // teamName
                            (String) templateData[2], // leaderName
                            (String) templateData[3], // eventName
                            (String) templateData[4]  // teamRole
                    );

            default -> {
                // No email required
            }
        }
    }

   //helpers
    private Notification getNotificationOrThrow(Long notificationId) {

        return notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                ApiMessages.NOTIFICATION_NOT_FOUND
                        )
                );
    }

    private void validateNotificationOwnership(
            Notification notification,
            User currentUser
    ) throws AccessDeniedException {

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(
                    ApiMessages.ACCESS_DENIED
            );
        }

    }

}