package com.teamup.teamup_backend.specification;

import com.teamup.teamup_backend.entity.Event;
import com.teamup.teamup_backend.enums.EventMode;
import com.teamup.teamup_backend.enums.EventStatus;
import com.teamup.teamup_backend.enums.EventType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class EventSpecifications {

    private EventSpecifications() {
    }

    public static Specification<Event> search(String keyword) {

        return (root, query, cb) -> {

            if (keyword == null || keyword.isBlank()) {
                return cb.conjunction();
            }

            String search = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("title")), search),
                    cb.like(cb.lower(root.get("description")), search),
                    cb.like(cb.lower(root.get("organizer")), search),
                    cb.like(cb.lower(root.get("location")), search)
            );
        };
    }

    public static Specification<Event> hasType(EventType type) {

        return (root, query, cb) ->
                type == null
                        ? cb.conjunction()
                        : cb.equal(root.get("type"), type);
    }

    public static Specification<Event> hasMode(EventMode mode) {

        return (root, query, cb) ->
                mode == null
                        ? cb.conjunction()
                        : cb.equal(root.get("mode"), mode);
    }

    public static Specification<Event> hasStatus(EventStatus status) {

        return (root, query, cb) ->
                status == null
                        ? cb.conjunction()
                        : cb.equal(root.get("status"), status);
    }

    public static Specification<Event> hasRegistrationOpen(Boolean registrationOpen) {

        return (root, query, cb) ->
                registrationOpen == null
                        ? cb.conjunction()
                        : cb.equal(root.get("registrationOpen"), registrationOpen);
    }

    public static Specification<Event> upcoming() {

        return (root, query, cb) -> cb.greaterThanOrEqualTo(
                root.get("eventDate"),
                LocalDateTime.now()
        );
    }

}