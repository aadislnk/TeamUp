package com.teamup.teamup_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "team_skills",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_team_skill",
                        columnNames = {"team_id", "skill_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_team_skill_team",
                        columnList = "team_id"
                ),
                @Index(
                        name = "idx_team_skill_skill",
                        columnList = "skill_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamSkill extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "team_id",
            nullable = false
    )
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "skill_id",
            nullable = false
    )
    private Skill skill;
}
