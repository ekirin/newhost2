package ru.doxhost.newhost.server.routing.filter;

import io.vertx.ext.web.RoutingContext;
import ru.doxhost.newhost.server.routing.Result;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eugene Kirin on 12.11.2015.
 */
public class GlobalFilter implements IGlobalFilter {

    private Set<Filter> global = new HashSet<>();

    @Override
    public void handle(RoutingContext context) {

        global.stream().forEach(filter -> {
            filter.filter(context);
        });

        context.next();
    }

    public GlobalFilter addFilter(Filter filter) {
        global.add(filter);
        return this;
    }
}
