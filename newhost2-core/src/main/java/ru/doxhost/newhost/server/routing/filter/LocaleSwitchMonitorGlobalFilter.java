package ru.doxhost.newhost.server.routing.filter;

import io.vertx.ext.web.RoutingContext;

import ru.doxhost.newhost.server.core.Nh2Params;
import ru.doxhost.newhost.server.routing.Result;


import java.util.Locale;
import java.util.Optional;

/**
 * @author Eugene Kirin on 12.11.2015.
 */
public class LocaleSwitchMonitorGlobalFilter implements Filter {

    /**
     * Set for all requests
     * @param context
     * @return
     */
    @Override
    public Result filter(RoutingContext context) {

        Optional<String> lang = Optional.ofNullable(context.request().getParam(Nh2Params.PARAM_LANG));

        if (lang.isPresent()) {

            Locale.setDefault(Locale.ENGLISH.equals(lang.get()) ? Locale.ENGLISH : new Locale(lang.get(), ""));
        }

        context.put("lang", Locale.getDefault().getLanguage());

        return null;
    }
}
