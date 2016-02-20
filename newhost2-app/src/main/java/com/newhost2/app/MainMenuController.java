package com.newhost2.app;

import ru.doxhost.newhost.server.routing.Result;
import ru.doxhost.newhost.server.routing.jaxy.GET;
import ru.doxhost.newhost.server.routing.jaxy.Order;
import ru.doxhost.newhost.server.routing.jaxy.Path;
import ru.doxhost.newhost.server.web.meta.MetaTag;

/**
 * Performs
 * @author Eugene Kirin
 */
public class MainMenuController {

    /**
     * Index page
     */
    @GET
    @Path(value = {"/", "/index"}, menuItem = true) @Order(1)
    @MetaTag(keywords = "{PAGE.INDEX.KEYWORDS}", description = "{PAGE.INDEX.DESCRIPTION}")
    public Result index() {
        return Result.HTML();
    }
}
