package ru.doxhost.newhost.server.web.menu;

import com.google.common.collect.Sets;
import com.google.inject.Singleton;
import io.vertx.core.json.Json;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.doxhost.newhost.server.routing.jaxy.Order;
import ru.doxhost.newhost.server.routing.jaxy.Path;
import ru.doxhost.newhost.server.config.Nh2Config;

import java.lang.reflect.Method;
import java.net.URL;

import java.text.MessageFormat;
import java.util.*;

import static java.text.MessageFormat.format;

/**
 * Builds main menu dynamically base on annotation.
 * @author Eugene Kirin
 */
@Singleton
public class MenuHandler {

    public static final String TMPL = "<li><a href=\"{0}\"></a></li>";

    public static final String SUB_TMPL = "<li class=\"dropdown\"><a href=\"{0}\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">{1}<span class=\"caret\"></span></a><ul class=\"dropdown-menu\"></ul></li>";

    private Menu menu;

    private Locale locale = Locale.getDefault();

    private static final MenuHandler instance = new MenuHandler();

    public static MenuHandler getInstance() {
        return instance;
    }

    public Menu getMenu() {
        if (menu == null) {
            menu = createMenu();
        }

        if (!Locale.getDefault().getLanguage().equals(locale.getLanguage())) {
            menu = createMenu();
            locale = Locale.getDefault();
        }

        return menu;
    }

    private Menu createMenu() {

        Menu menu = new Menu();

        menu.setActiveMarkerFunc(defaultActiveFunc());

        menu.setId("navbar");
        menu.setTmpl("<ul class=\"nav navbar-nav\"></ul>");

        final Map<String, Menu.Item> subItems = new HashMap<>();

        Reflections reflections = configureReflections();

        final boolean[] alreadyUnderLine = {false};

        final String[] header = new String[] {};

        reflections.getMethodsAnnotatedWith(Path.class).stream().filter(p -> p.getAnnotation(Path.class).menuItem())
                .sorted((e1, e2) -> Integer.compare(
                        e1.isAnnotationPresent(Order.class) ? e1.getAnnotation(Order.class).value() : Integer.MAX_VALUE,
                        e2.isAnnotationPresent(Order.class) ? e2.getAnnotation(Order.class).value() : Integer.MAX_VALUE
                ))
                .forEach(menuItem -> {

                    String base = menuItem.getDeclaringClass().getPackage().getName();
                    String baseName = "menu";

                    Path path = menuItem.getAnnotation(Path.class);

                    String pathString = path.value()[0];

                    StringTokenizer stringTokenizer = new StringTokenizer(pathString, "/");

                    if (stringTokenizer.countTokens() == 1 || "/".equals(pathString)) {
                        Menu.Item item = new Menu().new Item(PathLabel.label(path, base, baseName), format(TMPL, pathString), null);
                        menu.addItem(item);
                    }

                    if (stringTokenizer.countTokens() == 2) { // level 2

                        String oneLevel = stringTokenizer.nextToken();

                        if (subItems.containsKey(oneLevel)) {

                            Menu.Item sub = subItems.get(oneLevel);
                            sub.getSub();

                            List<Menu.Item> list = new LinkedList<Menu.Item>();
                            list.addAll(sub.getSub());

                            if (path.underLine() && !alreadyUnderLine[0]) {
                                list.add(separator());
                                alreadyUnderLine[0] = true;
                            }

                            list.add(new Menu().new Item(PathLabel.label(path, base, baseName), format(TMPL, path.value()[0]), null));

                            sub.setSub(Collections.unmodifiableList(list));

                        } else {

                            alreadyUnderLine[0] = path.underLine();

                            String subLabel = findSubmenuLabel(reflections, menuItem, baseName, base, oneLevel);

                            Menu.Item item = new Menu().new Item(oneLevel, MessageFormat.format(SUB_TMPL, oneLevel, subLabel == null ? PathLabel.label(oneLevel, base, baseName): subLabel),
                                    path.underLine()
                                            ? Arrays.asList(
                                            separator(),
                                            new Menu().new Item(PathLabel.label(path, base, baseName), format(TMPL, path.value()[0]), null)
                                    )
                                            : Arrays.asList(
                                            new Menu().new Item(PathLabel.label(path, base, baseName), format(TMPL, path.value()[0]), null)
                                    )
                            );
                            subItems.put(oneLevel, item);
                            menu.addItem(item);
                        }
                    }

                    if (stringTokenizer.countTokens() > 2) {
                        throw new MenuHandlerException("Can't handle menu item with more then 2 level. " + pathString);
                    }
                });

        return menu;
    }

