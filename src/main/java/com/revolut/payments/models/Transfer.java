package com.revolut.payments.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Transfer {
    String id;
    String status;
    String accountIdTo;
    String transaction;
    String accountIdFrom;
}
