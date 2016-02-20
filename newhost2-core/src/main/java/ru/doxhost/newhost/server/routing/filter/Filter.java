package ru.doxhost.newhost.server.routing.filter;

import io.vertx.ext.web.RoutingContext;
import ru.doxhost.newhost.server.routing.Result;

/**
 * A simple filter that can be applied to controller methods or while classes.
 * 
 * usually you use <code>@FilterWith(MyFilter.class)</code> where MyFilter.class
 * is implementing this interface.
 * 
 * <code>@FilterWith</code> works also with multiple filter
 * <code>@FilterWith({MyFirstFilter.class, MySecondFilter.class})</code>
 * 
 * @author ra
 * 
 */
public interface Filter {
    /**
     * Filter the request. Filters should invoke the filterChain.nextFilter()
     * method if they wish the request to proceed.
     * 
     *
     * @param context
     *            The context
     */
    Result filter(RoutingContext context);
}
