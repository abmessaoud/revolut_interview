package com.revolut.payments.repositories;

import com.revolut.payments.models.Transfer;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TransferRepository {
    private static TransferRepository instance;
    private static final ConcurrentMap<String, Transfer> MEMORY_STORE = new ConcurrentHashMap<>();

    private TransferRepository() {}

    public Transfer fetchOne() {

        return MEMORY_STORE.get("KEY");
    }

    public Set<Transfer> fetchMany() {
        return Collections.singleton(MEMORY_STORE.get("KEY"));
    }

    public Transfer create() {
        final String id = UUID.randomUUID().toString();
        MEMORY_STORE.put("KEY", Transfer.builder().build());
        return MEMORY_STORE.get("KEY");
    }

    public Transfer update() {
        return MEMORY_STORE.get("KEY");
    }

    public Transfer delete() {
        return MEMORY_STORE.get("KEY");
    }

    public static TransferRepository getInstance() {
        if (instance == null) {
            instance = new TransferRepository();
        }
        return instance;
    }
}
