package com.teamup.teamup_backend.entity;

import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "events",
        indexes = {
                @Index(name = "idx_event_status", columnList = "status"),
                @Index(name = "idx_event_type", columnList = "type"),
                @Index(name = "idx_event_mode", columnList = "mode"),
                @Index(name = "idx_event_date", columnList = "event_start"),
                @Index(name = "idx_event_owner", columnList = "owner_id")
        }
)
public class Event extends BaseEntity {

    @Column(nullable = false,length = 255)
    private String title;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false,length = 255)
    private String organizer;

    @Column(nullable = true,length = 255)
    private String location;

    @Column(name = "max_team_size", nullable = false)
    private Integer maxTeamSize;

    @Column(name = "min_team_size", nullable = false)
    private Integer minTeamSize;

    @Column(name = "registration_open", nullable = false)
    private Boolean registrationOpen;

    @Column(name = "event_url",nullable = true,length = 255)
    private String eventUrl;

    @Column(name = "registration_url", length = 500)
    private String registrationUrl;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventMode mode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EventType type;


    @Column(name = "registration_start",nullable = false)
    private LocalDateTime registrationStart;

    @Column(name = "registration_end",nullable = false)
    private LocalDateTime registrationEnd;

    @Column(name = "event_start",nullable = false)
    private LocalDateTime eventStart;

    @Column(name = "event_end",nullable = false)
    private LocalDateTime eventEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "event",fetch = FetchType.LAZY)
    private List<Team> teams;
}
