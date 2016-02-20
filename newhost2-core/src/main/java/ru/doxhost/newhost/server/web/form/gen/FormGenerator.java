package ru.doxhost.newhost.server.web.form.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.FileUpload;
import ru.doxhost.newhost.server.config.Nh2Config;
import ru.doxhost.newhost.server.lib.Nh2MessageResolver;
import ru.doxhost.newhost.server.lib.Nh2Path;
import ru.doxhost.newhost.server.web.form.FieldDescription;
import ru.doxhost.newhost.server.web.form.FormGeneratorException;
import ru.doxhost.newhost.server.web.form.Submit;

import java.io.IOException;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;

import static org.reflections.ReflectionUtils.withAnnotation;
import static ru.doxhost.newhost.server.lib.Nh2Annotation.getAnnotation;

/**
 * @author Eugene Kirin on 10.11.2015.
 */
public class FormGenerator {

    public static final String PAGE_LANG = "lang-form.html";

    public static final String MODE_AUTO = "auto";

    public static final String FIELD_TYPE_INPUT = "input-string";

    public static final String FIELD_TYPE_INPUT_FILE = "input-file";

    public static final String HTML_FOLDER = "static/html";

    public static String gen(Object bean) throws URISyntaxException, IOException {

        String gen = "";

        Field[] declaredFields = bean.getClass().getDeclaredFields();

        Set<Field> fields = new LinkedHashSet<>(Arrays.asList(declaredFields));
        
        StringBuilder re = new StringBuilder();

        Locale current = Locale.getDefault(); // save current because LocaleFormGenerator.asJson() switch locale.

        String allLocale = FormGeneratorLocale.asJson(bean);

        Locale.setDefault(current);

        re.append(MessageFormat.format(readLocalizationInclude(PAGE_LANG), allLocale)).append("\n").
                append(readJS(Nh2Config.appStyle())).append("\n");

        final boolean[] hasFileField = new boolean[1];

        fields.stream().forEach(field -> {

            try {

                String fieldName = field.getName().toUpperCase();

                String form = bean.getClass().getSimpleName().toLowerCase();

                String label = Nh2MessageResolver.get(form, "LABEL." + form.toUpperCase() + "." + fieldName);

                String formField = null;

                String placeHolder = "";

                if (field.isAnnotationPresent(FieldDescription.class)) {

                    formField = field.getAnnotation(FieldDescription.class).type();

                    if (formField.contains(Nh2MessageResolver.RESOLVE)) {
                        formField = getFormFieldByType(field.getType());
                    }

                    if (field.getAnnotation(FieldDescription.class).placeholder().equals(Nh2MessageResolver.RESOLVE)) {
                        placeHolder = "LABEL." + form.toUpperCase() + "." + "PLACEHOLDER";
                    }

                    placeHolder = Nh2MessageResolver.get(form, placeHolder);


                } else {
                    formField = getFormFieldByType(field.getType());
                }

                if (formField == null) {
                    throw new FormGeneratorException("Couldn't detect form field type for bean property \"" + field.getName() + "\"");
                }

                if (!hasFileField[0]) {
                    hasFileField[0] = FIELD_TYPE_INPUT_FILE.equals(formField);
                }

                String fieldRes = HTML_FOLDER + "/" + new StringJoiner("/").add(Nh2Config.appStyle()).add(formField + ".html");

                Optional<URL> formFieldResource = Optional.ofNullable(FormGenerator.class.getClassLoader().getResource(fieldRes));

                String rs = fragment(
                        formFieldResource.orElseThrow(() -> new FormGeneratorException("Couldn't find resource " + fieldRes)
                        ).toURI(), field.getName() + "-lbl", field.getName(), label, placeHolder);

                re.append(rs).append("\n");

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        String submitAction = bean.getClass().getSimpleName();

        if(withAnnotation(Submit.class).apply(bean.getClass())) {
            submitAction = getAnnotation(bean.getClass(), Submit.class).action();
        }

        String enctype= "";

        if (hasFileField[0]) {
            enctype = "multipart/form-data";
        }

        gen = re.insert(0, "<form method=\"post\" action=\""+ submitAction +"\" enctype=\"" + enctype +"\">\n")
                .append("<button id=\"submit\" type=\"submit\" class=\"btn btn-primary\">submit</button>")
                .append("\n</form>").toString();

        URL htmlFolder = FormGenerator.class.getClassLoader().getResource(HTML_FOLDER);

        //Path path = Paths.get(htmlFolder.toURI().getRawPath().replaceFirst("/", "") + "/" + bean.getClass().getSimpleName() + ".html");
        Path path = Nh2Path.obtainPath(HTML_FOLDER + "/" + bean.getClass().getSimpleName(), true);

        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        Files.write(path, gen.getBytes(StandardCharsets.UTF_8));

        return gen;
    }

    public static String fragment(URI uri, String... param) throws URISyntaxException, IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(uri));

        String input = new String(encoded, StandardCharsets.UTF_8).intern();

        return MessageFormat.format(input, param);
    }

    public static void main(String[] ar) throws IOException, URISyntaxException {

        String data = "{\"name\": \"David\",\n" +
                "  \"role\": \"Manager\",\n" +
                "  \"city\": \"Los Angeles\"\n}";

        Map<String,String> myMap;

        ObjectMapper objectMapper = new ObjectMapper();
        myMap = objectMapper.readValue(data, HashMap.class);
        System.out.println("Map is: "+myMap);


        //gen(new Form());
    }

    public static String readJS(String tmpl) throws URISyntaxException, IOException {

        String fieldRes = HTML_FOLDER + "/" + tmpl + "/form-js.html";

        Optional<URL> formFieldResource = Optional.ofNullable(FormGenerator.class.getClassLoader().getResource(fieldRes));

        byte[] bytes = Files.readAllBytes(Paths.get(
                formFieldResource.orElseThrow(() -> new FormGeneratorException("Couldn't find resource " + fieldRes)).toURI())
        );

        return new String(bytes).intern();
    }

    public static String readLocalizationInclude(String name) throws URISyntaxException, IOException {

        String fieldRes = HTML_FOLDER + "/" + Nh2Config.appStyle() + "/" + name;

        Optional<URL> formFieldResource = Optional.ofNullable(FormGenerator.class.getClassLoader().getResource(fieldRes));

        byte[] bytes = Files.readAllBytes(Paths.get(
                        formFieldResource.orElseThrow(() -> new FormGeneratorException("Couldn't find resource " + fieldRes)).toURI())
        );

        return new String(bytes).intern();
    }

    private static String getFormFieldByType(Type fieldType) {

        String formField = null;

        if (String.class.getName().equals(fieldType.getTypeName())) {
            formField = FIELD_TYPE_INPUT;
        }

        if (FileUpload.class.getName().equals(fieldType.getTypeName())) {
            formField = FIELD_TYPE_INPUT_FILE;
        }

        return formField;
    }

    public static boolean exist(String element) {
        return FormGenerator.class.getClassLoader().getResource(HTML_FOLDER + "/" + element + ".html") != null;
    }
}
