package com.revolut.payments.dto;

import org.junit.Test;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;

public class TransferRequestDTOTest {
    @Test
    public void testBuilder_whenBuildIsInvoked_thenCreatesDifferentInstances() {
        final TransferRequestDTO.Builder builder = new TransferRequestDTO.Builder();

        assertNotSame("this objects are not the same", builder.build(), builder.build());
    }

    @Test
    public void testBuilder_withAddedFilters_thenPassesParametersCorrectly() {
        final TransferRequestDTO dto = new TransferRequestDTO.Builder()
                .addFilter("test", "value")
                .build();

        assertEquals("value", dto.getFilter("test"));
    }
}