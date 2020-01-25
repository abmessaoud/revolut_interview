package com.revolut.payments.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ResponseDTO {
    private int httpStatus;
    private Object body;
}
