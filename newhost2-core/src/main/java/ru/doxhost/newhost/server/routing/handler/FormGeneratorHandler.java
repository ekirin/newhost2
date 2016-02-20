package ru.doxhost.newhost.server.routing.handler;

import io.vertx.ext.web.RoutingContext;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.doxhost.newhost.server.config.Nh2Config;
import ru.doxhost.newhost.server.lib.Nh2Checksum;
import ru.doxhost.newhost.server.core.Nh2Params;
import ru.doxhost.newhost.server.web.form.FormGeneratorException;
import ru.doxhost.newhost.server.web.form.gen.FormGenerator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 *
 * @author Eugene Kirin
 */
public class FormGeneratorHandler implements IFormGeneratorHandler {

    private Map<String, Object> foundForms = new HashMap<>();

    @Override
    public void handle(RoutingContext routingContext) {

        String formGen = routingContext.request().getParam(Nh2Params.PARAM_FORM_GEN);

        if (formGen != null) {
            try {
                if (FormGenerator.MODE_AUTO.equals(formGen)) {

                    String path = routingContext.request().path();

                    String staticFile = path.substring(path.lastIndexOf("/") + 1, path.length());

                    String form = staticFile.substring(0, staticFile.indexOf("."));

                    if (!FormGenerator.exist(form)) {

                        List<ClassLoader> classLoadersList = new LinkedList<>();
                        classLoadersList.add(ClasspathHelper.contextClassLoader());
                        classLoadersList.add(ClasspathHelper.staticClassLoader());

                        Reflections reflections = new Reflections(new ConfigurationBuilder()
                                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(Nh2Config.packageForm()))));

                        Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

                        Class<?> formClass = allClasses.stream()
                                .filter(c -> c.getSimpleName().equalsIgnoreCase(form))
                                .findFirst().get();

                        foundForms.put(form, formClass);

                        foundForms.put(checksumKey(form), Nh2Checksum.md5Sum(formClass));

                        FormGenerator.gen(formClass.newInstance());

                    } else {

                        // TODO - hotswap changed form is more difficult. Impl later

                        //String sum = NHChecksum.md5Sum((Class) foundForms.get(form));

                        /*if (!foundForms.get(checksumKey(form)).equals(sum)) {

                            Class aClass = (Class) foundForms.get(form);

                            new ReloadLoader().loadClass(aClass.getName());

                            Class<?> reload = ClassLoader.getSystemClassLoader().loadClass(aClass.getName());

                            FormGenerator.gen(reload.newInstance());

                            foundForms.replace(form, reload);
                            foundForms.replace(checksumKey(form), sum);
                        }*/
                    }
                }

            } catch (IllegalAccessException|NoSuchAlgorithmException|IOException|InstantiationException|URISyntaxException e) {
                throw new FormGeneratorException(e);
            }
        }

        routingContext.next();
    }

    public static String checksumKey(String prefix) {
        return prefix + "-checksum";
    }
}
