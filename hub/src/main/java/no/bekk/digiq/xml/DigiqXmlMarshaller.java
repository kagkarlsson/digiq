package no.bekk.digiq.xml;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;

import org.springframework.oxm.Marshaller;

public class DigiqXmlMarshaller {

    @Resource
    private Marshaller marshaller;
    
    public Marshaller getMarshaller() throws JAXBException {
        return marshaller;
    }
    
}
