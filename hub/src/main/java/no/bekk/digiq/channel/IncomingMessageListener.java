package no.bekk.digiq.channel;

import no.bekk.digiq.Forsendelse;

public interface IncomingMessageListener {

    void received(Forsendelse forsendelse);

}
