package ru.doxhost.newhost.server.listener;

import ru.doxhost.newhost.server.config.Nh2Config;
import ru.doxhost.newhost.server.web.menu.MenuHandler;

import java.util.Locale;

import static ru.doxhost.newhost.server.Nh2HttpServer.logger;
/**
 * @author Eugene Kirin
 */
public interface IBootstrapListener {

    void onStart();

    void onFinish();

    /**
     * Just print info about server starting
     */
    static void start() {
        logger.info("NEWHOST2 Server starting...");
        logger.info("NEWHOST2 port: " + Nh2Config.serverPort());
        logger.info("NEWHOST2 locale: " + Nh2Config.defaultLang());
    }

    /**
     * Just print info about finish server start.
     */
    static void finish() {
        Locale.setDefault(new Locale(Nh2Config.defaultLang()));
        MenuHandler.getInstance().getMenu(); // init main menu.

        logger.info("NEWHOST2 Server started.");
    }
}
