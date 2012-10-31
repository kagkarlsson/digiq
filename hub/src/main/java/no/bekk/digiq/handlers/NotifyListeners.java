package no.bekk.digiq.handlers;

import java.util.Collection;
import java.util.Map.Entry;

import javax.annotation.Resource;

import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.channel.ChannelResolver;
import no.bekk.digiq.channel.TwoWayChannel;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.model.Receipt;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimaps;

@Component
public class NotifyListeners {
    private static final Logger LOG = LoggerFactory.getLogger(NotifyListeners.class);

    private ChannelResolver channelResolver;
    private MessageDao messageDao;
    
    @Autowired
    public NotifyListeners(MessageDao messageDao, ChannelResolver channelResolver) {
        this.messageDao = messageDao;
        this.channelResolver = channelResolver;
    }
    
    @Handler
    public void handle(Exchange exchange) {
        Receipt receipt = exchange.getIn().getBody(Receipt.class);
        notifyListeners(receipt);
    }

    protected void notifyListeners(Receipt receipt) {
        String refJobbId = receipt.getRefJobbId();
        MessageBatch batch = messageDao.getBatch(refJobbId);
        
        if (batch == null) {
            LOG.warn("Could not find batch for digipostBatchId='{}'. Doing nothing.", refJobbId);
            return;
        }
        
        ImmutableMap<String, Collection<Message>> groupedByChannel = 
                Multimaps.index(batch.getMessages(), new Function<Message , String>() {

            @Override
            public String apply(Message input) {
                return input.channel != null ? input.channel : "none";
            }
        }).asMap();
        
        for (Entry<String, Collection<Message>>  entry : groupedByChannel.entrySet()) {
            TwoWayChannel channel = channelResolver.getTwoWayChannel(entry.getKey());
            channel.sent(batch, entry.getValue());
        }
    }
    
}
