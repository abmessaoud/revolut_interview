package com.revolut.payments.core;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private HttpServer server;
    private boolean started;
    private static final int SERVER_PORT = 8000;

    public Server() throws IOException {
        server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
    }

    public void start() {
        server.setExecutor(null);
        server.start();
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public HttpServer getServer() {
        return server;
    }
}
