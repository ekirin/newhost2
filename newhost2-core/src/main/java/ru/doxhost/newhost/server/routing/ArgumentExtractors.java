package ru.doxhost.newhost.server.routing;

import java.util.Map;

import io.vertx.ext.web.RoutingContext;

import com.google.common.collect.ImmutableMap;
import ru.doxhost.newhost.server.routing.parser.IContentTypeParser;


public class ArgumentExtractors {

    private static final Map<Class<?>, ArgumentExtractor<?>> STATIC_EXTRACTORS =
        ImmutableMap.<Class<?>, ArgumentExtractor<?>>builder()
                .put(RoutingContext.class, new ContextExtractor())
                .build();

    public static ArgumentExtractor<?> getExtractorForType(Class<?> type) {
        return STATIC_EXTRACTORS.get(type);
    }

    public static class ContextExtractor implements ArgumentExtractor<RoutingContext> {
        @Override
        public RoutingContext extract(RoutingContext context) {
            return context;
        }

        @Override
        public Class<RoutingContext> getExtractedType() {
            return RoutingContext.class;
        }

        @Override
        public String getFieldName() {
            return null;
        }
    }

    public static class PathParamExtractor implements ArgumentExtractor<String> {
        private final String key;

        public PathParamExtractor(PathParam pathParam) {
            this.key = pathParam.value();
        }

        @Override
        public String extract(RoutingContext context) {
            return context.request().getParam(key);
        }

        @Override
        public Class<String> getExtractedType() {
            return String.class;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class ParamExtractor implements ArgumentExtractor<String> {

        private final String key;

        public ParamExtractor(Param param) {
            this.key = param.value();
        }

        @Override
        public String extract(RoutingContext context) {
            return context.request().getParam(key);
        }

        @Override
        public Class<String> getExtractedType() {
            return String.class;
        }

        @Override
        public String getFieldName() {
            return key;
        }
    }

    public static class EmptyExtractor implements ArgumentExtractor<Void> {

        @Override
        public Void extract(RoutingContext context) {
            return null;
        }

        @Override
        public Class<Void> getExtractedType() {
            return null;
        }

        @Override
        public String getFieldName() {
            return null;
        }
    }

    public static class ContentTypeExtractor implements ArgumentExtractor<Object> {

        private final Class cl;
        private final IValidator validator;

        public ContentTypeExtractor(final Class cl, final IValidator validator) {
            this.cl = cl;
            this.validator = validator;
        }

        @Override
        public Object extract(RoutingContext context) {

            Object result = IContentTypeParser.create(context).parse(cl);

            if (validator != null) {
                validator.validate(result);
            }

            return result;
        }

        @Override
        public Class<Object> getExtractedType() {
            return null;
        }

        @Override
        public String getFieldName() {
            return null;
        }
    }
}