package com.revolut.payments.controllers;

import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;
import com.sun.net.httpserver.HttpExchange;

public abstract class Controller {
    public ResponseDTO handle(final HttpExchange exchange) throws Exception {
        return (ResponseDTO) this.getClass().getDeclaredMethod(exchange.getRequestMethod().toLowerCase(), HttpExchange.class).invoke(this, exchange);
    }

    protected ResponseDTO get(RequestDTO request) {
        return null;
    }
    protected ResponseDTO post(RequestDTO request) {
        return null;
    }
    protected ResponseDTO put(RequestDTO request) {
        return null;
    }
    protected ResponseDTO delete(RequestDTO request) {
        return null;
    }

    private RequestDTO getRequest(HttpExchange exchange) {
        return new RequestDTO();
    }
}
