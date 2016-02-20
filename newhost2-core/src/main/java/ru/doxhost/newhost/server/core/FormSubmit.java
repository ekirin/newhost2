package ru.doxhost.newhost.server.core;

import io.vertx.ext.web.RoutingContext;
import ru.doxhost.newhost.server.Nh2Cookies;

/**
 * This class to operation when web form is submitted by user
 * @author Eugene Kirin
 */
public class FormSubmit {
    public static final String NH2_FORM_SUBMIT_ERRORS = "errors";

    public static final String NH2_FORM_SUBMIT_STATE = "state";

    private final RoutingContext routingContext;

    public FormSubmit(RoutingContext routingContext) {
        this.routingContext = routingContext;
    }

    public String ifFormHasErrorsDontLeave(final String action) {
        if (routingContext.data().containsKey(FormSubmit.NH2_FORM_SUBMIT_ERRORS)) {
            return Nh2Cookies.httpReferer(routingContext);
        }
        return action;
    }
}
