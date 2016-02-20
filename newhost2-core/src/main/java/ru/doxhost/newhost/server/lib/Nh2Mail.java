package ru.doxhost.newhost.server.lib;

import io.vertx.core.Vertx;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;

/**
 * @author Eugene Kirin
 */
public class Nh2Mail {

    //TODO - need to implement
    public static void mail(final Vertx vertx, final String to, final String body) {

        MailConfig config = new MailConfig();

        MailMessage message = new MailMessage();

        MailClient mailClient = MailClient.createNonShared(vertx, config);

        mailClient.sendMail(message, result ->{
            if (result.succeeded()) {

            } else {
                result.cause().printStackTrace();
            }
        });
    }
}
