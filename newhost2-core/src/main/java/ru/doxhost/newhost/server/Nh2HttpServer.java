package ru.doxhost.newhost.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import ru.doxhost.newhost.server.config.Nh2Config;
import ru.doxhost.newhost.server.listener.IBootstrapListener;
import ru.doxhost.newhost.server.module.BootstrapModule;

import javax.inject.Inject;

/**
 * Main verticle which act as http server.
 */

public class Nh2HttpServer extends AbstractVerticle {

    public static final Logger logger = LoggerFactory.getLogger(Nh2HttpServer.class);

    private static final Injector injector;

    @Inject
    private IBootstrapListener bootstrapListener;

    static {
        injector = Guice.createInjector(new BootstrapModule());
    }

    {
        injector.injectMembers(this);
    }

    @Override
    public void start() {
        IBootstrapListener.start(); // TODO let custom listener (from config)
        bootstrapListener.onStart();
        vertx.createHttpServer().requestHandler(Nh2HttpServerRouter.init(vertx)::accept).listen(getServerPort());
        bootstrapListener.onFinish();
        IBootstrapListener.finish();
    }

    public int getServerPort() {
        return config().getInteger("http.port", Nh2Config.serverPort());
    }

    public static void main(String[] a) {
        Vertx.vertx().deployVerticle(Nh2HttpServer.class.getName());
    }

}