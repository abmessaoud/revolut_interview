package com.revolut.payments;

import com.revolut.payments.controllers.AccountController;
import com.revolut.payments.controllers.TransferController;
import com.revolut.payments.core.Injector;
import com.revolut.payments.core.PathBinder;
import com.revolut.payments.core.RequestParser;
import com.revolut.payments.core.Server;
import com.revolut.payments.repositories.AccountRepository;
import com.revolut.payments.repositories.TransferRepository;

class Application {

    public static void main(String[] args) {
        doBindings();
        final Server server = Injector.getInjector().getInstance(Server.class);
        final PathBinder binder = Injector.getInjector().getInstance(PathBinder.class);
        binder.bind();
        server.start();
    }

    public static void doBindings() {
        final Injector injector = Injector.getInjector();
        injector.singleton(Server.class);
        injector.singleton(RequestParser.class);
        injector.singleton(PathBinder.class);
        injector.singleton(AccountRepository.class);
        injector.singleton(TransferRepository.class);
        injector.singleton(AccountController.class);
        injector.singleton(TransferController.class);
    }
}
