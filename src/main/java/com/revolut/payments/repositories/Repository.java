package com.revolut.payments.repositories;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unchecked")
public abstract class Repository<Model> {
    private static final ConcurrentMap<String, Object> MEMORY_STORE = new ConcurrentHashMap<>();

    public Model fetchOne() {

        return (Model) MEMORY_STORE.get("KEY");
    }

    public Set<Model> fetchMany() {
        return Collections.singleton((Model) MEMORY_STORE.get("KEY"));
    }

    public Model create() {
        final String id = UUID.randomUUID().toString();
        MEMORY_STORE.put("KEY", "VALUE");
        return (Model) MEMORY_STORE.get("KEY");
    }

    public Model update() {
        return (Model) MEMORY_STORE.get("KEY");
    }

    public Model delete() {
        return (Model) MEMORY_STORE.get("KEY");
    }

    protected abstract Model parseData();
}
