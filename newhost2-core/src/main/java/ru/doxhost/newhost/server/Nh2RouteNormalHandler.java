package ru.doxhost.newhost.server;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import ru.doxhost.newhost.server.core.Controller;
import ru.doxhost.newhost.server.core.ControllerDispatcher;
import ru.doxhost.newhost.server.core.*;
import ru.doxhost.newhost.server.routing.Result;
import ru.doxhost.newhost.server.handler.HtmlResponseHandler;
import ru.doxhost.newhost.server.handler.StandardResponseHandler;


/**
 * @author Eugene Kirin
 */
public class Nh2RouteNormalHandler {

    public static final Logger log = LoggerFactory.getLogger(Nh2RouteNormalHandler.class);

    private final RoutingContext routingContext;

    public Nh2RouteNormalHandler(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public void handleWith(final Controller controller) {
        try {
            Result result = ControllerDispatcher.create(controller, routingContext).executeAction();

            if (result.isHtml()) {
                new HtmlResponseHandler(routingContext, controller.getAction()).sendResponse();

            } else if (result.isForm()) {
                new HtmlResponseHandler(routingContext,
                        new FormSubmit(routingContext).ifFormHasErrorsDontLeave(controller.getAction())).sendResponse();

            } else if (result.isText()) {
                new StandardResponseHandler(result, routingContext).sendResponse();

            } else if (result.isJson()) {
                new StandardResponseHandler(result, routingContext).sendResponse();

            } else {
                new StandardResponseHandler(result, routingContext).sendResponse();
            }

        } catch (RuntimeException e) {
            routingContext.fail(Nh2RouteFailureHandler.CODE_SERVER_ERROR);
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
