package com.revolut.payments;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import com.revolut.payments.controllers.AccountController;
import com.revolut.payments.controllers.TransferController;
import com.revolut.payments.dto.RequestDTO;
import com.revolut.payments.dto.ResponseDTO;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;

class Application {
    private static final int SERVER_PORT = 8000;
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        server.createContext("/ping", exchange -> {
            exchange.sendResponseHeaders(200, "pong".getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write("pong".getBytes());
            output.flush();
            exchange.close();
        });
        server.createContext("/account", exchange -> {
            final AccountController controller = AccountController.getInstance();
            final ResponseDTO response = controller.handle(handleRequest(exchange));
            if (response == null) {
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                return;
            }
            final String responseBody = JsonUtils.Gson.toJson(response.getBody());
            exchange.sendResponseHeaders(response.getHttpStatus(), responseBody.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(responseBody.getBytes());
            output.flush();
            exchange.close();
        });
        server.createContext("/transfer", exchange -> {
            final TransferController controller = TransferController.getInstance();
            final ResponseDTO response = controller.handle(handleRequest(exchange));
            if (response == null) {
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
                return;
            }
            final String responseBody = JsonUtils.Gson.toJson(response.getBody());
            exchange.sendResponseHeaders(response.getHttpStatus(), responseBody.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(responseBody.getBytes());
            output.flush();
            exchange.close();
        });

        server.setExecutor(null); // creates a default executor
        server.start();
    }

    private static RequestDTO handleRequest(final HttpExchange exchange) {
        return new RequestDTO()
                .setMethod(exchange.getRequestMethod().toLowerCase())
                .setUrlParam(parseUrlParam(exchange.getRequestURI().getPath()))
                .addManyBodyParam(parseBodyParams(exchange.getRequestBody()))
                .addManyQueryParam(parseQueryParams(exchange.getRequestURI().getRawQuery()));
    }

    private static String parseUrlParam(final String path) {
        return Pattern.compile("/").splitAsStream(path)
                .filter(StringUtils::isNumeric)
                .collect(Collectors.joining(""));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseBodyParams(final InputStream ios) {
        try {
            int i;
            final StringBuilder sb = new StringBuilder();
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            if (sb.length() == 0) {
                return Collections.emptyMap();
            }
            return JsonUtils.Gson.fromJson(sb.toString(), HashMap.class);
        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }

    private static Map<String, Object> parseQueryParams(final String rawQuery) {
        if (StringUtils.isBlank(rawQuery)) {
            return Collections.emptyMap();
        }
        final Map<String, Object> query = new HashMap<>();
        Pattern.compile("&").splitAsStream(rawQuery)
                .map(s -> s.split("="))
                .forEach(param -> query.put(param[0], param[1]));
        return query;
    }

    private static HttpServer startServer() throws IOException {
        if (server == null) {
            server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        }
        return server;
    }
}
