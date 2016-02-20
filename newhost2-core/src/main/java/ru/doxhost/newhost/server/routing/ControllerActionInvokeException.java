package ru.doxhost.newhost.server.routing;

/**
 * @author Eugene Kirin on 13.11.2015.
 */
public class ControllerActionInvokeException extends RuntimeException {

    public ControllerActionInvokeException() {
    }

    public ControllerActionInvokeException(String message) {
        super(message);
    }

    public ControllerActionInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerActionInvokeException(Throwable cause) {
        super(cause);
    }
}
