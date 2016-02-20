
package ru.doxhost.newhost.server.routing.jaxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows specifying route registration order. Lower numbers are registered
 * before higher numbers.
 *
 * @author Eugene Kirin
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    int value();
}