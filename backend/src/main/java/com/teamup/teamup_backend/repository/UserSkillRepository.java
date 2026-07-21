package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.Skill;
import com.teamup.teamup_backend.entity.User;
import com.teamup.teamup_backend.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {

    List<UserSkill> findByUser(User user);

    List<UserSkill> findBySkill(Skill skill);

    boolean existsByUserAndSkill(User user, Skill skill);

    void deleteByUserAndSkill(User user, Skill skill);

}