package com.newhost2.app;

import io.vertx.core.Vertx;
import ru.doxhost.newhost.server.Nh2HttpServer;

/**
 * @author Eugene Kirin
 */
public class MainClass {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Nh2HttpServer.class.getName());
    }
}
