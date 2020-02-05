package com.revolut.payments.controllers;

import com.revolut.payments.dto.AccountRequestDTO;
import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;
import com.revolut.payments.models.Account;
import com.revolut.payments.repositories.AccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AccountControllerTest {
    private AccountRepository repository;
    private AccountController controller;
    private RequestDTO request;

    @Before
    public void setUp() {
        repository = Mockito.mock(AccountRepository.class);
        controller = new AccountController(repository);
        request = new RequestDTO();
    }

    @Test
    public void testGetMany_whenRepositoryFetchNone_thenReturnsEmptyListBodyResponse() {
        request.setMethod("get");
        Mockito.doReturn(Collections.emptyList()).when(repository).fetchMany(Mockito.any(AccountRequestDTO.class));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(200, response.getHttpStatus());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(((List) response.getBody()).isEmpty());
    }

    @Test
    public void testGetMany_whenRepositoryFetchOne_thenReturnsSingletonListBodyResponse() {
        request.setMethod("get");
        Mockito.doReturn(Collections.singletonList(new Account.Builder().build())).when(repository).fetchMany(Mockito.any(AccountRequestDTO.class));

        final ResponseDTO response = controller.handle(request);

        final List body = (List) response.getBody();
        Assert.assertEquals(1, body.size());
        Assert.assertTrue(body.get(0) instanceof Account);
    }

    @Test
    public void testGetMany_whenRepositoryFetchMany_thenReturnsFullListBodyResponse() {
        request.setMethod("get");
        final Account.Builder builder = new Account.Builder();
        Mockito.doReturn(Arrays.asList(builder.build(), builder.build(), builder.build())).when(repository).fetchMany(Mockito.any(AccountRequestDTO.class));

        final ResponseDTO response = controller.handle(request);

        final List body = (List) response.getBody();
        Assert.assertEquals(3, body.size());
        Assert.assertTrue(body.get(0) instanceof Account);
        Assert.assertTrue(body.get(1) instanceof Account);
        Assert.assertTrue(body.get(2) instanceof Account);
    }

    @Test
    public void testGetById_whenRepositoryFetchNone_thenReturnsNotFoundStatusResponse() {
        request.setMethod("get")
                .setUrlParam("1");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(404, response.getHttpStatus());
        Assert.assertEquals("Resource not found", response.getBody());
    }

    @Test
    public void testGetById_whenRepositoryFetchOne_thenReturnsAccountBodyResponse() {
        request.setMethod("get")
                .setUrlParam("1");
        Mockito.doReturn(new Account.Builder().build()).when(repository).fetchOne(Mockito.eq(1));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(200, response.getHttpStatus());
        Assert.assertTrue(response.getBody() instanceof Account);
    }

    @Test
    public void testCreate_whenRequestHasNotBalanceBodyParam_thenReturnsBadRequestStatusResponse() {
        request.setMethod("post");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(400, response.getHttpStatus());
        Assert.assertEquals("Bad Request", response.getBody());
    }

    @Test
    public void testCreate_whenRequestHasBalanceBodyParam_thenReturnsNewAccountBodyResponse() {
        request.setMethod("post")
                .addBodyParam("balance", "12.0");
        Mockito.doReturn(new Account.Builder().build()).when(repository).create(Mockito.any(AccountRequestDTO.class));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(201, response.getHttpStatus());
        Assert.assertTrue(response.getBody() instanceof Account);
    }

    @Test
    public void testUpdate_whenAccountDoesNotExists_thenReturnsNotFoundStatusResponse() {
        request.setMethod("put")
                .setUrlParam("1")
                .addBodyParam("balance", "12.0");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(404, response.getHttpStatus());
        Assert.assertEquals("Resource not found", response.getBody());
    }

    @Test
    public void testUpdate_whenAccountIsUpdated_thenReturnsOldAccountBodyResponse() {
        request.setMethod("put")
                .setUrlParam("1")
                .addBodyParam("balance", "12.0");
        Mockito.doReturn(new Account.Builder().build()).when(repository).update(Mockito.any(AccountRequestDTO.class));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(204, response.getHttpStatus());
        Assert.assertTrue(response.getBody() instanceof Account);
    }

    @Test
    public void testDeactivate_whenAccountDoesNotExists_thenReturnsNotFoundStatusResponse() {
        request.setMethod("delete")
                .setUrlParam("1");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(404, response.getHttpStatus());
        Assert.assertEquals("Resource not found", response.getBody());
    }

    @Test
    public void testDeactivate_whenAccountIsSoftDeleted_thenReturnsOldAccountBodyResponse() {
        request.setMethod("delete")
                .setUrlParam("1");
        Mockito.doReturn(new Account.Builder().build()).when(repository).delete(Mockito.eq(1));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(202, response.getHttpStatus());
        Assert.assertTrue(response.getBody() instanceof Account);
    }
}