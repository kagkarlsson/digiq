package no.bekk.digiq.routes;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import no.bekk.digiq.DigiqCamelTestBase;
import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.MainRoutes;
import no.bekk.digiq.TestUtil;
import no.bekk.digiq.adapters.smtp.SmtpAdapter;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SmtpAdapterTest extends DigiqCamelTestBase {

    private static final String DIGIPOSTADRESS = "test.testsson#0000";
    private SmtpAdapter smtpAdapter;
    private HubConfiguration config;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        startCamel(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from(MainRoutes.INCOMING).to("mock:incoming");
            }
        });

        config = new HubConfiguration(store.getAbsolutePath(), null, null, "25000");
        smtpAdapter = new SmtpAdapter(config);
        smtpAdapter.addTo(context);
    }

    @After
    public void tearDown() throws Exception {
        smtpAdapter.destroy();
    }

    @Test
    public void test() throws Exception {
        sendMail("localhost", config.getSmtpPort());
        MockEndpoint incoming = getMockEndpoint("mock:incoming");
        Exchange received = incoming.assertExchangeReceived(0);
        Forsendelse body = received.getIn().getBody(Forsendelse.class);

        assertNotNull(body);
        assertEquals(DIGIPOSTADRESS, body.digipostAdresse);
        TestUtil.assertPdfContent(body.pdf);
    }

    private void sendMail(String host, int port) throws Exception {
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        Transport transport = mailSession.getTransport("smtp");

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("Emne");

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent("Dummt text", "text/html");

        MimeBodyPart attachFilePart = new MimeBodyPart();
        ByteArrayDataSource ds = new ByteArrayDataSource(getClass().getResourceAsStream("/Fra_gustav.pdf"), "application/pdf");

        attachFilePart.setDataHandler(new DataHandler(ds));
        attachFilePart.setFileName("Fra_gustav.pdf");

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(textPart);
        mp.addBodyPart(attachFilePart);

        message.setContent(mp);
        message.addRecipient(RecipientType.TO, new InternetAddress(DIGIPOSTADRESS));

        transport.connect(host, port, "user", "");
        transport.sendMessage(message, message.getRecipients(RecipientType.TO));
        transport.close();
    }

}
