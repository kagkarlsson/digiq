package no.bekk.digiq.handlers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.MessageBuilder;
import no.bekk.digiq.channel.MockChannelResolver;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.model.Receipt;
import no.bekk.digiq.xml.MasseutsendelseResultatBuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

public class NotifyListenersTest {

    private static final String CHANNEL_ID = "channelId";
    @Mock
    private MessageDao messageDao;
    private MockChannelResolver channelResolver = new MockChannelResolver();
    private NotifyListeners notifyListeners;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        notifyListeners = new NotifyListeners(messageDao, channelResolver);
        when(messageDao.getBatch(anyString())).thenReturn(null);
    }

    @Test
    public void shouldNotNotifyIfBatchNotFound() {
        notifyListeners.notifyListeners(new Receipt(MasseutsendelseResultatBuilder.newResult().build()));
        assertEquals(0, channelResolver.getNotifiedMessages().size());
    }

    @Test
    public void shouldNotifyListenerIfBatchFound() {
        when(messageDao.getBatch(anyString())).thenReturn(new MessageBatch("digiId", 
                Lists.newArrayList(MessageBuilder.newMessage().withChannel(CHANNEL_ID).build())));
        notifyListeners.notifyListeners(new Receipt(MasseutsendelseResultatBuilder.newResult().build()));
        
        assertEquals(1, channelResolver.getNotifiedMessages().size());
    }

    

}
