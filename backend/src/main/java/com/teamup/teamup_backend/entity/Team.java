package com.teamup.teamup_backend.entity;


import com.teamup.teamup_backend.enums.TeamStatus;
import jakarta.persistence.*;
import lombok.*;

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
                @Index(name = "idx_team_leader", columnList = "leader_id")
        }
)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id",nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id",nullable = false)
    private User leader;

    @OneToMany(mappedBy = "team",fetch = FetchType.LAZY)
    private List<TeamMember> members;

    @OneToMany(mappedBy = "team",fetch =  FetchType.LAZY)
    private List<JoinRequest>  joinRequests;
}
