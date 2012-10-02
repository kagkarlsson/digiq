package no.bekk.digiq.routes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult;

import no.bekk.digiq.DigiqCamelTestBase;
import no.bekk.digiq.handlers.ParseIdentificationReceipt;
import no.bekk.digiq.routes.PollForIdentificationReceipt;
import no.bekk.digiq.xml.MasseutsendelseResultatBuilder;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;

import org.apache.camel.builder.NotifyBuilder;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class PollForIdentificationReceiptTest extends DigiqCamelTestBase {

    @Resource
    private ParseIdentificationReceipt parseIdentificationReceipt;
    @Resource
    private Jaxb2Marshaller marshaller;
    private PollForIdentificationReceipt routes;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        routes = new PollForIdentificationReceipt(parseIdentificationReceipt);
    }
    
    @Test
    public void shouldBeAbleToPollEndpointForNewReceipt() throws Exception {
        startCamel(routes);
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();
        
        template.sendBody("direct:sftpPollForReceipt", 
                createReceiptZip(MasseutsendelseResultatBuilder.newResult().build()));
        
        notify.matches(5, TimeUnit.SECONDS);
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
