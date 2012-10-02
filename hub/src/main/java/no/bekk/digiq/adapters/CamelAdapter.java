package no.bekk.digiq.adapters;

import org.apache.camel.CamelContext;

public interface CamelAdapter {

    void addTo(CamelContext context);

}
