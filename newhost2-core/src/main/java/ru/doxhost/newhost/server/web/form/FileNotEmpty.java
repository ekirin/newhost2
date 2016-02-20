package ru.doxhost.newhost.server.web.form;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Eugene Kirin
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FormFileConstrainValidator.class)
@Documented
public @interface FileNotEmpty {

    String message() default "File is empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}