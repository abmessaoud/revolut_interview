package com.revolut.payments.repositories;

import com.revolut.payments.models.User;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UserRepository {
    private static UserRepository instance;
    private static final ConcurrentMap<String, User> MEMORY_STORE = new ConcurrentHashMap<>();

    public User fetchOne() {

        return MEMORY_STORE.get("KEY");
    }

    public Set<User> fetchMany() {
        return Collections.singleton(MEMORY_STORE.get("KEY"));
    }

    public User create() {
        final String id = UUID.randomUUID().toString();
        MEMORY_STORE.put("KEY", User.builder().build());
        return MEMORY_STORE.get("KEY");
    }

    public User update() {
        return MEMORY_STORE.get("KEY");
    }

    public User delete() {
        return MEMORY_STORE.get("KEY");
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
}
