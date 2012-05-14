package no.bekk.digiq.handlers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.transform.stream.StreamResult;

import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.xml.MasseutsendelseBuilder;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelse;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
public class CreateDigipostZip {

    private final Jaxb2Marshaller marshaller;
    private final HubConfiguration config;

    @Autowired
    public CreateDigipostZip(Jaxb2Marshaller jaxb2Marshaller, HubConfiguration config) {
        this.marshaller = jaxb2Marshaller;
        this.config = config;
    }

    @SuppressWarnings("unchecked")
    @Handler
    public void handle(Exchange exchange) {
        Message in = exchange.getIn();
        List<no.bekk.digiq.Message> toIdentification = (List<no.bekk.digiq.Message>) in.getBody();
        exchange.getOut().setBody(createZip(toIdentification));
        exchange.getOut().setHeader("CamelFileName", "test1.zip");
    }

    public InputStream createZip(List<no.bekk.digiq.Message> recipients) {
        try {
            ByteArrayOutputStream zipped = new ByteArrayOutputStream();
            ZipOutputStream zipOs = new ZipOutputStream(zipped);

            zipOs.putNextEntry(new ZipEntry("masseutsendelse.xml"));
            IOUtils.write(createMasseutsendelse(recipients), zipOs);

            for (no.bekk.digiq.Message message : recipients) {
                zipOs.putNextEntry(new ZipEntry(message.id + ".pdf"));
                IOUtils.write(message.content, zipOs);
            }

            zipOs.finish();
            zipOs.close();

            return new ByteArrayInputStream(zipped.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error when creating zip-archive.", e);
        }

    }

    private byte[] createMasseutsendelse(List<no.bekk.digiq.Message> recipients) {
        XmlMasseutsendelse xml = MasseutsendelseBuilder.newMasseutsendelse().withAvsender(config.getSenderId()).withRecipients(recipients)
                .build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(xml, new StreamResult(baos));
        return baos.toByteArray();
    }

}
