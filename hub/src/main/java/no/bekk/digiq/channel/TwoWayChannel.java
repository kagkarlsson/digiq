package no.bekk.digiq.channel;

import java.util.Collection;

import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBatch;

public interface TwoWayChannel {
    
    void sent(MessageBatch batch, Collection<Message> sentMessages);

}
