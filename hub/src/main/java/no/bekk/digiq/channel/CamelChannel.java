package no.bekk.digiq.channel;

import org.apache.camel.CamelContext;

public interface CamelChannel extends Channel {

    void start(CamelContext context);

}
