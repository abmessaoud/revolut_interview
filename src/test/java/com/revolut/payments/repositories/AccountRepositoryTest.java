package com.revolut.payments.repositories;

import com.revolut.payments.dto.AccountRequestDTO;
import com.revolut.payments.models.Account;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AccountRepositoryTest {
    private AccountRepository repository;

    @Before
    public void setUp() {
        repository = new AccountRepository();
        createManyMocks();
    }

    @After
    public void tearDown() {
        repository.clearStore();
    }

    @Test
    public void testFetchMany_whenRequestHasNoFilters_thenReturnsAllAccounts() {
        final AccountRequestDTO request = new AccountRequestDTO.Builder().build();

        final List<Account> response = repository.fetchMany(request);

        Assert.assertEquals(15, response.size());
    }

    @Test
    public void testFetchMany_whenRequestHasStatusFilter_thenReturnsFilteredAccounts() {
        final AccountRequestDTO request = new AccountRequestDTO.Builder().addFilter("status", "active").build();
        repository.delete(1);

        final List<Account> response = repository.fetchMany(request);

        Assert.assertEquals(14, response.size());
    }

    @Test
    public void testFetchMany_whenRequestHasBalanceFilter_thenReturnsFilteredAccounts() {
        final AccountRequestDTO request = new AccountRequestDTO.Builder().addFilter("balance", "2").build();

        final List<Account> response = repository.fetchMany(request);

        Assert.assertEquals(1, response.size());
    }

    @Test
    public void testFetchMany_whenRequestHasStatusAndBalanceFilter_thenReturnsEmptyList() {
        final AccountRequestDTO request = new AccountRequestDTO.Builder().addFilter("balance", "2").addFilter("status", "active").build();
        repository.delete(2);

        final List<Account> response = repository.fetchMany(request);

        Assert.assertTrue(response.isEmpty());
    }

    @Test
    public void testFetchOne_whenAccountDoesNotExists_thenReturnNull() {

        Assert.assertNull(repository.fetchOne(30));
    }

    @Test
    public void testFetchOne_whenAccountExists_thenReturnsAccount() {

        final Account response = repository.fetchOne(5);

        Assert.assertNotNull(response);
        Assert.assertEquals(5, response.getId());
    }

    @Test
    public void testCreate_whenIsCalled_thenAccountIsCreatedAndReturned() {
        final AccountRequestDTO request = new AccountRequestDTO.Builder()
                .setBalance("1200").build();

        final Account response = repository.create(request);

        Assert.assertEquals(16, response.getId());
        Assert.assertEquals("active", response.getStatus());
        Assert.assertEquals("1200", response.getBalance());
    }

    @Test
    public void testUpdate_whenAccountDoesNotExists_thenReturnNull() {

        final Account response = repository.update(new Account.Builder().setId(1000).build());

        Assert.assertNull(response);
    }

    @Test
    public void testUpdate_whenAccountDoesExists_thenUpdateAndReturnOldAccount() {
        final Account expected = new Account.Builder().setId(3).setBalance("12000").build();

        final Account response = repository.update(expected);

        Assert.assertNotNull(response);
        Assert.assertEquals(expected.getBalance(), repository.fetchOne(expected.getId()).getBalance());
    }

    @Test
    public void testDelete_whenAccountDoesNotExists_thenReturnNull() {

        final Account response = repository.delete(1000);

        Assert.assertNull(response);
    }

    @Test
    public void testDelete_whenAccountDoesExists_thenDeactivatesAndReturnOldAccount() {

        final Account response = repository.delete(3);

        Assert.assertNotNull(response);
        Assert.assertEquals("deactive", repository.fetchOne(3).getStatus());
    }

    private void createManyMocks() {
        final AccountRequestDTO.Builder builder = new AccountRequestDTO.Builder();
        for (int i = 0; i < 15; i++) {
            repository.create(builder.setBalance(String.valueOf(i*2)).build());
        }
    }
}