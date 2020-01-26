package com.revolut.payments.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Account {
    String id;
    String status;
    String userId;
    String balance;
}
