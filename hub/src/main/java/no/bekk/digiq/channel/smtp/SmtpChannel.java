package no.bekk.digiq.channel.smtp;

import java.util.Collection;

import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.channel.CamelChannel;
import no.bekk.digiq.channel.IncomingMessageListener;
import no.bekk.digiq.channel.TwoWayChannel;
import no.bekk.digiq.handlers.StoreMessage;

import org.apache.camel.CamelContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.server.SMTPServer;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;

@Component
public class SmtpChannel implements CamelChannel, TwoWayChannel, DisposableBean {
    private final ServerSetup GREENMAIL_SMTP_SERVER_SETUP = ServerSetupTest.SMTP;
    private final ServerSetup POP_SERVER_SETUP;
    static final Logger LOG = LoggerFactory.getLogger(DigipostMailHandler.class);
    private SMTPServer smtpServer;
    private final HubConfiguration config;
    private final StoreMessage storeMessage;
    private GreenMail greenMail;

    @Autowired
    public SmtpChannel(StoreMessage storeMessage, HubConfiguration config) {
        this.storeMessage = storeMessage;
        this.config = config;
        POP_SERVER_SETUP = new ServerSetup(config.getPopPort(), null, ServerSetup.PROTOCOL_POP3);
    }

    @Override
    public void start(CamelContext context) {
        startSmtpServer();
        startPopServer();
    }

    private void startPopServer() {
        greenMail = new GreenMail(new ServerSetup[] {POP_SERVER_SETUP, GREENMAIL_SMTP_SERVER_SETUP});
        greenMail.start();
        greenMail.setUser("to@localhost.com", "user", "pass");
    }

    private void startSmtpServer() {
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
        
        if (greenMail != null) {
            greenMail.stop();
        }
    }

    @Override
    public void sent(MessageBatch batch, Collection<Message> sentMessages) {
        StringBuilder b = new StringBuilder();
        
        b.append(StringUtils.rightPad("Jobb-id:", 30) + batch.digipostJobbId + "\n");
        b.append("\n");
        for (Message message : sentMessages) {
            b.append(StringUtils.rightPad(message.getRecepientIdentificationString(), 30));
            b.append(message.status.inNorwegian());
            b.append("\n");
        }
        // TODO: redo to use User.deliver(MimeMessage)
        GreenMailUtil.sendTextEmail("to@localhost.com", "noreply@digiq.no", "Kvittering for batch med id " + batch.digipostJobbId, 
                b.toString(), GREENMAIL_SMTP_SERVER_SETUP);
    }

}
