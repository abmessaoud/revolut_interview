package com.revolut.payments.controllers;

import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;

public class UserController extends Controller {
    private static UserController instance;

    @Override
    protected ResponseDTO get(RequestDTO request) {
        return ResponseDTO.builder()
                .httpStatus(200)
                .body(Boolean.TRUE)
                .build();
    }

    @Override
    protected ResponseDTO post(RequestDTO request) {
        return ResponseDTO.builder()
                .httpStatus(200)
                .body(Boolean.TRUE)
                .build();
    }

    @Override
    protected ResponseDTO put(RequestDTO request) {
        return ResponseDTO.builder()
                .httpStatus(200)
                .body(Boolean.TRUE)
                .build();
    }

    @Override
    protected ResponseDTO delete(RequestDTO request) {
        return ResponseDTO.builder()
                .httpStatus(200)
                .body(Boolean.TRUE)
                .build();
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }
}
