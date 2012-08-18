package no.bekk.digiq.adapters.smtp;



import java.util.List;

import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.adapters.CamelAdapter;
import no.bekk.digiq.adapters.IncomingMessageListener;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.AuthenticationHandler;
import org.subethamail.smtp.AuthenticationHandlerFactory;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

public class SmtpAdapter implements CamelAdapter, MessageHandlerFactory {
    static final Logger LOG = LoggerFactory.getLogger(DigipostMailHandler.class);
    private final CamelContext context;
    private SMTPServer smtpServer;
    private ProducerTemplate producerTemplate;

    public SmtpAdapter(CamelContext context) {
        this.context = context;
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
        smtpServer.setPort(25000);
        smtpServer.start();
    }

    @Override
    public MessageHandler create(MessageContext ctx) {
        return new DigipostMailHandler(ctx, new IncomingMessageListener() {
            
            @Override
            public void received(Forsendelse forsendelse) {
                producerTemplate.sendBody("direct:incoming",forsendelse);
                
            }
        });
    }

}
