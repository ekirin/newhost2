package ru.doxhost.newhost.server.routing.parser;

import io.vertx.ext.web.RoutingContext;
import ru.doxhost.newhost.server.core.ContentType;

/**
 *
 * @author Eugene Kirin on 13.11.2015.
 */
public class PostContentTypeParser implements IContentTypeParser {

    private final RoutingContext context;

    public PostContentTypeParser(final RoutingContext context) {
        this.context = context;
    }

    @Override
    public Object parse(final Class cl) {

        Object result = null;

        if (ContentType.isFormContentType(context.request())) {
            result = new POJOParser().parse(context, cl);
        }

        if (ContentType.isJsonContentType(context.request())) {
            throw new ContentTypeParserException("For Json not implemented yet.");
        }

        return result;
    }
}
