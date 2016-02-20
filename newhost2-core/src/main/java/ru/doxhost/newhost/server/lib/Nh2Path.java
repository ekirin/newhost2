package ru.doxhost.newhost.server.lib;

import ru.doxhost.newhost.server.web.template.ThymeleafEngine;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Eugene Kirin
 */
public class Nh2Path {

    public static Path obtainPath(String pathString, boolean ignoreTemplate) throws URISyntaxException, IOException {

        Optional<String> optional = Optional.ofNullable(System.getProperty(ThymeleafEngine.TEMPLATE_THYMELEAF_FOLDER_PARAM));

        Path path = Paths.get((ignoreTemplate ? optional.get() + "/" : ThymeleafEngine.templateFolder(optional)) + pathString + ThymeleafEngine.SUFFIX);

        if (!Files.exists(path)) {

            URL resource = Nh2Path.class.getClassLoader().getResource(pathString);

            if (resource != null) {
                try {
                    path = Paths.get(resource.toURI());
                } catch (FileSystemNotFoundException e) {
                    final String[] array = resource.toURI().toString().split("!");
                    final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), new HashMap<>());
                    path = fs.getPath(array[1]);
                }
            }
        }
        return path;
    }
}
