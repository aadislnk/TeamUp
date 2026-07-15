package com.teamup.teamup_backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_skills")
public class UserSkill extends BaseEntity{

    @Column(length = 20)
    private String proficiency;
}
