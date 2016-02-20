package ru.doxhost.newhost.server;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import static ru.doxhost.newhost.server.Nh2ServerUpload.allowedSizeToUpload;

/**
 * Handler if server sent error code
 * @author Eugene Kirin
 */
public class Nh2RouteFailureHandler {

    public static final int CODE_ENTITY_TOO_LARGE = 413;

    public static final int CODE_NOT_FOUND = 404;

    public static final int CODE_SERVER_ERROR = 500;

    public static final int CODE_FORBIDDEN = 403;

    private final RoutingContext routingContext;

    public Nh2RouteFailureHandler(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    /**
     * Send error response
     */
    public void failed() {
        int statusCode = routingContext.statusCode();

        if (statusCode == CODE_ENTITY_TOO_LARGE) { //  Request Entity Too Large
            HttpServerResponse response = routingContext.response();
            response.setStatusCode(statusCode).end(
                    "You trying to send a very big file. Allow size " + allowedSizeToUpload() + ".");

            return;

        } else if (statusCode != CODE_NOT_FOUND) {
            routingContext.failure().printStackTrace();
        }

        // Status code will be 500 for the RuntimeException or 403 for the other failure
        HttpServerResponse response = routingContext.response();
        response.setStatusCode(statusCode == -1 ? CODE_SERVER_ERROR : statusCode).end("Sorry! Not today");
    }
}
