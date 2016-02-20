package ru.doxhost.newhost.server.config;

import ru.doxhost.newhost.server.lib.Nh2MessageResolver;
import ru.doxhost.newhost.server.lib.Nh2ModeHelper;
import ru.doxhost.newhost.server.core.Nh2Params;

import java.util.Locale;
import java.util.StringJoiner;

/**
 * All available config params
 * @author Eugene Kirin
 */
public final class Nh2Config {

    private static final Nh2Config instance = new Nh2Config();

    private final Nh2Properties NH2Properties = new Nh2PropertiesImpl(Nh2ModeHelper.determineModeFromSystemPropertiesOrProdIfNotSet());

    private Nh2Config(){}

    public static Nh2Properties getConf() {
        return instance.NH2Properties;
    }

    public static final String KEY_APP = "app";

    /**
     * Which template to use.
     */
    public static final String KEY_APP_STYLE = new StringJoiner(".").add(KEY_APP).add("style").toString();;

    /**
     * Where to find stuff for generation or else.
     */
    public static final String KEY_APP_PACKAGE = new StringJoiner(".").add(KEY_APP).add("package").toString();;

    /**
     * Form generator will use this package to search form classes to generate forms.
     */
    public static final String KEY_APP_PACKAGE_FORM = new StringJoiner(".").add(KEY_APP_PACKAGE).add("form").toString();;

    /**
     * Where to find classes with JAXY annotation to build route.
     */
    public static final String KEY_APP_PACKAGE_JAXY = new StringJoiner(".").add(KEY_APP_PACKAGE).add("jaxy").toString();;

    /**
     * Version
     */
    public static final String KEY_APP_VER = new StringJoiner(".").add(KEY_APP).add("ver").toString();;

    /**
     * Description
     */
    public static final String KEY_APP_DESC = new StringJoiner(".").add(KEY_APP).add("description").toString();;

    public static final String KEY_APP_LANG = "app.lang";

    /**
     * The default lang when app deployed
     */
    public static final String KEY_APP_LANG_DEFAULT = new StringJoiner(".").add(KEY_APP_LANG).add("default").toString();

    /**
     * Supported lang list
     */
    public static final String KEY_APP_LANG_AVAILABLE = new StringJoiner(".").add(KEY_APP_LANG).add("available").toString();

    /**
     * Display available lang on the web: yes | no
     */
    public static final String KEY_APP_LANG_AVAILABLE_LIST = new StringJoiner(".").add(KEY_APP_LANG_AVAILABLE).add("list").toString();

    public static final String KEY_APP_SERVER = new StringJoiner(".").add(KEY_APP).add("server").toString();

    public static final String KEY_APP_SERVER_PORT = new StringJoiner(".").add(KEY_APP_SERVER).add("port").toString();

    public static int serverPort() {
        return getConf().getIntegerWithDefault(KEY_APP_SERVER_PORT, 8080);
    }

    public static String appVersion() {
        return getConf().getOrDie(KEY_APP_VER);
    }

    public static String defaultLang() {
        return getConf().getWithDefault(KEY_APP_LANG_DEFAULT, Locale.ENGLISH.getLanguage());
    }

    public static boolean displayLangList() {
        return getConf().getBooleanWithDefault(KEY_APP_LANG_AVAILABLE_LIST, true);
    }

    public static String packageForm() {
        return getConf().getWithDefault(KEY_APP_PACKAGE_FORM, "com.newhost2");
    }

    public static String packageJaxy() {
        return getConf().getWithDefault(KEY_APP_PACKAGE_JAXY, "com.newhost2");
    }

    public static String appName() {
        String raw = getConf().getWithDefault(KEY_APP, "Newhost");
        return Nh2MessageResolver.get("app", Nh2Params.extractKey(raw));
    }

    public static String appDescription() {
        String raw = getConf().getWithDefault(KEY_APP_DESC, "");
        return Nh2MessageResolver.get("app", Nh2Params.extractKey(raw));
    }

    public static String appStyle() {
        String style = getConf().getWithDefault(KEY_APP_STYLE, "bootstrap");
        return style;
    }
}
