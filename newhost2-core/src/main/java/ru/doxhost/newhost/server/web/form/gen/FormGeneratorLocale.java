package ru.doxhost.newhost.server.web.form.gen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.doxhost.newhost.server.lib.Nh2MessageResolver;

import ru.doxhost.newhost.server.web.form.FieldDescription;
import ru.doxhost.newhost.server.web.form.FormAttr;

import java.lang.reflect.Field;
import java.util.*;

import static ru.doxhost.newhost.server.lib.Nh2MessageResolver.RESOLVE;
import static ru.doxhost.newhost.server.web.form.FormAttr.*;

/**
 * @author Eugene Kirin on 11.11.2015.
 */
public class FormGeneratorLocale {

    private FormGeneratorLocale() {}

    private static String[] supportedLocale = new String[] {"ru", "en"};

    public static String asJson(Object bean) {

        final String base = bean.getClass().getSimpleName().toLowerCase();

        ObjectNode jsonNodes = new ObjectMapper().createObjectNode();

        for (String locale : supportedLocale) {

            ObjectNode lang = jsonNodes.putObject(locale);

            Set<Field> fields = new LinkedHashSet<>(Arrays.asList(bean.getClass().getDeclaredFields()));

            fields.stream().filter(f -> f.isAnnotationPresent(FieldDescription.class)).forEach(field -> {

                FieldDescription description = field.getAnnotation(FieldDescription.class);

                ObjectNode langField = lang.putObject(field.getName());

                saveFormAttrLocale(locale, langField, LABEL, description.label(), base, field.getName());
                saveFormAttrLocale(locale, langField, DESCRIPTION, description.description(), base, field.getName());
                saveFormAttrLocale(locale, langField, PLACEHOLDER, description.placeholder(), base, field.getName());
                saveFormAttrLocale(locale, langField, HELPER, description.helper(), base, field.getName());

                ObjectNode submitBtn = lang.putObject("btn").putObject("submit");

                saveFormAttrLocale(locale, submitBtn, LABEL, RESOLVE, base, "submit");
            });
        }

        String result = jsonNodes.toString();

        try {
            result = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonNodes).toString();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String saveFormAttrLocale(String lng, ObjectNode lang, FormAttr attr, String value, String base, String fieldName) {

        Optional<String> lng1 = Optional.ofNullable(lng);

        if (lng1.isPresent()) {
            Locale.setDefault(Locale.ENGLISH.equals(lng1.get()) ? Locale.ENGLISH : new Locale(lng1.get()));
        }

        if (value.contains("{") && value.contains("}")) {

            if (RESOLVE.equals(value)) {
                value = new StringJoiner(".").add(attr.name()).add(base).add(fieldName).toString().toUpperCase();

            } else {

                value = value.substring(1, value.length() - 1);
            }
        }

        lang.put(attr.name().toLowerCase(), Nh2MessageResolver.get(base, value));

        return value;
    }
}