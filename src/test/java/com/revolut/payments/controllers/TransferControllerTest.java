package com.revolut.payments.controllers;

import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;
import com.revolut.payments.dto.TransferRequestDTO;
import com.revolut.payments.models.Account;
import com.revolut.payments.models.Transfer;
import com.revolut.payments.repositories.AccountRepository;
import com.revolut.payments.repositories.TransferRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransferControllerTest {
    private TransferRepository transferRepository;
    private AccountRepository accountRepository;
    private TransferController controller;
    private RequestDTO request;

    @Before
    public void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        transferRepository = Mockito.mock(TransferRepository.class);
        controller = new TransferController(accountRepository, transferRepository);
        request = new RequestDTO();
    }

    @Test
    public void testGetMany_whenRepositoryFetchNone_thenReturnsEmptyListBodyResponse() {
        request.setMethod("get");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(200, response.getHttpStatus());
        Assert.assertNotNull(response.getBody());
        Assert.assertTrue(((List) response.getBody()).isEmpty());
    }

    @Test
    public void testGetMany_whenRepositoryFetchOne_thenReturnsSingletonListBodyResponse() {
        request.setMethod("get");
        Mockito.doReturn(Collections.singletonList(new Transfer.Builder().build())).when(transferRepository).fetchMany(Mockito.any(TransferRequestDTO.class));

        final ResponseDTO response = controller.handle(request);

        final List body = (List) response.getBody();
        Assert.assertEquals(1, body.size());
        Assert.assertTrue(body.get(0) instanceof Transfer);
    }

    @Test
    public void testGetMany_whenRepositoryFetchMany_thenReturnsFullListBodyResponse() {
        request.setMethod("get");
        final Transfer.Builder builder = new Transfer.Builder();
        Mockito.doReturn(Arrays.asList(builder.build(), builder.build(), builder.build())).when(transferRepository).fetchMany(Mockito.any(TransferRequestDTO.class));

        final ResponseDTO response = controller.handle(request);

        final List body = (List) response.getBody();
        Assert.assertEquals(3, body.size());
        Assert.assertTrue(body.get(0) instanceof Transfer);
        Assert.assertTrue(body.get(1) instanceof Transfer);
        Assert.assertTrue(body.get(2) instanceof Transfer);
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
        Mockito.doReturn(new Transfer.Builder().build()).when(transferRepository).fetchOne(Mockito.eq(1));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(200, response.getHttpStatus());
        Assert.assertTrue(response.getBody() instanceof Transfer);
    }

    @Test
    public void testCreate_whenRequestHasNotSourceBodyParam_thenReturnsBadRequestStatusResponse() {
        request.setMethod("post");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(400, response.getHttpStatus());
        Assert.assertEquals("Bad Request", response.getBody());
    }

    @Test
    public void testCreate_whenRequestHasNotTransactionBodyParam_thenReturnsBadRequestStatusResponse() {
        request.setMethod("post")
                .addBodyParam("source", "source");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(400, response.getHttpStatus());
        Assert.assertEquals("Bad Request", response.getBody());
    }

    @Test
    public void testCreate_whenRequestHasNotDestinationBodyParam_thenReturnsBadRequestStatusResponse() {
        request.setMethod("post")
                .addBodyParam("source", "source")
                .addBodyParam("transaction", "transaction");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(400, response.getHttpStatus());
        Assert.assertEquals("Bad Request", response.getBody());
    }

    @Test
    public void testCreate_whenRequestHasSameSourceAndDestinationBodyParam_thenReturnsBadRequestStatusResponse() {
        request.setMethod("post")
                .addBodyParam("source", "1")
                .addBodyParam("transaction", "transaction")
                .addBodyParam("destination", "1");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(400, response.getHttpStatus());
        Assert.assertEquals("Bad Request", response.getBody());
    }

    @Test
    public void testCreate_whenDestinationAccountDoesNotExists_thenReturnsConflictStatusResponse() {
        request.setMethod("post")
                .addBodyParam("source", "1")
                .addBodyParam("transaction", "12")
                .addBodyParam("destination", "2");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(409, response.getHttpStatus());
        Assert.assertEquals("Destination account does not exists.", response.getBody());
    }

    @Test
    public void testCreate_whenSourceAccountDoesNotExists_thenReturnsConflictStatusResponse() {
        final Account.Builder accountBuilder = new Account.Builder();
        request.setMethod("post")
                .addBodyParam("source", "1")
                .addBodyParam("transaction", "12")
                .addBodyParam("destination", "2");
        Mockito.doReturn(accountBuilder.build()).when(accountRepository).fetchOne(Mockito.eq(2));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(409, response.getHttpStatus());
        Assert.assertEquals("Source account does not exists.", response.getBody());
    }

    @Test
    public void testCreate_whenTransactionExceedsSourceAccountBalance_thenReturnsConflictStatusResponse() {
        final Account.Builder accountBuilder = new Account.Builder();
        request.setMethod("post")
                .addBodyParam("source", "1")
                .addBodyParam("transaction", "12")
                .addBodyParam("destination", "2");
        Mockito.doReturn(accountBuilder.build()).when(accountRepository).fetchOne(Mockito.eq(2));
        Mockito.doReturn(accountBuilder.setBalance("6").build()).when(accountRepository).fetchOne(Mockito.eq(1));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(409, response.getHttpStatus());
        Assert.assertEquals("Requested transaction exceeds the source account funds.", response.getBody());
    }

    @Test
    public void testCreate_whenAllValidationsSucceed_thenReturnsNewTransferBodyResponse() {
        final Account.Builder accountBuilder = new Account.Builder();
        request.setMethod("post")
                .addBodyParam("source", "1")
                .addBodyParam("transaction", "12")
                .addBodyParam("destination", "2");
        Mockito.doReturn(accountBuilder.setBalance("15").build()).when(accountRepository).fetchOne(Mockito.eq(2));
        Mockito.doReturn(accountBuilder.setBalance("22").build()).when(accountRepository).fetchOne(Mockito.eq(1));
        Mockito.doReturn(new Transfer.Builder().build()).when(transferRepository).create(Mockito.any(TransferRequestDTO.class));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(201, response.getHttpStatus());
        Assert.assertTrue(response.getBody() instanceof Transfer);
    }

    @Test
    public void testDeactivate_whenTransferDoesNotExists_thenReturnsNotFoundStatusResponse() {
        request.setMethod("delete")
                .setUrlParam("1");

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(404, response.getHttpStatus());
        Assert.assertEquals("Resource not found", response.getBody());
    }

    @Test
    public void testDeactivate_whenTransactionIsAlreadyRollback_thenReturnsConflictStatusResponse() {
        request.setMethod("delete")
                .setUrlParam("1");
        Mockito.doReturn(new Transfer.Builder().setStatus("rollback").build()).when(transferRepository).fetchOne(Mockito.eq(1));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(409, response.getHttpStatus());
        Assert.assertEquals("Transaction has already been rollback.", response.getBody());
    }

    @Test
    public void testDeactivate_whenTransactionExceedsNewSourceAccountBalance_thenReturnsConflictStatusResponse() {
        final Account.Builder accountBuilder = new Account.Builder();
        request.setMethod("delete")
                .setUrlParam("1");
        Mockito.doReturn(new Transfer.Builder().setSourceId("1")
                .setTransaction("12").setDestinationId("2").build())
                .when(transferRepository).fetchOne(Mockito.eq(1));
        Mockito.doReturn(accountBuilder.setBalance("6").build()).when(accountRepository).fetchOne(Mockito.eq(2));
        Mockito.doReturn(accountBuilder.setBalance("10").build()).when(accountRepository).fetchOne(Mockito.eq(1));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(409, response.getHttpStatus());
        Assert.assertEquals("Requested transaction exceeds the source account funds.", response.getBody());
    }

    @Test
    public void testDeactivate_whenAccountIsSoftDeleted_thenReturnsRollbackTransactionBodyResponse() {
        final Account.Builder accountBuilder = new Account.Builder();
        request.setMethod("delete")
                .setUrlParam("1");
        Mockito.doReturn(new Transfer.Builder().setSourceId("1")
                .setTransaction("12").setDestinationId("2").build())
                .when(transferRepository).fetchOne(Mockito.eq(1));
        Mockito.doReturn(accountBuilder.setBalance("27").build()).when(accountRepository).fetchOne(Mockito.eq(2));
        Mockito.doReturn(accountBuilder.setBalance("10").build()).when(accountRepository).fetchOne(Mockito.eq(1));

        final ResponseDTO response = controller.handle(request);

        Assert.assertEquals(202, response.getHttpStatus());
        Assert.assertTrue(response.getBody() instanceof Transfer);
    }
}