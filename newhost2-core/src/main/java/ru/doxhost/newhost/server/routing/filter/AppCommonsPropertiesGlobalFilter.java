package ru.doxhost.newhost.server.routing.filter;

import io.vertx.ext.web.RoutingContext;
import ru.doxhost.newhost.server.config.Nh2Config;
import ru.doxhost.newhost.server.routing.Result;

/**
 * @author Eugene Kirin on 12.11.2015.
 */
public class AppCommonsPropertiesGlobalFilter implements Filter {

    /**
     * Set for all requests
     * @param context
     * @return
     */
    @Override
    public Result filter(RoutingContext context) {

        context.put("ver", Nh2Config.appVersion());
        context.put("title", Nh2Config.appName());
        context.put("description", Nh2Config.appDescription());

        return null; // TODO - impl filter
    }
}
