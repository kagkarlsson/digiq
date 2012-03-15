package no.bekk.digiq.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.transform.stream.StreamResult;

import no.bekk.digiq.xml.MottakersplittBuilder;
import no.digipost.xsd.avsender1_6.XmlMottakersplitt;

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

    @Autowired
    public CreateDigipostZip(Jaxb2Marshaller jaxb2Marshaller) {
        this.marshaller = jaxb2Marshaller;
    }

    @SuppressWarnings("unchecked")
    @Handler
    public void handle(Exchange exchange) {
        Message in = exchange.getIn();
        List<no.bekk.digiq.Message> toIdentification = (List<no.bekk.digiq.Message>) in.getBody();
        exchange.getOut().setBody(createZip(toIdentification));
    }

    public InputStream createZip(List<no.bekk.digiq.Message> recipients) {
        try {
            ByteArrayOutputStream zipped = new ByteArrayOutputStream();
            ZipOutputStream zipOs = new ZipOutputStream(zipped);

            zipOs.putNextEntry(new ZipEntry("mottakersplitt.xml"));
            IOUtils.write(createMottakersplitt(recipients), zipOs);
            zipOs.finish();
            zipOs.close();

            return new ByteArrayInputStream(zipped.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error when creating zip-archive.", e);
        }

    }

    private byte[] createMottakersplitt(List<no.bekk.digiq.Message> recipients) {
        XmlMottakersplitt xml = MottakersplittBuilder.newMottakersplitt().withRecipients(recipients).build();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(xml, new StreamResult(baos));
        return baos.toByteArray();
    }

}
