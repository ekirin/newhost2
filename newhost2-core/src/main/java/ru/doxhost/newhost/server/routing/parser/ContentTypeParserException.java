package ru.doxhost.newhost.server.routing.parser;

/**
 * @author Eugene Kirin on 13.11.2015.
 */
public class ContentTypeParserException extends RuntimeException {

    public ContentTypeParserException() {
    }

    public ContentTypeParserException(String message) {
        super(message);
    }

    public ContentTypeParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
