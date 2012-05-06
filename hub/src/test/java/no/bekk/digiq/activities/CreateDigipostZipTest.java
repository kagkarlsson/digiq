package no.bekk.digiq.activities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.MessageBuilder;
import no.bekk.digiq.XsdHelper;
import no.bekk.digiq.util.ZipHelper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.google.common.collect.Lists;

public class CreateDigipostZipTest {

    private Jaxb2Marshaller marshaller;
    private CreateDigipostZip activity;

    @Before
    public void setUp() throws Exception {
        marshaller = XsdHelper.createValidatingJaxbMarshaller();
        activity = new CreateDigipostZip(marshaller, new HubConfiguration("1", null));
    }

    @Test
    public void testShouldCreateMasseutsendelseForOneRecipient() throws IOException {
        InputStream is = activity.createZip(Lists.newArrayList(MessageBuilder.newMessage().withId(1).build()));

        assertZipOk(is);
    }

    @Test
    public void testShouldCreateMasseutsendelseForOneRecipientIdentifiedByPersonalIdNumber() throws IOException {
        InputStream is = activity.createZip(Lists.newArrayList(MessageBuilder.newMessage().withId(1).withNoAdress()
                .withPersonalIdentificationNumber("12312312300").build()));

        assertZipOk(is);
    }

    @Test
    public void testShouldCreateMasseutsendelseForOneRecipientIdentifiedByDigipostadress() throws IOException {
        InputStream is = activity.createZip(Lists.newArrayList(MessageBuilder.newMessage().withId(1).withNoAdress()
                .withDigipostadress("test.person#1234").build()));

        assertZipOk(is);
    }

    
    private void assertZipOk(InputStream is) throws IOException {
        Map<String, byte[]> entries = ZipHelper.unzipEntries(is);
        assertEquals(2, entries.size());
        assertNotNull(entries.get("masseutsendelse.xml"));
        assertNotNull(entries.get("1.pdf"));
    }

    
}
