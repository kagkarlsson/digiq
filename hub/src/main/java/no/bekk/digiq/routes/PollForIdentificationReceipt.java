package no.bekk.digiq.routes;

import no.bekk.digiq.activities.ParseIdentificationReceipt;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PollForIdentificationReceipt extends RouteBuilder {

    private final ParseIdentificationReceipt parseIdentificationReceipt;

    @Autowired
    public PollForIdentificationReceipt(ParseIdentificationReceipt parseIdentificationReceipt) {
        this.parseIdentificationReceipt = parseIdentificationReceipt;

    }

    @Override
    public void configure() throws Exception {
        from("direct:sftpPollForReceipt")
        .bean(parseIdentificationReceipt);
    }

}
