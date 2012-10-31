package no.bekk.digiq.channel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.bekk.digiq.Message;
import no.bekk.digiq.MessageBatch;

public class MockChannelResolver implements ChannelResolver {

        private List<Message> notifiedMessages = new ArrayList<Message>();
        private MockChannel mockChannel = new MockChannel();
        
        @Override
        public TwoWayChannel getTwoWayChannel(String key) {
            return mockChannel;
        }
        
        public List<Message> getNotifiedMessages() {
            return notifiedMessages;
        }

        private class MockChannel implements TwoWayChannel {

            @Override
            public void sent(MessageBatch batch, Collection<Message> sentMessages) {
                getNotifiedMessages().addAll(sentMessages);
            }
        }
}
