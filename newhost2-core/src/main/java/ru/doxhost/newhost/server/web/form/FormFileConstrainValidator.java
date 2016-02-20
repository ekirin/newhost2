package ru.doxhost.newhost.server.web.form;

import io.vertx.ext.web.FileUpload;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Eugene Kirin
 */
public class FormFileConstrainValidator implements ConstraintValidator<FileNotEmpty, FileUpload> {

    @Override
    public void initialize(FileNotEmpty fileNoEmpty) {

    }

    @Override
    public boolean isValid(FileUpload fileUpload, ConstraintValidatorContext constraintValidatorContext) {
        return !(fileUpload == null || fileUpload.fileName() == null || fileUpload.fileName().length() == 0);
    }
}
