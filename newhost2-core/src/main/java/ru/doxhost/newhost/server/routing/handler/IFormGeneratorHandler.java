package ru.doxhost.newhost.server.routing.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by ekirin on 14.11.2015.
 */
public interface IFormGeneratorHandler extends Handler<RoutingContext> {

  static IFormGeneratorHandler create() {
    return new FormGeneratorHandler();
  }

  //public void generate(final Object formBean);
}
