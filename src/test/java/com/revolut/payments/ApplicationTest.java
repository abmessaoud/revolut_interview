package com.revolut.payments;

import com.revolut.payments.controllers.AccountController;
import com.revolut.payments.controllers.TransferController;
import com.revolut.payments.core.Injector;
import com.revolut.payments.core.PathBinder;
import com.revolut.payments.core.Server;
import com.revolut.payments.repositories.AccountRepository;
import com.revolut.payments.repositories.TransferRepository;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApplicationTest {
    @Test
    public void testMain_whenIsCalled_thenStartsApplication() {
        final Injector injector = Injector.getInjector();

        Application.main(new String[0]);

        assertNotNull(injector.getInstance(Server.class));
        assertTrue(injector.getInstance(PathBinder.class).didBind());
        assertTrue(injector.getInstance(Server.class).isStarted());
    }

    @Test
    public void testDoBindings_whenCalled_thenSingletonsAreCorrectlyLoaded() {

        Application.doBindings();

        final Injector injector = Injector.getInjector();
        assertNotNull(injector.getInstance(Server.class));
        assertNotNull(injector.getInstance(AccountRepository.class));
        assertNotNull(injector.getInstance(TransferRepository.class));
        assertNotNull(injector.getInstance(AccountController.class));
        assertNotNull(injector.getInstance(TransferController.class));
    }
}