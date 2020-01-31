package com.revolut.payments.dto;

public class ResponseDTO {
    private Object body;
    private int httpStatus;

    private ResponseDTO(final Builder builder) {
        this.httpStatus = builder.httpStatus;
        this.body = builder.body;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Object getBody() {
        return body;
    }

    public static final class Builder {
        private Object body;
        private int httpStatus;

        public Builder setBody(final Object body) {
            this.body = body;
            return this;
        }

        public Builder setHttpStatus(final int httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ResponseDTO build() {
            return new ResponseDTO(this);
        }
    }
}
