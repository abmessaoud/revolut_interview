package com.revolut.payments.controllers;

import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class ControllerTest {
    private Controller controller;
    private RequestDTO request;

    @Before
    public void setUp() {
        controller = new DummyController();
        request = new RequestDTO();
    }

    @Test
    public void testHandle_whenDeclaredMethodDoesNotExists_thenReturnsNull() {
        request.setMethod("patch");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNull(response);
    }

    @Test
    public void testHandle_whenMethodDoesExists_thenInvocationSucceed() {
        request.setMethod("get");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(2, response.getHttpStatus());
    }

    @Test
    public void testGet_whenRequestDoesNotHaveUrlParam_thenReturnsGetManyCall() {
        request.setMethod("get");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(2, response.getHttpStatus());
    }

    @Test
    public void testGet_whenRequestDoesHaveAUrlParam_thenReturnsGetByIdCall() {
        request.setMethod("get");
        request.setUrlParam("1");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(1, response.getHttpStatus());
    }

    @Test
    public void testPost_whenRequestDoesHaveAUrlParam_thenReturnsNull() {
        request.setMethod("post");
        request.setUrlParam("1");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNull(response);
    }

    @Test
    public void testPost_whenRequestDoesNotHaveUrlParam_thenReturnsCreateCall() {
        request.setMethod("post");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(3, response.getHttpStatus());
    }

    @Test
    public void testPut_whenRequestDoesNotHaveUrlParam_thenReturnsNull() {
        request.setMethod("put");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNull(response);
    }

    @Test
    public void testPut_whenRequestDoesNotHaveBalanceBodyParam_thenReturnsNull() {
        request.setMethod("put");
        request.setUrlParam("1");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNull(response);
    }

    @Test
    public void testPut_whenRequestDoesHaveAUrlParamAndBalanceBodyParam_thenReturnsUpdateCall() {
        request.setMethod("put");
        request.setUrlParam("1");
        request.addManyBodyParam(Collections.singletonMap("balance", "123"));

        final ResponseDTO response = controller.handle(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(4, response.getHttpStatus());
    }

    @Test
    public void testDelete_whenRequestDoesNotHaveUrlParam_thenReturnsNull() {
        request.setMethod("delete");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNull(response);
    }

    @Test
    public void testDelete_whenRequestDoesHaveAUrlParam_thenReturnsDeactivateCall() {
        request.setMethod("delete");
        request.setUrlParam("1");

        final ResponseDTO response = controller.handle(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(5, response.getHttpStatus());
    }

    private static class DummyController extends Controller {
        @Override
        protected ResponseDTO create(final RequestDTO requestDTO) {
            return new ResponseDTO.Builder().setHttpStatus(3).build();
        }

        @Override
        protected ResponseDTO update(final RequestDTO requestDTO) {
            return new ResponseDTO.Builder().setHttpStatus(4).build();
        }

        @Override
        protected ResponseDTO getMany(final RequestDTO requestDTO) {
            return new ResponseDTO.Builder().setHttpStatus(2).build();
        }

        @Override
        protected ResponseDTO getById(final RequestDTO requestDTO) {
            return new ResponseDTO.Builder().setHttpStatus(1).build();
        }

        @Override
        protected ResponseDTO deactivate(final RequestDTO requestDTO) {
            return new ResponseDTO.Builder().setHttpStatus(5).build();
        }
    }
}