package ru.doxhost.newhost.server.web.template;

import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import ru.doxhost.newhost.server.config.Nh2Config;

import java.util.Optional;
import java.util.StringJoiner;

/**
 *
 * @author Eugene Kirin on 03.11.2015.
 */
public class ThymeleafEngine {

    /**
     * For development mode (disable cache).
     * <br/>Example: <br/>
     * -DthymeleafTemplateFolder=E:/var/gits/newhost/src/main/webapp
     * <br/>Also need set -Dvertx.disableFileCaching=true
     */
    public static final String TEMPLATE_THYMELEAF_FOLDER_PARAM = "thymeleafTemplateFolder";

    public static final String SUFFIX = ".html";

    /**
     * Where templates files are.
     */
    public static final String TEMPLATE_DEFAULT_FOLDER = "templates";

    private ThymeleafEngine(){}

    public static TemplateEngine init() {
        TemplateEngine engine = ThymeleafTemplateEngine.create();

        org.thymeleaf.TemplateEngine thymeleafTemplateEngine = ((ThymeleafTemplateEngine) engine).getThymeleafTemplateEngine();

        thymeleafTemplateEngine.setMessageResolver(new ThymeleafMessageResolver());

        Optional<String> optional = Optional.ofNullable(System.getProperty(TEMPLATE_THYMELEAF_FOLDER_PARAM));

        for (ITemplateResolver iTemplateResolver : thymeleafTemplateEngine.getTemplateResolvers()) {
            ((TemplateResolver) iTemplateResolver).setTemplateMode("HTML5");
            ((TemplateResolver) iTemplateResolver).setCacheable(!optional.isPresent());
            ((TemplateResolver) iTemplateResolver).setPrefix(templateFolder(optional));
            ((TemplateResolver) iTemplateResolver).setSuffix(SUFFIX);
        }

        return engine;
    }

    public static String templateFolder(Optional<String> optional) {
        return optional.isPresent()
                ? new StringJoiner("/").add(optional.get()).add(TEMPLATE_DEFAULT_FOLDER).add(Nh2Config.appStyle()).add("").toString()
                : TEMPLATE_DEFAULT_FOLDER + "/" + Nh2Config.appStyle() + "/";
    }
}