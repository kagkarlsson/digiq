package no.bekk.digiq.xml;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;


@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
public class MarshallingTest {

    @Resource
    private Jaxb2Marshaller marshaller;
    
    @Test
    public void test() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(MasseutsendelseResultatBuilder.newResult().build(), new StreamResult(baos
                ));
        XmlMasseutsendelseResultat xml = (XmlMasseutsendelseResultat) marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(baos.toByteArray())));
        assertNotNull(xml);
    }
    
}
