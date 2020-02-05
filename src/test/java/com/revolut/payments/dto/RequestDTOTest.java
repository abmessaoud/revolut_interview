package com.revolut.payments.dto;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RequestDTOTest {
    @Test
    public void testGettersAndSetters_whenTheyAreCalled_thenHandlesParamsCorrectly() {
        final RequestDTO dto = new RequestDTO()
                .setMethod("get")
                .setUrlParam("1");

        assertEquals("get", dto.getMethod());
        assertEquals("1", dto.getUrlParam());
    }

    @Test
    public void testAddersAndGettersOfBodyAndQueryParams_whenTheyAreCalled_thenHandlesDataProperly() {
        final Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("testQuery1", "valueQuery1");
        queryParams.put("testQuery2", "valueQuery2");
        final Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("testParam1", "valueParam1");
        bodyParams.put("testParam2", "valueParam2");

        final RequestDTO dto = new RequestDTO()
                .addManyBodyParam(bodyParams)
                .addManyQueryParam(queryParams)
                .addBodyParam("testParam3", "valueParam3");

        assertEquals("valueQuery1", dto.getQueryParam("testQuery1"));
        assertEquals("valueQuery2", dto.getQueryParam("testQuery2"));
        assertEquals("valueParam1", dto.getBodyParam("testParam1"));
        assertEquals("valueParam2", dto.getBodyParam("testParam2"));
        assertEquals("valueParam3", dto.getBodyParam("testParam3"));
    }
}