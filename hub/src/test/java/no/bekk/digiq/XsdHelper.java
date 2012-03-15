package no.bekk.digiq;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

import no.digipost.xsd.avsender1_6.XmlMasseutsendelse;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class XsdHelper {

    public static Jaxb2Marshaller createValidatingJaxbMarshaller() throws Exception {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setValidationEventHandler(new ValidationEventHandler() {
            
            @Override
            public boolean handleEvent(ValidationEvent arg0) {
                return false;
            }
        });
        marshaller.setClassesToBeBound(XmlMasseutsendelse.class, XmlMasseutsendelseResultat.class);
        marshaller.setSchemas(new Resource[] {
                new ClassPathResource("/xsd/avsender1_6.xsd")
                });
        marshaller.afterPropertiesSet();
        return marshaller;
    }

    
}
