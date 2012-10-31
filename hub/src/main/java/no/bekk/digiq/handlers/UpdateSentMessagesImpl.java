package no.bekk.digiq.handlers;

import java.io.IOException;

import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.model.Receipt;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.util.ExchangeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateSentMessagesImpl implements UpdateSentMessages {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateSentMessagesImpl.class);
    
    private final MessageDao messageDao;
    @Autowired
    public UpdateSentMessagesImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Handler
    @Transactional
    @Override
    public void handle(Exchange exchange) throws InvalidPayloadException {
        Receipt receipt = ExchangeHelper.getMandatoryInBody(exchange, Receipt.class);
        if (!updateDatabase(receipt)) {
            LOG.warn("Could not find batch for digipostBatchId='{}'. Stopping route.", receipt.getRefJobbId());
            exchange.setProperty(Exchange.ROUTE_STOP, true);
        }
    }
    
    @Override
    public boolean updateDatabase(Receipt receipt) {
        MessageBatch batch = messageDao.getBatch(receipt.getRefJobbId());

        if (batch == null) {
            return false;
        } 

        for (Message m : batch.getMessages()) {
            m.status = receipt.toStatus(m.getRecepientId());
        }
        return true;
    }
    
}
