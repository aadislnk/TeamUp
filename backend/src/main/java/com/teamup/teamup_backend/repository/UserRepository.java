package com.teamup.teamup_backend.repository;

import com.teamup.teamup_backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

   // Optional<User> findById(Long id); //this is already provided by JpaRepository;

    @Query("""
SELECT DISTINCT us.user
FROM UserSkill us
WHERE us.skill.id = :skillId
""")
    Page<User> findDistinctUsersBySkillId(Long skillId, Pageable pageable);

    @Query("""
SELECT DISTINCT us.user
FROM UserSkill us
WHERE us.skill.id IN :skillIds
""")
    Page<User> findDistinctUsersBySkillIds(List<Long> skillIds, Pageable pageable);
}
