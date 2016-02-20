package ru.doxhost.newhost.server.routing;

/**
 *
 * @author Eugene Kirin on 12.10.2015.
 */
public class ResultException extends RuntimeException {

    public ResultException() {
    }

    public ResultException(String message) {
        super(message);
    }

    public ResultException(String message, Throwable cause) {
        super(message, cause);
    }
}
