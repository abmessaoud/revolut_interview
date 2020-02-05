package com.revolut.payments.core;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class InjectorTest {
    @Test
    public void testSingleton_whenIsCalled_thenSingletonIsCreated() {

        Injector.getInjector().singleton(InjectionMock.class);

        assertNotNull(Injector.getInjector().getInstance(InjectionMock.class));
    }

    @Test
    public void testSingleton_whenConstructorReceivesParams_thenSingletonIsCreated() {

        Injector.getInjector().singleton(InjectionWithParamsMock.class);

        assertNotNull(Injector.getInjector().getInstance(InjectionWithParamsMock.class));
    }

    @Test
    public void testBind_whenIsCalled_thenBindingIsMade() {

        Injector.getInjector().bind(InjectionMock.class, new InjectionWithInheritanceMock());

        final InjectionMock mock = Injector.getInjector().getInstance(InjectionMock.class);
        assertNotNull(mock);
        assertTrue(mock instanceof InjectionWithInheritanceMock);
    }

    @Test
    public void testGetInstance_whenCalledTwice_thenReturnsSameInstance() {

        final Injector first = Injector.getInjector();
        final Injector second = Injector.getInjector();

        assertSame(first, second);
    }

    private static class InjectionMock {
        public InjectionMock() {}
    }

    private static class InjectionWithInheritanceMock extends InjectionMock {
        public InjectionWithInheritanceMock() {}
    }

    private static class InjectionWithParamsMock {
        @SuppressWarnings("unused")
        public InjectionWithParamsMock(InjectionMock mock) {}
    }
}