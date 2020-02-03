package com.revolut.payments.repositories;

import com.revolut.payments.dto.TransferRequestDTO;
import com.revolut.payments.models.Transfer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TransferRepository {
    private static TransferRepository instance;
    private static final List<Transfer> MEMORY_STORE = new CopyOnWriteArrayList<>();

    private TransferRepository() {}

    public Transfer fetchOne(final int id) {
        return MEMORY_STORE.get(id-1);
    }

    public List<Transfer> fetchMany(final TransferRequestDTO request) {
        final Object sourceFilter = request.getFilter("source");
        final Object statusFilter = request.getFilter("status");
        final Object destinationFilter = request.getFilter("destination");
        final Object transactionFilter = request.getFilter("transaction");
        return MEMORY_STORE.stream()
                .filter(transfer -> sourceFilter == null || Arrays.asList(sourceFilter).contains(transfer.getStatus()))
                .filter(transfer -> statusFilter == null || Arrays.asList(statusFilter).contains(transfer.getStatus()))
                .filter(transfer -> destinationFilter == null || Arrays.asList(destinationFilter).contains(transfer.getStatus()))
                .filter(transfer -> transactionFilter == null || Arrays.asList(transactionFilter).contains(transfer.getStatus()))
                .collect(Collectors.toList());
    }

    public Transfer create(final TransferRequestDTO request) {
        //Adds null to reserve the element index and id.
        MEMORY_STORE.add(null);
        final int id = MEMORY_STORE.size();
        final Transfer transfer = new Transfer.Builder()
                .setId(id)
                .setStatus("successful")
                .setSourceId(request.getSourceId())
                .setTransaction(request.getTransaction())
                .setDestinationId(request.getDestinationId())
                .build();
        MEMORY_STORE.add(id, transfer);
        return transfer;
    }

    public Transfer update(final Transfer updated) {
        final Transfer current = MEMORY_STORE.get(updated.getId());
        if (current == null) {
            return null;
        }
        MEMORY_STORE.add(updated.getId(), updated);
        return current;
    }

    public Transfer delete(final int id) {
        final Transfer current = MEMORY_STORE.get(id);
        if (current == null) {
            return null;
        }
        MEMORY_STORE.add(id, new Transfer.Builder(current).setStatus("rollback").build());
        return current;
    }

    public static TransferRepository getInstance() {
        if (instance == null) {
            instance = new TransferRepository();
        }
        return instance;
    }
}
