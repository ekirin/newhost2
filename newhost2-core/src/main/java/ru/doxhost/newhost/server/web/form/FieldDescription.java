package ru.doxhost.newhost.server.web.form;

import ru.doxhost.newhost.server.lib.Nh2MessageResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Eugene Kirin on 11.11.2015.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDescription {

    String label() default "";

    String placeholder() default "";

    String helper() default "";

    String description() default "";

    /**
     * Form field type
     * @return
     */
    String type() default Nh2MessageResolver.RESOLVE;
}
