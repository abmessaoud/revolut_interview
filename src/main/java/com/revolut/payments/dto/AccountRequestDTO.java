package com.revolut.payments.dto;

import com.revolut.payments.models.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountRequestDTO extends Account {
    private Map<String, Object> filters;

    private AccountRequestDTO(final Builder builder) {
        super(builder);
        this.filters = builder.filters;
    }

    public Object getFilter(final String key) {
        return filters.get(key);
    }

    public static final class Builder extends AccountBuilder<Builder, Account> {
        private Map<String, Object> filters = new HashMap<>();

        public Builder addFilter(final String key, final Object value) {
            filters.put(key, value);
            return self();
        }

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
