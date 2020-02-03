package com.revolut.payments.dto;

import com.revolut.payments.models.Transfer;

import java.util.HashMap;
import java.util.Map;

public class TransferRequestDTO extends Transfer {
    private Map<String, Object> filters;

    private TransferRequestDTO(final Builder builder) {
        super(builder);
        this.filters = builder.filters;
    }

    public Object getFilter(final String key) {
        return filters.get(key);
    }

    public static final class Builder extends TransferBuilder<Builder, TransferRequestDTO> {
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
        public TransferRequestDTO build() {
            return new TransferRequestDTO(this);
        }
    }
}
