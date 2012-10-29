package no.bekk.digiq.handlers;

import java.util.List;

import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.dao.MessageDao;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetMessagesToIdentification {
    
    private static final Logger LOG = LoggerFactory.getLogger(GetMessagesToIdentification.class);
    private final MessageDao messageDao;

    @Autowired
    public GetMessagesToIdentification(MessageDao messageDao) {
        this.messageDao = messageDao;
        
    }
    
    @Handler
    public void handle(Exchange exchange) {
        MessageBatch messages = messageDao.createMessageBatch();
        if (messages.isEmpty()) {
            exchange.setProperty(Exchange.ROUTE_STOP, true);
        } else {
            LOG.info("Sending {} messages to identification.", messages.getMessages().size());
            exchange.getOut().setBody(messages);
        }
    }

}
