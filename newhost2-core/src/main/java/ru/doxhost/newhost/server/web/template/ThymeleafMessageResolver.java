package ru.doxhost.newhost.server.web.template;

import org.thymeleaf.Arguments;
import org.thymeleaf.messageresolver.AbstractMessageResolver;
import org.thymeleaf.messageresolver.MessageResolution;
import ru.doxhost.newhost.server.lib.Nh2MessageResolver;

/**
 * To resolve message from html template
 */
public class ThymeleafMessageResolver extends AbstractMessageResolver {

    @Override
    public MessageResolution resolveMessage(Arguments arguments, String key, Object[] objects) {

        String base = arguments.getTemplateName();

        String message = Nh2MessageResolver.get(base, key);

        return message == null ? null : new MessageResolution(message);
    }
}
