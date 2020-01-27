package com.revolut.payments;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.revolut.payments.controllers.UserController;
import com.revolut.payments.dto.ResponseDTO;
import com.sun.net.httpserver.HttpServer;

class Application {
    private static final int SERVER_PORT = 8000;
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        server.createContext("/users", (exchange -> {
            final UserController userController = UserController.getInstance();
            try {
                final ResponseDTO response = userController.handle(exchange);
                exchange.sendResponseHeaders(200, response.toString().getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write(response.toString().getBytes());
                output.flush();
                exchange.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static HttpServer startServer() throws IOException {
        if (server == null) {
            server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        }
        return server;
    }
}
