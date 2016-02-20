package ru.doxhost.newhost.server.routing;

import io.vertx.ext.web.RoutingContext;
import ru.doxhost.newhost.server.core.Controller;

/**
 * This interface executes controller's method
 * @author Eugene Kirin on 13.11.2015.
 */
public interface ISmartControllerInvoker {

    static ISmartControllerInvoker create(final Controller controller, final RoutingContext context) {
        return new SmartControllerInvoker(controller, context);
    }

    Result invokeAction(final IValidator validator);
}
