package com.teamup.teamup_backend.entity;

import com.teamup.teamup_backend.enums.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "join_requests",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_join_request",
                        columnNames = {"user_id", "team_id"}
                )
        }
)
public class JoinRequest extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JoinRequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id",nullable = false)
    private Team team;
}
