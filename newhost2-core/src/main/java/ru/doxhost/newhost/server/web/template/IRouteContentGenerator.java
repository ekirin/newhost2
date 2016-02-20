package ru.doxhost.newhost.server.web.template;

/**
 *
 * @author Eugene Kirin
 */
public interface IRouteContentGenerator {

    static IRouteContentGenerator create() {
        return new RouteContentGenerator();
    }

    /**
     * Generates content file in filesystem
     * @param fileName content file name
     */
    void gen(String fileName);
}
