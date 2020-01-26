package com.revolut.payments.repositories;

import com.revolut.payments.models.User;

public class UserRepository extends Repository<User> {
    private static UserRepository instance;

    @Override
    protected User parseData() {
        return null;
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
}
