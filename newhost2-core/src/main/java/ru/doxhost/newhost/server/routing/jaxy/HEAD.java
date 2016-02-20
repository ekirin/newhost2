
package ru.doxhost.newhost.server.routing.jaxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod(io.vertx.core.http.HttpMethod.HEAD)
public @interface HEAD {
}