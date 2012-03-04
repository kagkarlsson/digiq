package no.bekk.digiq;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.routes.SendToIdentificationRoute;

import org.apache.camel.builder.NotifyBuilder;
import org.junit.Test;

public class SendToIdentificationTest extends DigiqCamelTestBase {

	@Resource
	private MessageDao messageDao;

	@Test
	public void test() throws Exception {
		startCamel(new SendToIdentificationRoute(messageDao));

		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();

		messageDao.create(MessageBuilder.newMessage().build());
		assertEquals(1, messageDao.count());
		
		notify.matches(5, TimeUnit.SECONDS);
	}

}
