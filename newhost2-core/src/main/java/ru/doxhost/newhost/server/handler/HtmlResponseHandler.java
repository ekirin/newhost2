package ru.doxhost.newhost.server.handler;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;
import ru.doxhost.newhost.server.config.Nh2Config;
import ru.doxhost.newhost.server.web.template.IRouteContentGenerator;
import ru.doxhost.newhost.server.web.template.ThymeleafEngine;


/**
 * @author Eugene Kirin
 */
public final class HtmlResponseHandler implements IResponseHandler {

    public static final Logger log = LoggerFactory.getLogger(HtmlResponseHandler.class);

    private final RoutingContext routingContext;

    private static final TemplateEngine tmplEngine = ThymeleafEngine.init();

    private String tmplName;

    private HtmlResponseHandler() {
        routingContext = null;
    }

    public HtmlResponseHandler(RoutingContext routingContext, String tmplName) {
        this.routingContext = routingContext;
        this.tmplName = tmplName;
    }

    @Override
    public void sendResponse() {

        if (Nh2Config.getConf().isDev()) {
            IRouteContentGenerator.create().gen(tmplName);
        }

        HtmlResponseHandler.tmplEngine.render(routingContext, tmplName, res -> {
            if (res.succeeded()) {
                routingContext.response()// do not allow proxies to cache the data
                        //.putHeader("Cache-Control", "no-store, no-cache")
                        // prevents Internet Explorer from MIME - sniffing a
                        // response away from the declared content-type
                        //.putHeader("X-Content-Type-Options", "nosniff")
                        // Strict HTTPS (for about ~6Months)
                        //.putHeader("Strict-Transport-Security", "max-age=" + 15768000)
                        // IE8+ do not allow opening of attachments in the context of this resource
                        //.putHeader("X-Download-Options", "noopen")
                        // enable XSS for IE
                        //.putHeader("X-XSS-Protection", "1; mode=block")
                        // deny frames
                        //.putHeader("X-FRAME-OPTIONS", "DENY")
                        .end(res.result());

            } else {
                routingContext.fail(res.cause());
            }
        });
    }
}
