package no.bekk.digiq.channel.smtp;

import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.channel.CamelChannel;
import no.bekk.digiq.channel.IncomingMessageListener;
import no.bekk.digiq.handlers.StoreMessage;

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

@Component
public class SmtpAdapter implements CamelChannel, DisposableBean {
    static final Logger LOG = LoggerFactory.getLogger(DigipostMailHandler.class);
    private SMTPServer smtpServer;
    private final HubConfiguration config;
    private final StoreMessage storeMessage;

    @Autowired
    public SmtpAdapter(StoreMessage storeMessage, HubConfiguration config) {
        this.storeMessage = storeMessage;
        this.config = config;
    }

    @Override
    public void addTo(CamelContext context) {
        smtpServer = new SMTPServer(new MessageHandlerFactory() {
            
            @Override
            public MessageHandler create(MessageContext ctx) {
                return new DigipostMailHandler(ctx, new IncomingMessageListener() {
                    
                    @Override
                    public void received(Forsendelse forsendelse) {
                        storeMessage.store(forsendelse);
                    }
                });
            }
        });
        smtpServer.setPort(config.getSmtpPort());
        smtpServer.start();
    }

    @Override
    public void destroy() throws Exception {
        if (smtpServer != null) {
            smtpServer.stop();
        }
        
    }

}
