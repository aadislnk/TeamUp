package com.teamup.teamup_backend.specification;

import com.teamup.teamup_backend.entity.Team;
import com.teamup.teamup_backend.entity.TeamSkill;
import com.teamup.teamup_backend.enums.TeamStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class TeamSpecification {

    private TeamSpecification() {
    }

    public static Specification<Team> hasKeyword(String keyword) {

        return (root, query, criteriaBuilder) -> {

            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String pattern = "%" + keyword.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("description")),
                            pattern
                    )
            );
        };
    }

    public static Specification<Team> hasEvent(Long eventId) {

        return (root, query, criteriaBuilder) -> {

            if (eventId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("event").get("id"),
                    eventId
            );
        };
    }

    public static Specification<Team> hasStatus(TeamStatus status) {

        return (root, query, criteriaBuilder) -> {

            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("status"),
                    status
            );
        };
    }

    public static Specification<Team> hasRecruitment(Boolean recruitmentOpen) {

        return (root, query, criteriaBuilder) -> {

            if (recruitmentOpen == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("recruitmentOpen"),
                    recruitmentOpen
            );
        };
    }

    public static Specification<Team> hasRequiredSkill(Long skillId) {

        return (root, query, criteriaBuilder) -> {

            if (skillId == null) {
                return criteriaBuilder.conjunction();
            }

            query.distinct(true);

            Join<Team, TeamSkill> teamSkillJoin =
                    root.join("teamSkills");

            return criteriaBuilder.equal(
                    teamSkillJoin.get("skill").get("id"),
                    skillId
            );
        };
    }
}
