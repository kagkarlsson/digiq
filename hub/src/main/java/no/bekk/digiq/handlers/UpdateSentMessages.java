package no.bekk.digiq.handlers;

import no.bekk.digiq.model.Receipt;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;

public interface UpdateSentMessages {

    @Handler
    void handle(Exchange exchange) throws CamelException;

    boolean updateDatabase(Receipt receipt);
    
}
