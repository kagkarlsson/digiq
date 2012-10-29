package no.bekk.digiq.channel;

import org.apache.camel.CamelContext;

public interface CamelChannel {

    void addTo(CamelContext context);

}
