package com.revolut.payments.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TransferTest {
    @Test
    public void testBuilder_whenBuildIsInvoked_thenCreatesDifferentInstances() {
        final Transfer.Builder builder = new Transfer.Builder();

        assertNotSame("this objects are not the same", builder.build(), builder.build());
    }

    @Test
    public void testBuilder_withAddedFilters_thenPassesParametersCorrectly() {
        final Transfer transfer = new Transfer.Builder()
                .setId(1234)
                .setStatus("active")
                .setSourceId("2345")
                .setTransaction("3456")
                .setDestinationId("4567")
                .build();

        assertEquals(1234, transfer.getId());
        assertEquals("active", transfer.getStatus());
        assertEquals("2345", transfer.getSourceId());
        assertEquals("3456", transfer.getTransaction());
        assertEquals("4567", transfer.getDestinationId());
    }

    @Test
    public void testBuilder_whenCalledWithOldAccount_thenConsumesTheAccountCorrectly() {
        final Transfer expected = new Transfer.Builder()
                .setId(1234)
                .setStatus("active")
                .setSourceId("2345")
                .setTransaction("3456")
                .setDestinationId("4567")
                .build();

        final Transfer received = new Transfer.Builder(expected)
                .build();

        assertNotSame(expected, received);
        assertEquals(expected.getId(), received.getId());
        assertEquals(expected.getStatus(), received.getStatus());
        assertEquals(expected.getSourceId(), received.getSourceId());
        assertEquals(expected.getTransaction(), received.getTransaction());
        assertEquals(expected.getDestinationId(), received.getDestinationId());
    }
}