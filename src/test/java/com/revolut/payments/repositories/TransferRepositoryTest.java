package com.revolut.payments.repositories;

import com.revolut.payments.dto.TransferRequestDTO;
import com.revolut.payments.models.Transfer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TransferRepositoryTest {
    private TransferRepository repository;

    @Before
    public void setUp() {
        repository = new TransferRepository();
        createManyMocks();
    }

    @After
    public void tearDown() {
        repository.clearStore();
    }

    @Test
    public void testFetchMany_whenRequestHasNoFilters_thenReturnsAllTransfers() {
        final TransferRequestDTO request = new TransferRequestDTO.Builder().build();

        final List<Transfer> response = repository.fetchMany(request);

        Assert.assertEquals(15, response.size());
    }

    @Test
    public void testFetchMany_whenRequestHasStatusFilter_thenReturnsFilteredTransfers() {
        final TransferRequestDTO request = new TransferRequestDTO.Builder().addFilter("status", "successful").build();
        repository.delete(1);

        final List<Transfer> response = repository.fetchMany(request);

        Assert.assertEquals(14, response.size());
    }

    @Test
    public void testFetchMany_whenRequestHasSourceFilter_thenReturnsFilteredTransfers() {
        final TransferRequestDTO request = new TransferRequestDTO.Builder().addFilter("source", "2").build();

        final List<Transfer> response = repository.fetchMany(request);

        Assert.assertEquals(1, response.size());
    }

    @Test
    public void testFetchMany_whenRequestHasStatusAndSourceFilter_thenReturnsEmptyList() {
        final TransferRequestDTO request = new TransferRequestDTO.Builder().addFilter("source", "2").addFilter("status", "successful").build();
        repository.delete(3);

        final List<Transfer> response = repository.fetchMany(request);

        Assert.assertTrue(response.isEmpty());
    }

    @Test
    public void testFetchOne_whenTransferDoesNotExists_thenReturnNull() {

        Assert.assertNull(repository.fetchOne(30));
    }

    @Test
    public void testFetchOne_whenTransferExists_thenReturnsTransfer() {

        final Transfer response = repository.fetchOne(5);

        Assert.assertNotNull(response);
        Assert.assertEquals(5, response.getId());
    }

    @Test
    public void testCreate_whenIsCalled_thenTransferIsCreatedAndReturned() {
        final TransferRequestDTO request = new TransferRequestDTO.Builder()
                .setSourceId("1200").setTransaction("15000")
                .setDestinationId("1000").build();

        final Transfer response = repository.create(request);

        Assert.assertEquals(16, response.getId());
        Assert.assertEquals("successful", response.getStatus());
        Assert.assertEquals("1200", response.getSourceId());
        Assert.assertEquals("15000", response.getTransaction());
        Assert.assertEquals("1000", response.getDestinationId());
    }

    @Test
    public void testUpdate_whenTransferDoesNotExists_thenReturnNull() {

        final Transfer response = repository.update(new Transfer.Builder().setId(1000).build());

        Assert.assertNull(response);
    }

    @Test
    public void testUpdate_whenTransferDoesExists_thenUpdateAndReturnOldTransfer() {
        final Transfer expected = new Transfer.Builder().setId(3).setSourceId("12000").build();

        final Transfer response = repository.update(expected);

        Assert.assertNotNull(response);
        Assert.assertEquals(expected.getSourceId(), repository.fetchOne(expected.getId()).getSourceId());
    }

    @Test
    public void testDelete_whenTransferDoesNotExists_thenReturnNull() {

        final Transfer response = repository.delete(1000);

        Assert.assertNull(response);
    }

    @Test
    public void testDelete_whenTransferDoesExists_thenDeactivatesAndReturnOldTransfer() {

        final Transfer response = repository.delete(3);

        Assert.assertNotNull(response);
        Assert.assertEquals("rollback", repository.fetchOne(3).getStatus());
    }

    private void createManyMocks() {
        final TransferRequestDTO.Builder builder = new TransferRequestDTO.Builder();
        for (int i = 0; i < 15; i++) {
            repository.create(builder.setSourceId(String.valueOf(i))
                    .setDestinationId(String.valueOf(i+1))
                    .setTransaction(String.valueOf(i*2)).build());
        }
    }
}