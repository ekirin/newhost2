package ru.doxhost.newhost.server;

import io.vertx.core.Vertx;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.StaticHandler;

import ru.doxhost.newhost.server.core.Controller;
import ru.doxhost.newhost.server.web.menu.MainMenuController;
import ru.doxhost.newhost.server.routing.filter.AppCommonsPropertiesGlobalFilter;
import ru.doxhost.newhost.server.routing.filter.IGlobalFilter;
import ru.doxhost.newhost.server.routing.filter.LocaleSwitchMonitorGlobalFilter;

import ru.doxhost.newhost.server.routing.handler.IFormGeneratorHandler;
import ru.doxhost.newhost.server.routing.jaxy.JaxyRoutes;

import static ru.doxhost.newhost.server.Nh2ServerUpload.allowedSize;

/**
 *
 */
public class Nh2HttpServerRouter {

    public static final Logger log = LoggerFactory.getLogger(Nh2HttpServerRouter.class);

    private Router router;

    public static Router init(final Vertx vertx) {

        log.debug("Init ServerRouter starting");

        Nh2HttpServerRouter serverRouter = new Nh2HttpServerRouter();

        serverRouter.router = Router.router(vertx);

        serverRouter.router.route().handler(IGlobalFilter.create()
                        .addFilter(new LocaleSwitchMonitorGlobalFilter()) // goes first cause has locale sensitive properties
                        .addFilter(new AppCommonsPropertiesGlobalFilter())
        );

        serverRouter.router.route().handler(CookieHandler.create());

        serverRouter.router.route("/static/html/*").handler(IFormGeneratorHandler.create());
        serverRouter.router.route("/webjars/*").handler(StaticHandler.create().setWebRoot("META-INF/resources/webjars"));
        serverRouter.router.route("/static/html/*").handler(StaticHandler.create().setWebRoot("static/html").setCachingEnabled(false));
        serverRouter.router.route("/static/js/*").handler(StaticHandler.create().setWebRoot("static/js").setCachingEnabled(false));
        serverRouter.router.route("/static/css/*").handler(StaticHandler.create().setWebRoot("static/css").setCachingEnabled(false));
        serverRouter.router.route("/static/json/*").handler(StaticHandler.create().setWebRoot("static/json").setCachingEnabled(false));

        new JaxyRoutes().findJaxyPaths((httpMethod, path, controller, action) -> {
            log.debug("Register path " + path);
            serverRouter.new RequestRouter(httpMethod).route(path).with(controller, action);
        });

        /**
         * Main menu json get from this url
         */
        serverRouter.GET().route("/menu/json").with(MainMenuController.class, "json");

        log.debug("Init ServerRouter done");

        return serverRouter.router;
    }

    private RequestRouter GET() {
        return new RequestRouter(HttpMethod.GET);
    }

    private class RequestRouter {

        private final HttpMethod method;

        private Route route;

        public RequestRouter(final HttpMethod method) {

            this.method = method;

            if (HttpMethod.POST == this.method) {
                router.route().handler(BodyHandler.create().setBodyLimit(allowedSize()));
            }
        }

        public RequestRouter route(final String path) {

            if (path.contains("*")) {
                route = router.routeWithRegex(method, path);

            } else {
                route = router.route(method, path);
            }
            return this;
        }

        public RequestRouter with(final Class controllerClass, final String action) {

            log.debug("Run action " + action + ", controller class " + controllerClass);
            log.debug("Http Method " + method);

            final Controller controller = new Controller(method, controllerClass, action);

            this.route.handler(routingContext -> {
                new Nh2RouteNormalHandler(routingContext).handleWith(controller);
            });

            this.route.failureHandler(failContext -> {
                new Nh2RouteFailureHandler(failContext).failed();
            });

            return this;
        }
    }
}