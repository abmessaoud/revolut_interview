package com.revolut.payments.repositories;

import com.revolut.payments.dto.AccountRequestDTO;
import com.revolut.payments.models.Account;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class AccountRepository {
    private static AccountRepository instance;
    private static final List<Account> MEMORY_STORE = new CopyOnWriteArrayList<>();

    private AccountRepository() {}

    public Account fetchOne(final int id) {
        return MEMORY_STORE.get(id-1);
    }

    public List<Account> fetchMany(final AccountRequestDTO request) {
        final Object statusFilter = request.getFilter("status");
        final Object balanceFilter = request.getFilter("balance");
        return MEMORY_STORE.stream()
                .filter(account -> statusFilter == null || Arrays.asList(statusFilter).contains(account.getStatus()))
                .filter(account -> balanceFilter == null || Arrays.asList(balanceFilter).contains(account.getBalance()))
                .collect(Collectors.toList());
    }

    public Account create(final AccountRequestDTO request) {
        //Adds null to reserve the element index and id.
        MEMORY_STORE.add(null);
        final int id = MEMORY_STORE.size();
        final Account account = new Account.Builder()
                .setId(id)
                .setStatus("active")
                .setBalance(request.getBalance())
                .build();
        MEMORY_STORE.add(id, account);
        return account;
    }

    public Account update(final Account updated) {
        final Account current = MEMORY_STORE.get(updated.getId());
        if (current == null) {
            return null;
        }
        MEMORY_STORE.add(updated.getId(), updated);
        return current;
    }

    public Account delete(final int id) {
        final Account current = MEMORY_STORE.get(id);
        if (current == null) {
            return null;
        }
        MEMORY_STORE.add(id, new Account.Builder(current).setStatus("deactive").build());
        return current;
    }

    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }
}
