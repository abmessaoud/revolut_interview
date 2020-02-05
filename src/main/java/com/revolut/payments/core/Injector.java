package com.revolut.payments.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Injector {
    private static Injector self;
    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    private Injector() {}

    public void singleton(final Class<?> clazz) {
        final Object instance = generateClass(clazz);
        dependencies.put(clazz, instance);
    }

    public void bind(final Class<?> mask, final Object instance) {
        dependencies.put(mask, instance);
    }

    private Object generateClass(final Class<?> clazz) {
        if (dependencies.containsKey(clazz)) {
            return dependencies.get(clazz);
        }
        final Constructor<?> constructor = clazz.getConstructors()[0];
        final Class<?>[] parameters = constructor.getParameterTypes();
        final List<Object> objects = new ArrayList<>();
        for (final Class<?> parameter : parameters) {
            objects.add(generateClass(parameter));
        }
        try {
            return constructor.newInstance(objects.toArray());
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

    public <T> T getInstance(final Class<T> clazz) {
        return (T) dependencies.get(clazz);
    }

    public static Injector getInjector() {
        if (self == null) {
            self = new Injector();
        }
        return self;
    }
}
