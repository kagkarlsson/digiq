package no.bekk.digiq.xml;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import javax.xml.transform.stream.StreamResult;

import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBuilder;
import no.bekk.digiq.XsdHelper;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.google.common.collect.Lists;

public class MasseutsendelseBuilderTest {

    private Jaxb2Marshaller marshaller;

    @Before
    public void setUp() throws Exception {
        marshaller = XsdHelper.createValidatingJaxbMarshaller();
    }
    
    @Test
    public void test() {
        ArrayList<Message> recipients = Lists.newArrayList(MessageBuilder.newMessage().build());
        XmlMasseutsendelse masseutsendelse = MasseutsendelseBuilder.newMasseutsendelse().withRecipients(recipients).build();
        validate(masseutsendelse);
    }

    private void validate(XmlMasseutsendelse masseutsendelse) {
        marshaller.marshal(masseutsendelse, new StreamResult(new ByteArrayOutputStream()));
    }
}
