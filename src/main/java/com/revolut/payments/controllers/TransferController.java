package com.revolut.payments.controllers;

public class TransferController {
    private static TransferController instance;

    public static TransferController getInstance() {
        if (instance == null) {
            instance = new TransferController();
        }
        return instance;
    }
}
