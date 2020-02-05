package com.revolut.payments.core;

import com.revolut.payments.core.http.AccountHandler;
import com.revolut.payments.core.http.PongHandler;
import com.revolut.payments.core.http.TransferHandler;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.util.HashMap;
import java.util.Map;

public class PathBinder {
    private Server server;
    private boolean binded;
    private final Map<String, HttpHandler> handlers = new HashMap<>();

    public PathBinder(final Server server, final RequestParser parser) {
        this.server = server;
        handlers.put("/ping", new PongHandler());
        handlers.put("/accounts", new AccountHandler(parser));
        handlers.put("/transfers", new TransferHandler(parser));
    }

    public void bind() {
        if (binded) {
            return;
        }
        final HttpServer server = this.server.getServer();
        handlers.forEach(server::createContext);
        binded = true;
    }

    Server getServer() {
        return server;
    }

    Map<String, HttpHandler> getHandlers() {
        return handlers;
    }

    public boolean didBind() {
        return binded;
    }
}
