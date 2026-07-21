package com.teamup.teamup_backend.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@Table(name = "user_skills")
@Table(
        name = "user_skills",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_skill",columnNames = {"user_id", "skill_id"} )
        },
        indexes = {
                @Index(name = "idx_user_skill_user", columnList = "user_id"),
                @Index(name = "idx_user_skill_skill", columnList = "skill_id")
        }
)
public class UserSkill extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
}
