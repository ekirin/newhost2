package ru.doxhost.newhost.server.web.menu;

/**
 * @author Eugene Kirin
 */
public class MenuHandlerException extends RuntimeException {

    public MenuHandlerException() {
    }

    public MenuHandlerException(String message) {
        super(message);
    }

    public MenuHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuHandlerException(Throwable cause) {
        super(cause);
    }
}
