package ru.doxhost.newhost.server.routing.filter;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Eugene Kirin on 12.11.2015.
 */
public interface IGlobalFilter extends Handler<RoutingContext> {

    static IGlobalFilter create() {
        return new GlobalFilter();
    }

    IGlobalFilter addFilter(Filter filter);
}
