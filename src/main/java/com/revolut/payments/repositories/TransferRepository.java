package com.revolut.payments.repositories;

import com.revolut.payments.dto.TransferRequestDTO;
import com.revolut.payments.models.Transfer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TransferRepository {
    private static final List<Transfer> MEMORY_STORE = new CopyOnWriteArrayList<>();

    public TransferRepository() {}

    public Transfer fetchOne(final int id) {
        if (id > MEMORY_STORE.size()) {
            return null;
        }
        return MEMORY_STORE.get(id-1);
    }

    public List<Transfer> fetchMany(final TransferRequestDTO request) {
        final Object sourceFilter = request.getFilter("source");
        final Object statusFilter = request.getFilter("status");
        final Object destinationFilter = request.getFilter("destination");
        final Object transactionFilter = request.getFilter("transaction");
        return MEMORY_STORE.stream()
                .filter(transfer -> sourceFilter == null || Arrays.asList(sourceFilter).contains(transfer.getSourceId()))
                .filter(transfer -> statusFilter == null || Arrays.asList(statusFilter).contains(transfer.getStatus()))
                .filter(transfer -> destinationFilter == null || Arrays.asList(destinationFilter).contains(transfer.getDestinationId()))
                .filter(transfer -> transactionFilter == null || Arrays.asList(transactionFilter).contains(transfer.getTransaction()))
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
        MEMORY_STORE.set(id-1, transfer);
        return transfer;
    }

    public Transfer update(final Transfer updated) {
        if (updated.getId() > MEMORY_STORE.size()) {
            return null;
        }
        return MEMORY_STORE.set(updated.getId()-1, updated);
    }

    public Transfer delete(final int id) {
        if (id > MEMORY_STORE.size()) {
            return null;
        }
        final Transfer current = MEMORY_STORE.get(id-1);
        MEMORY_STORE.set(id-1, new Transfer.Builder(current).setStatus("rollback").build());
        return current;
    }

    void clearStore() {
        MEMORY_STORE.clear();
    }
}
