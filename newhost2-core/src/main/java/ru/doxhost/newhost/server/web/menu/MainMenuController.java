package ru.doxhost.newhost.server.web.menu;

import io.vertx.core.json.Json;
import ru.doxhost.newhost.server.routing.jaxy.GET;
import ru.doxhost.newhost.server.routing.jaxy.Order;
import ru.doxhost.newhost.server.routing.jaxy.Path;
import ru.doxhost.newhost.server.routing.Param;

import ru.doxhost.newhost.server.routing.Result;
import ru.doxhost.newhost.server.web.meta.MetaTag;

/**
 * This controller represents main menu.
 * @author Eugene Kirin on 20.11.2015.
 */
public class MainMenuController {

    /**
     * Returns main menu structure.
     * @param currentItem Selected menu item when user clicked on some item. Used to mark as active.
     * @return Json representation.
     */
    public Result json(@Param("item") String currentItem) {

        Menu menu = MenuHandler.getInstance().getMenu();

        menu.setCurrent(currentItem);

        return Result.JSON().display(Json.encodePrettily(menu));
    }
}
