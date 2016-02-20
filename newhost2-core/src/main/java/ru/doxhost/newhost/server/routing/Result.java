package ru.doxhost.newhost.server.routing;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import ru.doxhost.newhost.server.core.ContentType;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static ru.doxhost.newhost.server.core.ContentType.JSON;

/**
 * Result from Controller
 * @author Eugene Kirin on 12.10.2015.
 */
public class Result {

    private ContentType type = ContentType.HTML;

    private Object data;

    public static Result OK() {
        return new Result();
    }

    public static Result JSON() {
        Result result = new Result();
        result.type = ContentType.JSON;
        return result;
    }

    public static Result TEXT() {
        Result result = new Result();
        result.type = ContentType.TEXT;
        return result;
    }

    public static Result HTML() {
        Result result = new Result();
        result.type = ContentType.HTML;
        return result;
    }

    public static Result FORM() {
        Result result = new Result();
        result.type = ContentType.FORM;
        return result;
    }

    public Result display(Object data) {
        this.data = data;
        return this;
    }

    /**
     *
     * @return result html
     */
    public boolean isHtml() {
        return ContentType.HTML.equals(getType());
    }

    /**
     *
     * @return Result from form submit
     */
    public boolean isForm() {
        return ContentType.FORM.equals(getType());
    }

    /**
     *
     * @return Result in text format
     */
    public boolean isText() {
        return ContentType.TEXT.equals(getType());
    }

    public boolean isJson() {
        return ContentType.JSON.equals(getType());
    }

    public Object getData() {
        return data;
    }

    public Map<String, Object> getDataAsMap() {
        return data instanceof Map ? (Map) data : Collections.EMPTY_MAP;
    }

    public ContentType getType() {
        return type;
    }

    public void bindWithContext(RoutingContext routingContext) {

        Object data = getData();

        if (data instanceof Map) {
            Set<Map.Entry<String, Object>> entries = getDataAsMap().entrySet();

            for (Map.Entry<String, Object> entry : entries) {
                routingContext.put(entry.getKey(), entry.getValue());
            }

        } else if (data != null) {
            String key = data.getClass().getSimpleName().toLowerCase();
            routingContext.put(key, data);
        }
    }

    public String toString() {

        if (data == null) {
            throw new ResultException("Result without content data. Nothing to display.");
        }

        if (type == JSON && data instanceof JsonObject) {
            return ((JsonObject) data).encode();
        }

        return data.toString();
    }
}