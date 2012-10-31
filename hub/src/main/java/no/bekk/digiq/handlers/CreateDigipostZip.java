package no.bekk.digiq.handlers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.transform.stream.StreamResult;

import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.file.FileStore;
import no.bekk.digiq.xml.MasseutsendelseBuilder;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelse;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
public class CreateDigipostZip {

    public static final String DIGIQ = "digiq_";

    private static final Logger LOG = LoggerFactory.getLogger(CreateDigipostZip.class);

    private final Jaxb2Marshaller marshaller;
    private final HubConfiguration config;
    private final FileStore fileStore;

    @Autowired
    public CreateDigipostZip(FileStore fileStore, Jaxb2Marshaller jaxb2Marshaller, HubConfiguration config) {
        this.fileStore = fileStore;
        this.marshaller = jaxb2Marshaller;
        this.config = config;
    }

    @Handler
    public void handle(Exchange exchange) {
        LOG.info("Creating Zip");
        MessageBatch messageBatch = exchange.getIn().getBody(MessageBatch.class);
        exchange.getOut().setBody(createZip(messageBatch));
        exchange.getOut().setHeader("CamelFileName", DIGIQ + messageBatch.id + ".zip");
    }

    public InputStream createZip(MessageBatch messageBatch) {
        try {
            File tempFile = fileStore.createTempfile();
            ZipOutputStream zipOs = new ZipOutputStream(new FileOutputStream(tempFile));

            zipOs.putNextEntry(new ZipEntry("masseutsendelse.xml"));
            IOUtils.write(createMasseutsendelse(messageBatch), zipOs);

            for (no.bekk.digiq.Message message : messageBatch.getMessages()) {
                zipOs.putNextEntry(new ZipEntry(message.id + ".pdf"));
                InputStream is = fileStore.read(message);
                IOUtils.copy(is, zipOs);
            }

            zipOs.finish();
            zipOs.close();

            return new FileInputStream(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Error when creating zip-archive.", e);
        }
    }

    private byte[] createMasseutsendelse(MessageBatch messageBatch) {
        XmlMasseutsendelse xml = MasseutsendelseBuilder.newMasseutsendelse().withAvsender(config.getSenderId())
                .withJobbId(messageBatch.digipostJobbId).withRecipients(messageBatch.getMessages()).build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(xml, new StreamResult(baos));
        return baos.toByteArray();
    }

}
