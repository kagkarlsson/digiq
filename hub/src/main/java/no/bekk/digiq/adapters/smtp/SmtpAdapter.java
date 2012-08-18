package no.bekk.digiq.adapters.smtp;



import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.MainRoutes;
import no.bekk.digiq.adapters.CamelAdapter;
import no.bekk.digiq.adapters.IncomingMessageListener;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

public class SmtpAdapter implements CamelAdapter, MessageHandlerFactory {
    static final Logger LOG = LoggerFactory.getLogger(DigipostMailHandler.class);
    private SMTPServer smtpServer;
    private ProducerTemplate producerTemplate;
    private final HubConfiguration config;

    public SmtpAdapter(CamelContext context, HubConfiguration config) {
        this.config = config;
        producerTemplate = context.createProducerTemplate();
    }

    @Override
    public void stop() {
        if (smtpServer != null) {
            smtpServer.stop();
        }
    }

    @Override
    public void start() {
        smtpServer = new SMTPServer(this);
        smtpServer.setPort(config.getSmtpPort());
        smtpServer.start();
    }

    @Override
    public MessageHandler create(MessageContext ctx) {
        return new DigipostMailHandler(ctx, new IncomingMessageListener() {
            
            @Override
            public void received(Forsendelse forsendelse) {
                producerTemplate.sendBody(MainRoutes.INCOMING,forsendelse);
            }
        });
    }

}
