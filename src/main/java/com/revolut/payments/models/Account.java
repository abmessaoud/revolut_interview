package com.revolut.payments.models;

public class Account {
    private String id;
    private String status;
    private String balance;

    protected <B extends AccountBuilder<?,?>> Account(final B builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.balance = builder.balance;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getBalance() {
        return balance;
    }

    protected abstract static class AccountBuilder<B extends AccountBuilder<B, R>, R extends Account> {
        protected String id;
        protected String status;
        protected String balance;

        protected abstract B self();

        public B setId(final String id) {
            this.id = id;
            return self();
        }

        public B setStatus(final String status) {
            this.status = status;
            return self();
        }

        public B setBalance(final String balance) {
            this.balance = balance;
            return self();
        }

        public abstract R build();
    }

    public static final class Builder extends AccountBuilder<Builder, Account> {

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Account build() {
            return new Account(this);
        }
    }
}
