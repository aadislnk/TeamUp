package com.teamup.teamup_backend.entity;

import com.teamup.teamup_backend.enums.AcademicYear;
import com.teamup.teamup_backend.enums.Gender;
import com.teamup.teamup_backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

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
    private String about;

    @Column(name = "github_url", length = 255)
    private String githubUrl;

    @Column(name = "linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name = "whatsapp_number", length = 20)
    private String whatsappNumber;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified = false;

}
