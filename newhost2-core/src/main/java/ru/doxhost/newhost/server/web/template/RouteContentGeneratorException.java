package ru.doxhost.newhost.server.web.template;

/**
 * @author Eugene Kirin
 */
public class RouteContentGeneratorException extends RuntimeException {
    public RouteContentGeneratorException() {
    }

    public RouteContentGeneratorException(String message) {
        super(message);
    }

    public RouteContentGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouteContentGeneratorException(Throwable cause) {
        super(cause);
    }
}
