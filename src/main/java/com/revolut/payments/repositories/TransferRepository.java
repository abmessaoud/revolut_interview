package com.revolut.payments.repositories;

import com.revolut.payments.models.Transfer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TransferRepository {
    private static TransferRepository instance;
    private static final ConcurrentMap<String, Transfer> MEMORY_STORE = new ConcurrentHashMap<>();

    private TransferRepository() {}

    public static TransferRepository getInstance() {
        if (instance == null) {
            instance = new TransferRepository();
        }
        return instance;
    }
}
