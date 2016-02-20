package ru.doxhost.newhost.server.routing;

import io.vertx.ext.web.RoutingContext;
import org.reflections.ReflectionUtils;
import ru.doxhost.newhost.server.lib.Nh2MessageResolver;
import ru.doxhost.newhost.server.core.Controller;
import ru.doxhost.newhost.server.web.meta.MetaTag;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Predicates.and;
import static org.reflections.ReflectionUtils.getAllMethods;
import static ru.doxhost.newhost.server.core.FormSubmit.NH2_FORM_SUBMIT_ERRORS;

/**
 * @author Eugene Kirin on 13.11.2015.
 */
public class SmartControllerInvoker implements ISmartControllerInvoker {

    private final Controller controller;
    private final RoutingContext context;

    public SmartControllerInvoker(final Controller controller, final RoutingContext context) {
        this.controller = controller;
        this.context =context;
    }

    @Override
    public Result invokeAction(IValidator validator) {

        Method controllerAction = getAllMethods(controller.getController(), and(ReflectionUtils.withName(controller.getAction())))
                .stream().findFirst().orElseThrow(
                        () -> new ControllerActionInvokeException("Cann't found " + controller.getAction() + " method for controller " + controller.getController().getName())
                );

        // I don't know maybe it's not the best place for this action.
        if (controllerAction.isAnnotationPresent(MetaTag.class)) {

            String description = controllerAction.getAnnotation(MetaTag.class).description();
            String[] keywords = controllerAction.getAnnotation(MetaTag.class).keywords();

            if (description.length() != 0) {
                description = Nh2MessageResolver.get(controller.getController().getSimpleName(), description);
                context.put(MetaTag.META_DESCRIPTION, description);
            }

            if (keywords.length != 0) {

                StringBuilder sb = new StringBuilder(keywords.length);

                for (String keyword : keywords) {
                    keyword = Nh2MessageResolver.get(controller.getController().getSimpleName(), keyword);
                    sb.append(keyword);
                }

                context.put(MetaTag.META_KEYWORDS, String.join(",", sb.toString()));
            }
        }

        Parameter[] parameters = controllerAction.getParameters();

        Object[] paramValues = new Object[parameters.length];

        List<ArgumentExtractor<?>> extractors = new ArrayList<>(paramValues.length);

        Arrays.stream(parameters).forEach(parameter -> {

            ArgumentExtractor extractor = null;

            Annotation[] declaredAnnotations = parameter.getDeclaredAnnotations();

            if (declaredAnnotations.length == 1) {

                Annotation annotation = declaredAnnotations[0];

                WithArgumentExtractor withArgumentExtractor = annotation.annotationType().getAnnotation(WithArgumentExtractor.class);

                if (withArgumentExtractor != null) {
                    ArgumentExtractor<?> argumentExtractor = instantiateComponent(withArgumentExtractor.value(), annotation, String.class);
                    extractor = argumentExtractor;
                }
            }

            if (parameter.getType().getName().equals(RoutingContext.class.getName())) {
                extractor = ArgumentExtractors.getExtractorForType(RoutingContext.class);
            }

            if (extractor == null) {
                extractor = new ArgumentExtractors.ContentTypeExtractor(parameter.getType(), validator);
            }

            extractors.add(extractor);
        });

        try {
            for (int i=0; i < extractors.size(); i++) {
                paramValues[i] = extractors.get(i).extract(context);
            }

            Result result;

            if (!context.data().containsKey(NH2_FORM_SUBMIT_ERRORS)) { // if form submit has errors then ignore action.
                result = (Result) controllerAction.invoke(controller.getController().newInstance(), paramValues);
            } else {
                result = Result.FORM();
            }

            return result;

        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | IndexOutOfBoundsException e) {
            throw new ControllerActionInvokeException(e);
        }
    }

    private static <T> T instantiateComponent(Class<? extends T> argumentExtractor,
                                              final Annotation annotation, final Class<?> paramType) {
        // Noarg constructor
        Constructor noarg = getNoArgConstructor(argumentExtractor);
        if (noarg != null) {
            try {
                return (T) noarg.newInstance();
            } catch (Exception e) {
                throw new RoutingException(e);
            }
        }
        // Simple case, just takes the annotation
        Constructor simple = getSingleArgConstructor(argumentExtractor, annotation.annotationType());
        if (simple != null) {
            try {
                return (T) simple.newInstance(annotation);
            } catch (Exception e) {
                throw new RoutingException(e);
            }
        }
        // Simple case, just takes the parsed class
        Constructor simpleClass = getSingleArgConstructor(argumentExtractor, Class.class);
        if (simpleClass != null) {
            try {
                return (T) simpleClass.newInstance(paramType);
            } catch (Exception e) {
                throw new RoutingException(e);
            }
        }
        return null;
    }

    private static Constructor getNoArgConstructor(Class<?> clazz) {
        for (Constructor constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                return constructor;
            }
        }
        return null;
    }

    private static Constructor getSingleArgConstructor(Class<?> clazz, Class<?> arg) {
        for (Constructor constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == 1) {
                if (constructor.getParameterTypes()[0].isAssignableFrom(arg)) {
                    return constructor;
                }
            }
        }
        return null;
    }

    private static Class<?> box(Class<?> typeToBox) {
        if (typeToBox == int.class) {
            return Integer.class;
        } else if (typeToBox == boolean.class) {
            return Boolean.class;
        } else if (typeToBox == long.class) {
            return Long.class;
        } else if (typeToBox == float.class) {
            return Float.class;
        } else if (typeToBox == double.class) {
            return Double.class;
        } else if (typeToBox == byte.class) {
            return Byte.class;
        } else if (typeToBox == short.class) {
            return Short.class;
        } else if (typeToBox == char.class) {
            return Character.class;
        }
        throw new IllegalArgumentException("Don't know how to box type of " + typeToBox);
    }
}