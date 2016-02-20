
package ru.doxhost.newhost.server.routing.jaxy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for specifying a controller path or a controller method path.
 * @author Eugene Kirin
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Path {

    String[] value();

    boolean menuItem() default false;

    String menuItemLabel() default "";

    boolean underLine() default false;

    String header() default "";
}