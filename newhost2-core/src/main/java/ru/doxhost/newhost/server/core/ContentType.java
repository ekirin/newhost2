package ru.doxhost.newhost.server.core;

import io.vertx.core.http.HttpServerRequest;

/**
 * @author Eugene Kirin on 09.10.2015.
 */
public enum ContentType {

    JSON("application/json"),

    TEXT("text/plain"),

    HTML("text/html"),

    FORM("application/x-www-form-urlencoded"),

    FORM_UPLOAD("multipart/form-data");

    private String typeString;

    ContentType(String typeString) {
        this.typeString = typeString;
    }

    public String toString() {
        return typeString;
    }

    public static boolean isFormContentType(HttpServerRequest request) {
        String type = request.getHeader("Content-Type");
        return FORM.toString().equals(type) || type.contains(FORM_UPLOAD.toString());
    }

    public static boolean isJsonContentType(HttpServerRequest request) {
        String type = request.getHeader("Content-Type");
        return JSON.toString().equals(type);
    }
}
