package ru.doxhost.newhost.server.core;

/**
 * @author Eugene Kirin
 */
public class Nh2Params {

    public static final String PARAM_LANG = "lang";

    public static final String PARAM_FORM_GEN = "mode";

    public static String extractKey(String rawKey) {

        if (rawKey.contains("{") && rawKey.contains("}")) {
            return rawKey.substring(1, rawKey.length() - 1);

        } else {
            return rawKey;
        }
    }
}
