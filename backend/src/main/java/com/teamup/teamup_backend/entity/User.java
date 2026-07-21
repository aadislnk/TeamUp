package com.teamup.teamup_backend.entity;

import com.teamup.teamup_backend.enums.AcademicYear;
import com.teamup.teamup_backend.enums.Gender;
import com.teamup.teamup_backend.enums.PreferredRole;
import com.teamup.teamup_backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Column(nullable = false,length = 100)
    private String fullName;

    @Column(nullable = false,unique = true,length = 255)
    private String email;

    @Column(nullable = false,length = 255)
    private String password;

    @Column(nullable = false,length = 255)
    private String college;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicYear academicYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = true,columnDefinition = "TEXT")
    private String bio;

    @Column(name = "github_url", length = 500)
    private String githubUrl;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Column(name = "whatsapp_number", length = 20)
    private String whatsappNumber;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_role")
    private PreferredRole preferredRole;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<EmailVerificationToken> emailVerificationTokens;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "leader",fetch = FetchType.LAZY)
    private List<Team> teams;

    @OneToMany(mappedBy = "user",fetch =  FetchType.LAZY)
    private List<UserSkill> skills;

    @OneToMany(mappedBy = "user",fetch =  FetchType.LAZY)
    private List<TeamMember> teamMembers;

    @OneToMany(mappedBy = "user",fetch =  FetchType.LAZY)
    private List<JoinRequest>  joinRequests;

}
