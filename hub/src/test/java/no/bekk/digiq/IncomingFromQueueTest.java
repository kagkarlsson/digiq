package no.bekk.digiq;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.handlers.StoreMessage;
import no.bekk.digiq.handlers.StoreMessageImpl;
import no.bekk.digiq.routes.IncomingRoute;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.kahadb.util.ByteArrayInputStream;
import org.junit.Test;

public class IncomingFromQueueTest extends DigiqCamelTestBase {

	@Resource
	private DigipostQueue digiqClient;
	@Resource
	private MessageDao messageDao;
	@Resource
	private StoreMessage storeMessage;
	@Test
	public void shouldReceiveMessagesFromQueue() throws Exception {
		startCamel(new IncomingRoute(storeMessage));

		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

		addMessageToQueue();

		notify.matches(10, TimeUnit.SECONDS);

		assertEquals(1, messageDao.count());
	}

	@Test
	public void shouldPutFailingMessageOnDLQ() throws Exception {
		assertEquals(0, messageDao.count());
		
		StoreMessageImpl mockStore = mock(StoreMessageImpl.class);
		doThrow(new RuntimeException()).when(mockStore).store(
				any(Forsendelse.class));
		startCamel(new IncomingRoute(mockStore));

		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

		addMessageToQueue();
		
		notify.matches(
				5, TimeUnit.SECONDS);
		
		assertEquals(0, messageDao.count());
		assertNotNull(consumer.receiveBody("activemq:ActiveMQ.DLQ"));
		assertNull(consumer.receiveBodyNoWait("activemq:no.bekk.digiq.ny"));
	}

    private void addMessageToQueue() throws IOException {
        digiqClient.sendDigipost("Emne", "first.last#1111", new ByteArrayInputStream("dummy".getBytes()));
    }
}
