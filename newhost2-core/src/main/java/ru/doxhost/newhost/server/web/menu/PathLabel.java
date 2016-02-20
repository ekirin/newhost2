package ru.doxhost.newhost.server.web.menu;

import ru.doxhost.newhost.server.routing.jaxy.Path;
import ru.doxhost.newhost.server.lib.Nh2MessageResolver;

import java.util.StringJoiner;

/**
 * @author Eugene Kirin
 */
public class PathLabel {

    /**
     * Returns label by key. If key = {@code MessageResolver.RESOLVE} then the key will be build by pattern {@code LABEL.${name}.${pathItem}}.
     * If path item = "/" the key will be {@code LABEL.${name}.INDEX}
     * @param path menuItem
     * @param base Bundle base
     * @return menu item label
     */
    public static String label(final Path path, final String base, final String name) {

        String itemLabel = path.menuItemLabel();

        String index = ".index";

        if (itemLabel == null || itemLabel.length() == 0) {
            String val = path.value()[0].equals("/") ? index : path.value()[0].replace("/", ".");
            itemLabel = (new StringJoiner(".").add("label").add(name).toString() + val).toUpperCase();
        }

        if (Nh2MessageResolver.RESOLVE.equals(itemLabel)) {

            String key = path.value()[0];

            if ("/".equals(path.value()[0])) {
                key = index;

            } else {
                key = key.replace("/", ".");
            }

            itemLabel = (new StringJoiner(".").add("label").add(name).toString() + key).toUpperCase();
        }

        return Nh2MessageResolver.get(base + "." + name, itemLabel);
    }

    public static String label(final String item, final String base, final String name) {
        String labelKey = buildKey(item, base, name);
        return Nh2MessageResolver.get(base + "." + name, labelKey);
    }

    public static String buildKey(final String item, final String base, final String name) {
        return new StringJoiner(".").add("label").add(name).add(item).toString().toUpperCase();
    }
}
