package com.revolut.payments.dto;

import java.util.HashMap;
import java.util.Map;

public class RequestDTO {
    private String method;
    private String urlParam;
    private final Map<String, Object> query = new HashMap<>();
    private final Map<String, Object> body = new HashMap<>();

    public String getMethod() {
        return method;
    }

    public RequestDTO setMethod(final String method) {
        this.method = method;
        return this;
    }

    public String getUrlParam() {
        return urlParam;
    }

    public RequestDTO setUrlParam(final String urlParam) {
        this.urlParam = urlParam;
        return this;
    }

    public Object getQueryParam(final String key) {
        return query.get(key);
    }

    public boolean hasQueryParam(final String key) {
        return query.containsKey(key);
    }

    public RequestDTO addManyQueryParam(final Map<String, Object> query) {
        this.query.putAll(query);
        return this;
    }

    public Object getBodyParam(final String key) {
        return body.get(key);
    }

    public boolean hasBodyParam(final String key) {
        return body.containsKey(key);
    }

    public RequestDTO addBodyParam(final String key, final Object value) {
        this.body.put(key, value);
        return this;
    }

    public RequestDTO addManyBodyParam(final Map<String, Object> body) {
        this.body.putAll(body);
        return this;
    }
}
