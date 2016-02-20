package ru.doxhost.newhost.server.core;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;

import io.vertx.ext.web.RoutingContext;

import ru.doxhost.newhost.server.Nh2Cookies;
import ru.doxhost.newhost.server.routing.ISmartControllerInvoker;
import ru.doxhost.newhost.server.routing.Result;
import ru.doxhost.newhost.server.web.form.FormInputValidator;
import ru.doxhost.newhost.server.web.form.FormValidation;

import static ru.doxhost.newhost.server.core.FormSubmit.NH2_FORM_SUBMIT_ERRORS;
import static ru.doxhost.newhost.server.core.FormSubmit.NH2_FORM_SUBMIT_STATE;

/**
 * @author Eugene Kirin
 */
public final class ControllerDispatcher {

    private RoutingContext routingContext;

    private Controller controller;

    private ControllerDispatcher() {
    }

    private ControllerDispatcher(final Controller controller, final RoutingContext routingContext) {
        this.routingContext = routingContext;
        this.controller = controller;
    }

    public static ControllerDispatcher create(Controller controller, RoutingContext routingContext) {
        return new ControllerDispatcher(controller, routingContext);
    }

    private Result processPostRequest() {
        return ISmartControllerInvoker.create(controller, routingContext).invokeAction(target -> {

            FormValidation formValidation = FormInputValidator.validate1(target);

            if (formValidation.size() > 0) {
                routingContext.put(NH2_FORM_SUBMIT_ERRORS, formValidation);
                routingContext.put(NH2_FORM_SUBMIT_STATE, Json.encodePrettily(target));
            }
        });
    }

    private Result processGetRequest() {
        Nh2Cookies.saveHttpReferer(routingContext, controller.getAction());
        Nh2Cookies.saveUserLocale(routingContext);

        return ISmartControllerInvoker.create(controller, routingContext).invokeAction(null);
    }

    /**
     * Executes controller action
     * @return The result
     */
    public Result executeAction() {

        Result result = Result.OK();

        if (HttpMethod.POST == controller.getHttpMethod()) {
            result = processPostRequest();

        } else if (HttpMethod.GET == controller.getHttpMethod()) {
            result = processGetRequest();
        }

        result.bindWithContext(routingContext);

        return result;
    }
}
