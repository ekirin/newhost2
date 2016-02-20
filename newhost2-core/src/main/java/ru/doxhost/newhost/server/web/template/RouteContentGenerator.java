package ru.doxhost.newhost.server.web.template;

import ru.doxhost.newhost.server.config.Nh2Config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Optional;

import static ru.doxhost.newhost.server.lib.Nh2Path.obtainPath;

/**
 * It used to generate a content file on the fly when request is sent. Available in dev mode.<br/>
 *
 * @author Eugene Kirin
 */
public class RouteContentGenerator implements IRouteContentGenerator {

    public static final String FOLDER = "stencil";

    public static final String STENCIL_FILE = "routeContent.html";

    private String templateFolder;

    {
        Optional<String> optional = Optional.ofNullable(System.getProperty(ThymeleafEngine.TEMPLATE_THYMELEAF_FOLDER_PARAM));
        templateFolder = ThymeleafEngine.templateFolder(optional);
    }

    @Override
    public void gen(String fileName) {
        try {
            Path contentFile = obtainPath(fileName, false);

            if (!Files.exists(contentFile)) {

                if (Nh2Config.getConf().isDev()) {
                    templateFolder = ThymeleafEngine.templateFolder(Optional.ofNullable(null));
                }

                Path stencilFile = obtainPath(templateFolder + FOLDER + "/" + STENCIL_FILE, false);

                if (Files.exists(stencilFile)) {

                    String result = MessageFormat.format(
                            new String(Files.readAllBytes(stencilFile), StandardCharsets.UTF_8).intern(), fileName);

                    Files.write(contentFile, result.getBytes());

                } else {
                    throw new RouteContentGeneratorException("Cann't find the route content file template "
                            + templateFolder + FOLDER + "/" + STENCIL_FILE);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RouteContentGeneratorException(e);
        }
    }
}
