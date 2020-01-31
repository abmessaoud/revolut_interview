package com.revolut.payments.dto;

import com.revolut.payments.models.Account;

public class AccountRequestDTO extends Account {

    private AccountRequestDTO(final Builder builder) {
        super(builder);
    }

    public static final class Builder extends AccountBuilder<Builder, Account> {
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public AccountRequestDTO build() {
            return new AccountRequestDTO(this);
        }
    }
}
