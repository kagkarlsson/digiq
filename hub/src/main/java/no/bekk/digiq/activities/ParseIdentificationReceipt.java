package no.bekk.digiq.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import no.bekk.digiq.util.ZipHelper;
import no.digipost.xsd.avsender1_6.XmlJobbStatus;
import no.digipost.xsd.avsender1_6.XmlMasseutsendelseResultat;

import org.apache.camel.BytesSource;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.util.ExchangeHelper;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
public class ParseIdentificationReceipt {

    private static final Logger LOG = LoggerFactory.getLogger(ParseIdentificationReceipt.class);

    private final Jaxb2Marshaller unmarshaller;

    @Autowired
    public ParseIdentificationReceipt(Jaxb2Marshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Handler
    public void handle(Exchange exchange) throws InvalidPayloadException, IOException {
        InputStream is = ExchangeHelper.getMandatoryInBody(exchange, InputStream.class);

        Map<String, byte[]> entries = ZipHelper.unzipEntries(is);
        if (entries.size() != 1) {
            throw new RuntimeException("Receipt should have exactly one file.");
        }

        String singleKey = entries.keySet().iterator().next();
        byte[] content = entries.get(singleKey);

        if (FilenameUtils.isExtension(singleKey, "xml")) {
            XmlMasseutsendelseResultat result = (XmlMasseutsendelseResultat) unmarshaller.unmarshal(new BytesSource(content));
            parseReceipt(result);
        } else {
            LOG.error("A message was unparsable by Digipost.");
        }

    }

    public void parseReceipt(XmlMasseutsendelseResultat result) {

        if (result.getStatus().equals(XmlJobbStatus.FEILET)) {
            LOG.error("The receipt indicated an error. " + result.getFeilinformasjon().getFeilkode().name() + ":"
                    + result.getFeilinformasjon().getMelding());
            return;
        }
        
        int digipost = result.getMottakerSammendrag().getDigipost() + result.getMottakerSammendrag().getDigipostVask();
        int total = result.getMottakerSammendrag().getTotal();

        LOG.info("The recipt indicated success. {} of {} was delivered in Digipost.", digipost, total);
    }
}
