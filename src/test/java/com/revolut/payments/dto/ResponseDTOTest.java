package com.revolut.payments.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class ResponseDTOTest {
    @Test
    public void testBuilder_whenBuildIsInvoked_thenCreatesDifferentInstances() {
        final ResponseDTO.Builder builder = new ResponseDTO.Builder();

        assertNotSame("this objects are not the same", builder.build(), builder.build());
    }

    @Test
    public void testBuilder_whenSettersAreCalled_thenPassValuesCorrectly() {
        final ResponseDTO dto = new ResponseDTO.Builder()
                .setHttpStatus(200)
                .setBody("test body")
                .build();

        assertEquals(200, dto.getHttpStatus());
        assertEquals("test body", dto.getBody());
    }
}