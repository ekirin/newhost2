package ru.doxhost.newhost.server.routing;

import io.vertx.ext.web.RoutingContext;

public interface ArgumentExtractor<T> {
    /**
     * Extract the argument from the context
     *
     * @param context The argument to extract
     * @return The extracted argument
     */
    T extract(RoutingContext context);

    /**
     * Get the type of the argument that is extracted
     *
     * @return The type of the argument that is being extracted
     */
    Class<T> getExtractedType();

    /**
     * Get the field name that is being extracted, if this value is
     * extracted from a field
     *
     * @return The field name, or null if the argument isn't extracted
     *         from a named field
     */
    String getFieldName();
}
