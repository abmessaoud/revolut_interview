package com.revolut.payments.repositories;

import com.revolut.payments.models.Account;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AccountRepository {
    private static AccountRepository instance;
    private static final ConcurrentMap<String, Account> MEMORY_STORE = new ConcurrentHashMap<>();

    private AccountRepository() {}

    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }
}
