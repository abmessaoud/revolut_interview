package com.revolut.payments.controllers;

public class AccountController {
    private static AccountController instance;

    public static AccountController getInstance() {
        if (instance == null) {
            instance = new AccountController();
        }
        return instance;
    }
}
