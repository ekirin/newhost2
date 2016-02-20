package ru.doxhost.newhost.server.module;

import com.google.inject.AbstractModule;

import ru.doxhost.newhost.server.listener.StandardBootstrapListener;
import ru.doxhost.newhost.server.listener.IBootstrapListener;

/**
 * @author Eugene Kirin
 */
public class BootstrapModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IBootstrapListener.class).to(StandardBootstrapListener.class);
    }
}
