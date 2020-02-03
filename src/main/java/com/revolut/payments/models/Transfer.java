package com.revolut.payments.models;

public class Transfer {
    private int id;
    private String status;
    private String sourceId;
    private String transaction;
    private String destinationId;

    protected <B extends TransferBuilder<?,?>> Transfer(final B builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.sourceId = builder.sourceId;
        this.transaction = builder.transaction;
        this.destinationId = builder.destinationId;
    }
    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getTransaction() {
        return transaction;
    }

    public String getDestinationId() {
        return destinationId;
    }

    protected abstract static class TransferBuilder<B extends TransferBuilder<B, R>, R extends Transfer> {
        protected int id;
        protected String status;
        protected String sourceId;
        protected String transaction;
        protected String destinationId;

        protected abstract B self();

        public B setId(final int id) {
            this.id = id;
            return self();
        }

        public B setStatus(final String status) {
            this.status = status;
            return self();
        }

        public B setSourceId(final String sourceId) {
            this.sourceId = sourceId;
            return self();
        }

        public B setTransaction(final String transaction) {
            this.transaction = transaction;
            return self();
        }

        public B setDestinationId(final String destinationId) {
            this.destinationId = destinationId;
            return self();
        }

        public abstract R build();
    }

    public static final class Builder extends TransferBuilder<Builder, Transfer> {

        public Builder() {}
        public Builder(final Transfer old) {
            this.id = old.getId();
            this.status = old.getStatus();
            this.sourceId = old.getSourceId();
            this.transaction = old.getTransaction();
            this.destinationId = old.getDestinationId();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Transfer build() {
            return new Transfer(this);
        }
    }
}
