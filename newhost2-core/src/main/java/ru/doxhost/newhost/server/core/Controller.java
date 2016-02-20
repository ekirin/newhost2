package ru.doxhost.newhost.server.core;

import io.vertx.core.http.HttpMethod;

/**
 * @author Eugene Kirin
 */
public class Controller {

    private final HttpMethod httpMethod;
    private final Class controller;
    private final String action;

    public Controller(final HttpMethod httpMethod, final Class controller, final String action) {
        this.httpMethod = httpMethod;
        this.controller = controller;
        this.action = action;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Class getController() {
        return controller;
    }

    public String getAction() {
        return action;
    }
}
