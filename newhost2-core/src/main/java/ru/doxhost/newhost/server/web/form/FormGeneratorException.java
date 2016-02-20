package ru.doxhost.newhost.server.web.form;

/**
 * @author Eugene Kirin on 17.11.2015.
 */
public class FormGeneratorException extends RuntimeException {

    public FormGeneratorException() {
    }

    public FormGeneratorException(String message) {
        super(message);
    }

    public FormGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormGeneratorException(Throwable cause) {
        super(cause);
    }
}
