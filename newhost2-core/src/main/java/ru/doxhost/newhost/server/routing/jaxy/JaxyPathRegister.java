package ru.doxhost.newhost.server.routing.jaxy;

public interface JaxyPathRegister {

    void register(final io.vertx.core.http.HttpMethod httpMethod, final String path, final Class<?> controllerClass, final String action);
}
