package com.revolut.payments.controllers;

import com.revolut.payments.dto.AccountRequestDTO;
import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;
import com.revolut.payments.models.Account;
import com.revolut.payments.repositories.AccountRepository;
import org.apache.commons.lang3.StringUtils;
import java.util.List;

public class AccountController extends Controller {
    private AccountRepository repository;
    private static AccountController instance;

    private AccountController() {
        repository = AccountRepository.getInstance();
    }

    @Override
    protected ResponseDTO getMany(final RequestDTO requestDTO) {
        final AccountRequestDTO accountRequest = new AccountRequestDTO.Builder()
                .addFilter("status", requestDTO.getQueryParam("status"))
                .addFilter("balance", requestDTO.getQueryParam("balance"))
                .build();
        final List<Account> accounts = repository.fetchMany(accountRequest);
        return new ResponseDTO.Builder()
                .setHttpStatus(200)
                .setBody(accounts)
                .build();
    }

    @Override
    protected ResponseDTO getById(final RequestDTO requestDTO) {
        final Account account = repository.fetchOne(Integer.parseInt(requestDTO.getUrlParam()));
        if (account == null) {
            return new ResponseDTO.Builder()
                    .setHttpStatus(404)
                    .setBody("Resource not found")
                    .build();
        }
        return new ResponseDTO.Builder()
                .setHttpStatus(200)
                .setBody(account)
                .build();
    }

    @Override
    protected ResponseDTO create(final RequestDTO requestDTO) {
        final AccountRequestDTO accountRequest = new AccountRequestDTO.Builder()
                .setBalance((String) requestDTO.getBodyParam("balance"))
                .build();
        if (StringUtils.isBlank(accountRequest.getBalance())) {
            return new ResponseDTO.Builder()
                    .setHttpStatus(400)
                    .setBody("Bad Request")
                    .build();
        }
        final Account account = repository.create(accountRequest);
        return new ResponseDTO.Builder()
                .setHttpStatus(201)
                .setBody(account)
                .build();
    }

    @Override
    protected ResponseDTO update(final RequestDTO requestDTO) {
        final AccountRequestDTO accountRequest = new AccountRequestDTO.Builder()
                .setId(Integer.parseInt(requestDTO.getUrlParam()))
                .setBalance((String) requestDTO.getBodyParam("balance"))
                .build();
        final Account account = repository.update(accountRequest);
        if (account == null) {
            return new ResponseDTO.Builder()
                    .setHttpStatus(404)
                    .setBody("Resource not found")
                    .build();
        }
        return new ResponseDTO.Builder()
                .setHttpStatus(204)
                .setBody(account)
                .build();
    }

    @Override
    protected ResponseDTO deactivate(final RequestDTO requestDTO) {
        final Account account = repository.delete(Integer.parseInt(requestDTO.getUrlParam()));
        if (account == null) {
            return new ResponseDTO.Builder()
                    .setHttpStatus(404)
                    .setBody("Resource not found")
                    .build();
        }
        return new ResponseDTO.Builder()
                .setHttpStatus(202)
                .setBody(account)
                .build();
    }

    public static AccountController getInstance() {
        if (instance == null) {
            instance = new AccountController();
        }
        return instance;
    }
}
