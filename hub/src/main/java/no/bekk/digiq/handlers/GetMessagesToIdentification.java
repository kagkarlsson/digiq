package no.bekk.digiq.handlers;

import java.util.List;

import no.bekk.digiq.Message;
import no.bekk.digiq.dao.MessageDao;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetMessagesToIdentification {
    
    private final MessageDao messageDao;

    @Autowired
    public GetMessagesToIdentification(MessageDao messageDao) {
        this.messageDao = messageDao;
        
    }
    
    @Handler
    public void handle(Exchange exchange) {
        List<Message> messages = messageDao.reserveMessagesToIdentification();
        if (messages.size() == 0) {
            exchange.setProperty(Exchange.ROUTE_STOP, true);
        }
        exchange.getOut().setBody(messages);
    }

}
