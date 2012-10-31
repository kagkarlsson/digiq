package no.bekk.digiq.routes;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.mail.Folder;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.bekk.digiq.DigiqCamelTestBase;
import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.MessageBuilder;
import no.bekk.digiq.TestUtil;
import no.bekk.digiq.channel.smtp.SmtpChannel;
import no.bekk.digiq.file.FileStore;
import no.bekk.digiq.handlers.StoreMessage;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class SmtpChannelTest extends DigiqCamelTestBase {

    private static final String DIGIPOSTADRESS = "test.testsson#0000";
    private SmtpChannel smtpAdapter;
    private HubConfiguration config;
    @Resource
    private StoreMessage storeMessage;
    @Resource
    private FileStore fileStore;
    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        startCamel();

        config = new HubConfiguration(store.getAbsolutePath(), null, null, "25000");
        smtpAdapter = new SmtpChannel(storeMessage, config);
        smtpAdapter.start(context);
    }

    @After
    public void tearDown() throws Exception {
        smtpAdapter.destroy();
    }

    @Test
    public void adapterShouldReceiveAndStoreMail() throws Exception {
        sendMail("localhost", config.getSmtpPort());

        Thread.sleep(2000); // TODO: fix with better await-logic

        List<Message> messges = em.createQuery("from " + Message.class.getSimpleName(), Message.class).getResultList();

        Message body = messges.get(0);

        assertNotNull(body);
        assertEquals(DIGIPOSTADRESS, body.digipostAddress);
        TestUtil.assertPdfContent(IOUtils.toByteArray(fileStore.read(body)));
    }

    @Test
    public void shouldBeAbleToFetchMailViaPop() throws Exception {
        assertEquals(0, fetchMail().length);
        smtpAdapter.sent(new MessageBatch("digiid", null), Lists.newArrayList(MessageBuilder.newMessage().withChannel("smtp").build()));
        Thread.sleep(100);
        assertEquals(1, fetchMail().length);
        javax.mail.Message singleMail = fetchMail()[0];
    }

    private javax.mail.Message[] fetchMail() {
        javax.mail.Message[] messages;
        try {
            Store store = connect();

            Folder inbox = store.getFolder("INBOX");
            if (inbox == null || !inbox.exists()) {
                throw new RuntimeException("Coiuld not find INBOX.");
            }
            inbox.open(Folder.READ_ONLY);
            messages = inbox.getMessages();
//            for (int i = 0; i < messages.length; i++) {
//                messages[i] = inbox.getMessage(i);
//            }
            inbox.close(false);
            store.close();
            return messages;
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Feil ved henting av mail via POP3.", e);
        } catch (MessagingException e) {
            throw new RuntimeException("Kunne ikke hente mail fra mailbox.", e);
        }
    }

    private Store connect() throws MessagingException {
        String host = "localhost";
        int port = 25001;
        String username = "user";
        String password = "pass";
        String provider = "pop3";

        Session session = Session.getDefaultInstance(new Properties(), null);
        Store store = session.getStore(provider);
        store.connect(host, port, username, password);
        return store;
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
