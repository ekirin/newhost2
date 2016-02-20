package ru.doxhost.newhost.server.handler;

import io.vertx.ext.web.RoutingContext;
import ru.doxhost.newhost.server.routing.Result;

import java.nio.charset.StandardCharsets;

/**
 * @author Eugene Kirin
 */
public class StandardResponseHandler implements IResponseHandler {

    private final Result result;

    private final RoutingContext routingContext;

    public StandardResponseHandler(Result result, RoutingContext routingContext) {
        this.result = result;
        this.routingContext = routingContext;
    }

    @Override
    public void sendResponse() {
        String contentType = result.getType().toString() + "; charset=utf-8";
        routingContext.response().putHeader("content-type", contentType).
                end(result.toString(), StandardCharsets.UTF_8.toString());
    }
}
