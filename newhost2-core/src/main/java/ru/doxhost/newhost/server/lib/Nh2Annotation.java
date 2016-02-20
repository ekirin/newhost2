package ru.doxhost.newhost.server.lib;

import org.reflections.ReflectionUtils;

/**
 * Util class for annotations
 * @author Eugene Kirin
 */
public class Nh2Annotation {

    private Nh2Annotation(){}

    /**
     * Useful to avoid casting
     * @param cl
     * @param annotation
     * @param <T>
     * @return
     */
    @SuppressWarnings(value = "unchecked")
    public static <T extends java.lang.annotation.Annotation> T getAnnotation(Class cl, final Class<T> annotation) {
        return (T) ReflectionUtils.getAnnotations(cl).stream().findFirst().get();
    }
}
