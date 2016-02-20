package ru.doxhost.newhost.server.routing.parser;

import com.google.common.collect.Sets;
import io.vertx.core.MultiMap;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author Eugene Kirin on 27.10.2015.
 */
public class POJOParser {

    private Logger logger = LoggerFactory.getLogger(POJOParser.class);

    public <T> T parse(final RoutingContext context, final Class<T> cl) {

        if (cl == null) {
            return (T) "";
        }

        T t = null;

        try {
            t = cl.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Can't create new instance of class {}" + cl.getName(), e);
            return null;
        }

        Set<String> declaredFields = getAllDeclaredFieldsAsStringSet(cl);

        MultiMap entries = context.request().formAttributes();

        final T finalT = t;

        context.fileUploads().stream().forEach(entry -> {
            if (declaredFields.contains(entry.name())){
                try {
                    Field field = cl.getDeclaredField(entry.name());
                    field.setAccessible(true);
                    field.set(finalT, entry);
                } catch (NoSuchFieldException | IllegalAccessException e ) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        });

        entries.iterator().forEachRemaining(entry -> {

            if (declaredFields.contains(entry.getKey())) {
                try {
                    Field field = cl.getDeclaredField(entry.getKey());

                    field.setAccessible(true);

                    String value = entry.getValue();

                    Object convertedValue = NH2ParserHelper.convert(value, field.getType());

                    if (convertedValue != null) {
                        field.set(finalT, convertedValue);
                    }

                } catch (NoSuchFieldException
                        | SecurityException
                        | IllegalArgumentException
                        | IllegalAccessException e) {

                    logger.warn("Error parsing incoming Post request into class {}. Key {} and value {}." +
                            cl.getName() + entry.getKey() + entry.getValue(), e);
                }
            }
        });

        return t;
    }

    private <T> Set<String> getAllDeclaredFieldsAsStringSet(Class<T> clazz) {

        Set<String> declaredFields = Sets.newHashSet();

        for (Field field : clazz.getDeclaredFields()) {
            declaredFields.add(field.getName());
        }

        return declaredFields;

    }
}
