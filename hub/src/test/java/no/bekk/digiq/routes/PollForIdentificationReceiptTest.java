package no.bekk.digiq.routes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;
import javax.xml.transform.stream.StreamResult;

import no.bekk.digiq.DigiqCamelTestBase;
import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.MessageBuilder;
import no.bekk.digiq.Message.Status;
import no.bekk.digiq.channel.MockChannelResolver;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.handlers.NotifyListeners;
import no.bekk.digiq.handlers.ParseIdentificationReceipt;
import no.bekk.digiq.handlers.UpdateSentMessages;
import no.bekk.digiq.xml.MasseutsendelseResultatBuilder;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;

import org.apache.camel.builder.NotifyBuilder;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

public class PollForIdentificationReceiptTest extends DigiqCamelTestBase {

    @Resource
    private ParseIdentificationReceipt parseIdentificationReceipt;
    @Resource
    private Jaxb2Marshaller marshaller;
    @Resource
    private MessageDao messageDao;
    @Resource
    private UpdateSentMessages updateSentMessages;

    private PollForIdentificationReceipt routes;
    private MessageBatch batch;
    private MockChannelResolver channelResolver;
    private Message message;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        channelResolver = new MockChannelResolver();
        routes = new PollForIdentificationReceipt(parseIdentificationReceipt, updateSentMessages, new NotifyListeners(messageDao,
                channelResolver));
        message = MessageBuilder.newMessage().withStatus(Status.IDENTIFY).build();
        messageDao.create(message);
        batch = messageDao.createMessageBatch();
    }

    @Test
    public void shouldBeAbleToPollEndpointForNewReceipt() throws Exception {
        startCamel(routes);
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

        template.sendBody(
                "direct:sftpPollForReceipt",
                createReceiptZip(MasseutsendelseResultatBuilder.newResult()
                        .withDigipostRecipient(message.getRecepientId(), "test.test#1234").withJobbId(batch.digipostJobbId).build()));

        notify.matches(5, TimeUnit.SECONDS);
        assertEquals(1, channelResolver.getNotifiedMessages().size());
        assertEquals(Status.DIGIPOST, messageDao.getMessage(message.id).status);
    }

    public InputStream createReceiptZip(XmlMasseutsendelseResultat resultat) {
        try {
            ByteArrayOutputStream zipped = new ByteArrayOutputStream();
            ZipOutputStream zipOs = new ZipOutputStream(zipped);

            zipOs.putNextEntry(new ZipEntry("masseutsendelse-resultat.xml"));

            marshaller.marshal(resultat, new StreamResult(zipOs));

            zipOs.finish();
            zipOs.close();

            return new ByteArrayInputStream(zipped.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error when creating zip.", e);
        }
    }
}
