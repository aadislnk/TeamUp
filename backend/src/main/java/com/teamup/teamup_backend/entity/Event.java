package com.teamup.teamup_backend.entity;

import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "events")
public class Event extends BaseEntity {

    @Column(nullable = false,length = 255)
    private String title;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false,length = 255)
    private String organizer;

    @Column(nullable = true,length = 255)
    private String location;

    @Column(name = "event_url",nullable = true,length = 255)
    private String eventUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventMode mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Column(name = "registration_deadline",nullable = false)
    private LocalDateTime registrationDeadline;
    @Column(name = "event_date",nullable = false)
    private LocalDateTime eventDate;
}
