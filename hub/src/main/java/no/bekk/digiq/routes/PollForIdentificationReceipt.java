package no.bekk.digiq.routes;

import no.bekk.digiq.handlers.NotifyListeners;
import no.bekk.digiq.handlers.ParseIdentificationReceipt;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PollForIdentificationReceipt extends RouteBuilder {

    private final ParseIdentificationReceipt parseIdentificationReceipt;
    private final NotifyListeners notifyListeners;

    @Autowired
    public PollForIdentificationReceipt(ParseIdentificationReceipt parseIdentificationReceipt, NotifyListeners notifyListeners) {
        this.parseIdentificationReceipt = parseIdentificationReceipt;
        this.notifyListeners = notifyListeners;
    }

    @Override
    public void configure() throws Exception {
        from("direct:sftpPollForReceipt")
        .bean(parseIdentificationReceipt)
        .bean(notifyListeners);
    }

}
