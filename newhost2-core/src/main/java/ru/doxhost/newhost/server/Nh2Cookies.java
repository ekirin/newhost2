package ru.doxhost.newhost.server;

import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.RoutingContext;

import java.util.Locale;
import java.util.Optional;

/**
 * @author Eugene Kirin
 */
public final class Nh2Cookies {

    private Nh2Cookies() {
    }

    /**
     * It's mainly to support error messages for form submit.
     */
    public static final String NH2_COOKIE_HTTP_REFERER = "nh2_http_referer";
    public static final String NH2_COOKIE_USER_LOCALE = "nh2_user_locale";

    /**
     * In the most common situation this means that when a user clicks a hyperlink in a web browser,
     * the browser sends a request to the server holding the destination webpage.
     * The request includes the referrer field, which indicates the last page the
     * user was on (the one where they clicked the link) (From Wikipedia).<BR/>
     * Use cookies because Referer Header maybe hidden or not available over HTTPS.
     * @return
     */
    public static String httpReferer(final RoutingContext routingContext) {
        Optional<Cookie> optional = Optional.ofNullable(routingContext.getCookie(NH2_COOKIE_HTTP_REFERER));
        return optional.isPresent() ? optional.get().getValue() : null;
    }

    public static void saveHttpReferer(final RoutingContext routingContext, final String path) {
        if (httpReferer(routingContext) == null) {
            routingContext.addCookie(Cookie.cookie(NH2_COOKIE_HTTP_REFERER, path));

        } else {
            routingContext.getCookie(NH2_COOKIE_HTTP_REFERER).setValue(path);
        }
    }

    public static String userLocale(final RoutingContext routingContext) {
        Optional<Cookie> optional = Optional.ofNullable(routingContext.getCookie(NH2_COOKIE_USER_LOCALE));
        return optional.isPresent() ? optional.get().getValue() : null;
    }

    public static void saveUserLocale(final RoutingContext routingContext) {
        if (userLocale(routingContext) == null) {
            routingContext.addCookie(Cookie.cookie(NH2_COOKIE_USER_LOCALE, Locale.getDefault().getLanguage()));

        } else {
            routingContext.getCookie(NH2_COOKIE_USER_LOCALE).setValue(Locale.getDefault().getLanguage());
        }
    }
}
