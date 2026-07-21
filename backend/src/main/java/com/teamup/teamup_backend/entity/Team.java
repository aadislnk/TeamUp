package com.teamup.teamup_backend.entity;


import com.teamup.teamup_backend.enums.TeamStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "teams",
        indexes = {
                @Index(name = "idx_team_event", columnList = "event_id"),
                @Index(name = "idx_team_leader", columnList = "leader_id"),
                @Index(name = "idx_team_status", columnList = "status")
        }
)
public class Team extends BaseEntity {

    @Column(nullable = false,length = 255)
    private String name;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String description;

    @Column(name = "max_members",nullable = false)
    private Integer maxMembers;

    @Column(name = "current_members", nullable = false)
    @Builder.Default
    private Integer currentMembers = 1;

    @Column(name = "recruitment_open",nullable = false)
    private Boolean recruitmentOpen;

    @Column(name = "whatsapp_group_link",nullable = true,length = 255)
    private String whatsappGroupLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TeamStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id",nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id",nullable = false)
    private User leader;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TeamMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @Builder.Default
    private List<JoinRequest> joinRequests = new ArrayList<>();

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @Builder.Default
    private List<TeamSkill> teamSkills = new ArrayList<>();
}
