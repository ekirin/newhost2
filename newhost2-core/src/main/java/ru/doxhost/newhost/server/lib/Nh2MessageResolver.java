package ru.doxhost.newhost.server.lib;

import com.google.common.base.CaseFormat;

import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Eugene Kirin
 */
public final class Nh2MessageResolver {

    /**
     * Resolve bundle key by pattern {@code  BeanClassName + "." + }
     */
    public static final String RESOLVE = "{RESOLVE}";

    public static String get(String base, String key) {

        base = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, base);

        ResourceBundle bundle = PropertyResourceBundle.getBundle(base);

        String message = null;

        if (bundle != null) {
            try {
                if (key.contains("{") && key.contains("}")) {
                    key = key.substring(1, key.length() - 1);;
                }
                message = bundle.getString(key);
            } catch (MissingResourceException m) {
                message = key;
            }
        }
        return message;
    }
}
