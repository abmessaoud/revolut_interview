package com.revolut.payments.core;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ServerTest {
    @Test
    public void testConstructor_whenInvoked_thenServerIsCreated() throws IOException {

        final Server server = new Server();

        Assert.assertNotNull(server.getServer());
    }

    @Test
    public void testStart_whenIsCalled_thenServerStartsAndFlagRaised() throws IOException {
        final Server server = new Server();

        server.start();

        Assert.assertTrue(server.isStarted());
    }

    @Test
    public void testIsStarted_whenServerIsNotStarted_thenReturnsFalse() throws IOException {
        final Server server = new Server();

        final boolean started = server.isStarted();

        Assert.assertTrue(started);
    }
}