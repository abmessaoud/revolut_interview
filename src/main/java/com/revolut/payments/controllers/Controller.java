package com.revolut.payments.controllers;

import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;
import com.sun.net.httpserver.HttpExchange;

public abstract class Controller {
    public ResponseDTO handle(final HttpExchange exchange) throws Exception {
        switch(exchange.getRequestMethod()) {
            case "GET":
                return get(getRequest(exchange));
            case "POST":
                return post(getRequest(exchange));
            case "PUT":
                return put(getRequest(exchange));
            case "DELETE":
                return delete(getRequest(exchange));
            default:
                throw new Exception("Error handling request");
        }
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
