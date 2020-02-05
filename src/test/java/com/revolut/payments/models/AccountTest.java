package com.revolut.payments.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class AccountTest {
    @Test
    public void testBuilder_whenBuildIsInvoked_thenCreatesDifferentInstances() {
        final Account.Builder builder = new Account.Builder();

        assertNotSame("this objects are not the same", builder.build(), builder.build());
    }

    @Test
    public void testBuilder_withAddedFilters_thenPassesParametersCorrectly() {
        final Account account = new Account.Builder()
                .setId(1234)
                .setStatus("active")
                .setBalance("12.0")
                .build();

        assertEquals(1234, account.getId());
        assertEquals("active", account.getStatus());
        assertEquals("12.0", account.getBalance());
    }

    @Test
    public void testBuilder_whenCalledWithOldAccount_thenConsumesTheAccountCorrectly() {
        final Account expected = new Account.Builder()
                .setId(1234)
                .setStatus("active")
                .setBalance("12.0")
                .build();

        final Account received = new Account.Builder(expected)
                .build();

        assertNotSame(expected, received);
        assertEquals(expected.getId(), received.getId());
        assertEquals(expected.getStatus(), received.getStatus());
        assertEquals(expected.getBalance(), received.getBalance());
    }
}