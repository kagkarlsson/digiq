package no.bekk.digiq.activities;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component
public class ParseIdentificationReceipt {

    @Handler
    public void handle(Exchange exchange) {
        System.out.println("Fant fil " + exchange.getOut().getBody());
    }
    
}
