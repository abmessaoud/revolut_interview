package com.revolut.payments.core.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class PongHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, "pong".getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write("pong".getBytes());
        output.flush();
        exchange.close();
    }
}