    private Reflections configureReflections() {

        ConfigurationBuilder builder = new ConfigurationBuilder();

        Set<URL> packagesToScan = getPackagesToScanForRoutes();
        builder.addUrls(packagesToScan);

        builder.addScanners(new MethodAnnotationsScanner());
        return new Reflections(builder);
    }

    public Set<URL> getPackagesToScanForRoutes() {

        Set<URL> packagesToScanForRoutes = Sets.newHashSet();

        packagesToScanForRoutes.addAll(ClasspathHelper.forPackage(Nh2Config.packageJaxy()));
        packagesToScanForRoutes.addAll(ClasspathHelper.forPackage("ru.doxhost")); // TODO - need more independent package

        return packagesToScanForRoutes;
    }

    private String defaultActiveFunc() {
        return "function activeMarker(target) {\n" +
                "\n" +
                "        // Only support two level menu (/item/item.html) and the path has to start from '/' symbol.\n" +
                "\n" +
                "        if (target.indexOf('/') !== -1) {\n" +
                "\n" +
                "            var arr = target.split('/');\n" +
                "\n" +
                "            if (arr.length === 3) {                \n" +
                "                var firstItem = arr[1];\n" +
                "                $('a[href=\\\"' + firstItem + '\\\"]').parent().addClass(\"active\");                \n" +
                "\n" +
                "            } else if (arr.length != 2) {\n" +
                "                console.error(\"Only two level menu supported.\");\n" +
                "            }\n" +
                "        }\n" +
                "        $('a[href=\\\"' + target + '\\\"]').parent().addClass(\"active\");\n" +
                "    }";
    }

    private Menu.Item separator() {
        return new Menu().new Item("", "<li role=\"separator\" class=\"divider\"></li>", null);
    }

    private Menu.Item header(String headerLabel) {
        return new Menu().new Item(headerLabel, "<li class=\"dropdown-header\"></li>", null);
    }

    public static void main(String[] abc) {
        Menu menu = new MenuHandler().createMenu();
        System.out.println(Json.encodePrettily(menu));
    }

    private String findSubmenuLabel(
            final Reflections reflections,
            final Method menuItem,
            final String baseName,
            final String base,
            final String oneLevel) {

        String subLabel = null;

        Method[] toArray = reflections.getMethodsAnnotatedWith(Path.class).stream().
                filter(p -> p.getAnnotation(Path.class).menuItem()).toArray(Method[]::new);

        for (Method m : toArray) {

            String base1 = m.getDeclaringClass().getPackage().getName();

            StringTokenizer tokenizer = new StringTokenizer(menuItem.getAnnotation(Path.class).value()[0], "/");
            if (tokenizer.hasMoreTokens()) {
                String aa = tokenizer.nextToken();

                String labelKey = PathLabel.buildKey(aa, base1, baseName);

                String subLabelKey = PathLabel.buildKey(oneLevel, base, baseName);

                if (labelKey.equals(subLabelKey)) {
                    String label = PathLabel.label(aa, base1, baseName);
                    if (!label.equals(labelKey)) {
                        subLabel = label;
                        break;
                    }
                }
            }
        }
        return subLabel;
    }
}