package ru.doxhost.newhost.server.web.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Eugene Kirin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaTag {

    public static final String META_KEYWORDS = "meta-keywords";

    public static final String META_DESCRIPTION = "meta-description";

    String[] keywords() default {};

    String description() default "";
}
