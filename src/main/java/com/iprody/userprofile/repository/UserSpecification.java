package com.iprody.userprofile.repository;

import com.iprody.userprofile.domain.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    private static final List<Specification<User>> filters = new ArrayList<>();

    public UserSpecification firstNameContains(String value) {
        if (value != null) {
            filters.add((root, query, cb) -> cb.like(cb.lower(root.get("firstName")),
                    "%" + cb.literal(value.toLowerCase()) + "%"));
        }
        return this;
    }

    public UserSpecification lastNameContains(String value) {
        if (value != null) {
            filters.add((root, query, cb) -> cb.like(cb.lower(root.get("lastName")),
                    "%" + cb.literal(value.toLowerCase()) + "%"));
        }
        return this;
    }

    public UserSpecification emailContains(String value) {
        if (value != null) {
            filters.add((root, query, cb) -> cb.like(cb.lower(root.get("email")),
                    "%" + cb.literal(value.toLowerCase()) + "%"));
        }
        return this;
    }

    public UserSpecification mobilePhoneContains(String value) {
        if (value != null) {
            filters.add((root, query, cb) -> cb.like(cb.lower(root.get("mobilePhone")),
                    "%" + cb.literal(value.toLowerCase()) + "%"));
        }
        return this;
    }

    public UserSpecification telegramIdContains(String value) {
        if (value != null) {
            filters.add((root, query, cb) -> cb.like(cb.lower(root.get("telegramId")),
                    "%" + cb.literal(value.toLowerCase()) + "%"));
        }
        return this;
    }

    public Specification<User> getFilters() {
        if (filters.isEmpty()) {
            throw new IllegalStateException("Unknown filter");
        }
        return Specification.anyOf(filters);
    }
}