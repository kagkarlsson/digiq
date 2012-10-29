package no.bekk.digiq.handlers;

import javax.annotation.Resource;

import no.bekk.digiq.channel.ChannelResolver;
import no.bekk.digiq.dao.MessageDao;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component
public class NotifyListeners {

    @Resource
    private ChannelResolver channelResolver;
    
    @Resource
    private MessageDao messageDao;
    
    @Handler
    public void handle(Exchange exchange) {
        XmlMasseutsendelseResultat receipt = exchange.getIn().getBody(XmlMasseutsendelseResultat.class);
        messageDao.getBatch(receipt.getJobbSammendrag().getRefJobbId());
    }
    
}
