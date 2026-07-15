package com.teamup.teamup_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "team_members")
public class TeamMember extends BaseEntity{

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
}
