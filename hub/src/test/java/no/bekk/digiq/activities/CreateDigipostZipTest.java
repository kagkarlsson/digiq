package no.bekk.digiq.activities;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import no.bekk.digiq.MessageBuilder;
import no.bekk.digiq.XsdHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.google.common.collect.Lists;

public class CreateDigipostZipTest {

    private Jaxb2Marshaller marshaller;

    @Before
    public void setUp() throws JAXBException {
        MockitoAnnotations.initMocks(this);
        marshaller = XsdHelper.createValidatingJaxbMarshaller();
    }
    
    @Test
    public void testShouldCreateMottakersplittForOneRecipient() throws IOException {
        CreateDigipostZip activity = new CreateDigipostZip(marshaller);
        InputStream is = activity.createZip(Lists.newArrayList(MessageBuilder.newMessage().build()));
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry singleEntry = zis.getNextEntry();
        assertEquals("mottakersplitt.xml", singleEntry.getName());
//        byte[] b = new byte[(int) singleEntry.getSize()];
//        zis.read(b);
//        
//        marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(b)));
    }

}
