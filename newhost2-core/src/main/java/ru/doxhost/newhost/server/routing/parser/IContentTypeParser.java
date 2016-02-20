package ru.doxhost.newhost.server.routing.parser;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;

/**
 * @author Eugene Kirin on 13.11.2015.
 */
public interface IContentTypeParser {

    static IContentTypeParser create(final RoutingContext context) {

        Optional<IContentTypeParser> optional = Optional.empty();

        HttpMethod httpMethod = context.request().method();

        if (HttpMethod.POST == httpMethod) {
            optional =  Optional.ofNullable(new PostContentTypeParser(context));
        }

        return optional.orElseThrow(
                () -> new ContentTypeParserException("No suitable Content-Type parser found for " + httpMethod.name() + " http method.")
        );
    }

    Object parse(final Class cl);
}
