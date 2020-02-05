package com.revolut.payments.core;

import com.sun.net.httpserver.HttpServer;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class PathBinderTest {
    @Test
    public void testConstructor_whenIsCalled_thenSetsHandlers() {

        final PathBinder binder = new PathBinder(Mockito.mock(Server.class), null);

        Assert.assertNotNull(binder.getServer());
        Assert.assertEquals(3, binder.getHandlers().size());
        Assert.assertTrue(binder.getHandlers().containsKey("/ping"));
        Assert.assertTrue(binder.getHandlers().containsKey("/accounts"));
        Assert.assertTrue(binder.getHandlers().containsKey("/transfers"));
    }

    @Test
    public void testBind_whenIsCalledOnce_thenUpdatesBinded() {
        final Server serverMock = Mockito.mock(Server.class);
        Mockito.doReturn(Mockito.mock(HttpServer.class)).when(serverMock).getServer();
        final PathBinder binder = new PathBinder(serverMock, null);

        binder.bind();

        Assert.assertTrue(binder.didBind());
    }

    @Test
    public void testBind_whenIsCalledTwice_thenSecondIteratorDoesNothing() {
        final Server serverMock = Mockito.mock(Server.class);
        Mockito.doReturn(Mockito.mock(HttpServer.class)).when(serverMock).getServer();
        final PathBinder binder = new PathBinder(serverMock, null);

        binder.bind();
        binder.bind();
    }
}