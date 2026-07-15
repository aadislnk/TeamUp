package com.teamup.teamup_backend.entity;


import com.teamup.teamup_backend.enums.TeamStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "teams")
public class Team extends BaseEntity {

    @Column(nullable = false,length = 255)
    private String name;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String description;

    @Column(name = "max_members",nullable = false)
    private Integer maxMembers;

    @Column(name = "recruitment_open",nullable = false)
    private Boolean recruitmentOpen;

    @Column(name = "whatsapp_group_link",nullable = true,length = 255)
    private String whatsappGroupLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamStatus status;
}
