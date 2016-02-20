
package ru.doxhost.newhost.server.routing.jaxy;

import ru.doxhost.newhost.server.lib.Nh2Mode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RuntimeMode(Nh2Mode.PROD)
public @interface Prod {
}