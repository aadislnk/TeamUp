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
@Table(name = "join_requests")
public class JoinRequest extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JoinRequestStatus status;
}
