package no.bekk.digiq.adapters;

import no.bekk.digiq.Forsendelse;

public interface IncomingMessageListener {

    void received(Forsendelse forsendelse);

}
