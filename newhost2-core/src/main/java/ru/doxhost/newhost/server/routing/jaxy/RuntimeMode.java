
package ru.doxhost.newhost.server.routing.jaxy;

import ru.doxhost.newhost.server.lib.Nh2Mode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RuntimeMode {

    Nh2Mode value();
}