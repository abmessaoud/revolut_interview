package com.revolut.payments.repositories;

import com.revolut.payments.models.Account;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccountRepository {
    private static AccountRepository instance;
    private static final ConcurrentMap<String, Account> MEMORY_STORE = new ConcurrentHashMap<>();

    private AccountRepository() {}

    public Account fetchOne() {

        return MEMORY_STORE.get("KEY");
    }

    public Set<Account> fetchMany() {
        return Collections.singleton(MEMORY_STORE.get("KEY"));
    }

    public Account create() {
        final String id = UUID.randomUUID().toString();
        MEMORY_STORE.put("KEY", Account.builder().build());
        return MEMORY_STORE.get("KEY");
    }

    public Account update() {
        return MEMORY_STORE.get("KEY");
    }

    public Account delete() {
        return MEMORY_STORE.get("KEY");
    }

    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }
}
