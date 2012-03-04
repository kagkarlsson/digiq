package no.bekk.digiq;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.routes.IncomingRoute;

import org.apache.camel.builder.NotifyBuilder;
import org.junit.Test;

public class IncomingFromQueueTest extends DigiqCamelTestBase {

	@Resource
	private TestProducer digiqClient;
	@Resource
	private MessageDao messageDao;
	@Resource
	private StoreMessage storeMessage;
	@Test
	public void test() throws Exception {
		startCamel(new IncomingRoute(storeMessage));

		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

		digiqClient.test();

		notify.matches(3, TimeUnit.SECONDS);

		assertEquals(1, messageDao.count());
	}

	@Test
	public void testDatabaseError() throws Exception {
		assertEquals(0, messageDao.count());
		
		StoreMessageImpl mockStore = mock(StoreMessageImpl.class);
		doThrow(new RuntimeException()).when(mockStore).store(
				any(Forsendelse.class));
		startCamel(new IncomingRoute(mockStore));

		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

		digiqClient.test();
		notify.matches(
				5, TimeUnit.SECONDS);
		
		assertEquals(0, messageDao.count());
		assertNotNull(consumer.receiveBody("activemq:ActiveMQ.DLQ"));
		assertNull(consumer.receiveBodyNoWait("activemq:no.bekk.digiq.ny"));
	}
}
