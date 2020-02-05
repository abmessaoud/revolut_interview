package com.revolut.payments.repositories;

import com.revolut.payments.dto.AccountRequestDTO;
import com.revolut.payments.models.Account;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class AccountRepository {
    private static final List<Account> MEMORY_STORE = new CopyOnWriteArrayList<>();

    public AccountRepository() {}

    public Account fetchOne(final int id) {
        if (id > MEMORY_STORE.size()) {
            return null;
        }
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
        MEMORY_STORE.set(id-1, account);
        return account;
    }

    public Account update(final Account updated) {
        if (updated.getId() > MEMORY_STORE.size()) {
            return null;
        }
        return MEMORY_STORE.set(updated.getId()-1, updated);
    }

    public Account delete(final int id) {
        if (id > MEMORY_STORE.size()) {
            return null;
        }
        final Account current = MEMORY_STORE.get(id-1);
        MEMORY_STORE.set(id-1, new Account.Builder(current).setStatus("deactive").build());
        return current;
    }

    void clearStore() {
        MEMORY_STORE.clear();
    }
}
