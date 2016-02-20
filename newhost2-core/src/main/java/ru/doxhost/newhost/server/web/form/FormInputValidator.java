package ru.doxhost.newhost.server.web.form;

import com.google.common.base.CaseFormat;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import javax.validation.*;
import java.util.Set;

/**
 * @author Eugene Kirin on 28.10.2015.
 */
public class FormInputValidator {

    public static Validator validate(Object form) {
        return Validation.byDefaultProvider().configure().messageInterpolator(new ResourceBundleMessageInterpolator(
                        new PlatformResourceBundleLocator(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, form.getClass().getSimpleName()))
                )
        ).buildValidatorFactory().getValidator();
    }

    public static FormValidation validate1(Object form) {
        Validator validator = validate(form);
        Set<ConstraintViolation<Object>> validate = validator.validate(form);

        FormValidation formValidation = new FormValidation();

        for (ConstraintViolation<Object> formConstraintViolation : validate) {

            String message = formConstraintViolation.getMessage();
            String field = formConstraintViolation.getPropertyPath().iterator().next().getName();

            formValidation.add(field, message);
        }

        return formValidation;
    }
}
