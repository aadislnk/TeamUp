package com.teamup.teamup_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "skills")
public class Skill extends BaseEntity{

    @Column(nullable = false,unique = true,length = 100)
    String name;

    @OneToMany(mappedBy = "skill",fetch = FetchType.LAZY)
    private List<UserSkill> skills;
}
