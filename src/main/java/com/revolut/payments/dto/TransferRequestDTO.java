package com.revolut.payments.dto;

import com.revolut.payments.models.Transfer;

public class TransferRequestDTO extends Transfer {

    private TransferRequestDTO(final Builder builder) {
        super(builder);
    }

    public static final class Builder extends TransferBuilder<Builder, TransferRequestDTO> {
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public TransferRequestDTO build() {
            return new TransferRequestDTO(this);
        }
    }
}
