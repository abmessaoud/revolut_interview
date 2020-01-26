package com.revolut.payments.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class User {
    String id;
    String name;
    String email;
    String password;
    String username;
}
