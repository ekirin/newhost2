package ru.doxhost.newhost.server.web.menu;

import io.vertx.core.json.Json;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Eugene Kirin on 19.11.2015.
 */
public class Menu {

    private String id;

    private String tmpl;

    private String current;

    private String activeMarkerFunc;

    private List<Item> items = new LinkedList<>();

    public String getActiveMarkerFunc() {
        return activeMarkerFunc;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTmpl(String tmpl) {
        this.tmpl = tmpl;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public void setActiveMarkerFunc(String activeMarkerFunc) {
        this.activeMarkerFunc = activeMarkerFunc;
    }

    public String getTmpl() {
        return tmpl;
    }

    public String getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", tmpl='" + tmpl + '\'' +
                ", current='" + current + '\'' +
                ", activeMarkerFunc='" + activeMarkerFunc + '\'' +
                ", items=" + items +
                '}';
    }

    public class Item {

        public Item(String name, String tmpl, List<Item> sub) {
            this.name = name;
            this.tmpl = tmpl;
            this.sub = sub;
        }

        private String name;

        private String tmpl;

        private List<Item> sub;

        public void setSub(List<Item> sub) {
            this.sub = sub;
        }

        public String getName() {
            return name;
        }

        public String getTmpl() {
            return tmpl;
        }

        public List<Item> getSub() {
            return sub;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", tmpl='" + tmpl + '\'' +
                    ", sub=" + sub +
                    '}';
        }
    }

    public static Menu create() {
        Menu menu = new Menu();

        menu.id = "navbar";
        menu.tmpl = "<ul class=\"nav navbar-nav\"></ul>";
        menu.items = Arrays.asList(
                new Menu().new Item("Home", "<li><a href=\"/\"></a></li>", null),
                new Menu().new Item("About", "<li><a href=\"/about\"></a></li>", null),
                new Menu().
                        new Item("", "<li class=\"dropdown\"><a href=\"england\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">England<span class=\"caret\"></span></a><ul class=\"dropdown-menu\"></ul></li>",
                        Arrays.asList(
                                new Menu().new Item("Arsenal", "<li><a href=\"/england/arsenal.html\"></a></li>", null),
                                new Menu().new Item("", "<li role=\"separator\" class=\"divider\"></li>", null),
                                new Menu().new Item("Header 2", "<li class=\"dropdown-header\"></li>", null),
                                new Menu().new Item("Liverpool", "<li><a href=\"/england/liverpool.html\"></a></li>", null),
                                new Menu().new Item("Manchester United", "<li><a href=\"index2.html\"></a></li>", null)
                        ))
        );

        return menu;
    }

    public static void main(String[] a) {

        Menu menu = create();

        System.out.println(Json.encodePrettily(menu));
    }

}
