package com.revolut.payments.core;

import com.revolut.payments.JsonUtils;
import com.revolut.payments.dto.RequestDTO;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RequestParser {
    public RequestParser() {}

    public RequestDTO parse(final HttpExchange exchange) {
        return new RequestDTO()
                .setMethod(exchange.getRequestMethod().toLowerCase())
                .setUrlParam(parseUrlParam(exchange.getRequestURI().getPath()))
                .addManyBodyParam(parseBodyParams(exchange.getRequestBody()))
                .addManyQueryParam(parseQueryParams(exchange.getRequestURI().getRawQuery()));
    }

    private String parseUrlParam(final String path) {
        return Pattern.compile("/").splitAsStream(path)
                .filter(StringUtils::isNumeric)
                .findFirst().orElse("");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseBodyParams(final InputStream ios) {
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

    private Map<String, Object> parseQueryParams(final String rawQuery) {
        if (StringUtils.isBlank(rawQuery)) {
            return Collections.emptyMap();
        }
        final Map<String, Object> query = new HashMap<>();
        Pattern.compile("&").splitAsStream(rawQuery)
                .map(s -> s.split("="))
                .forEach(param -> query.put(param[0], param[1]));
        return query;
    }
}
