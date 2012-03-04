package no.bekk.digiq;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import no.bekk.digiq.Message.Status;
import no.bekk.digiq.activities.CreateDigipostZip;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.routes.SendToIdentificationRoute;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class SendToIdentificationTest extends DigiqCamelTestBase {

	@Resource
	private MessageDao messageDao;
	@Resource
	private CreateDigipostZip createDigipostZip;
	private MockEndpoint sftpMock;
	private SendToIdentificationRoute routes;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		sftpMock = getMockEndpoint("mock:sftp");
		routes = new SendToIdentificationRoute(messageDao, createDigipostZip);
		routes.interceptSendToEndpoint("sftp*").to(sftpMock).skipSendToOriginalEndpoint();	
	}
	
	@Test
	public void testMessageToIdentificationShouldBeSentToSftpEndpoint() throws Exception {
		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).wereSentTo("mock:sftp").create();
		
		messageDao.create(MessageBuilder.newMessage().build());
		assertEquals(1, messageDao.listWithStatus(Status.IDENTIFY).size());
		
		startCamel(routes);
		
		notify.matches(5, TimeUnit.SECONDS);
		
		sftpMock.assertExchangeReceived(0);
		assertEquals(0, messageDao.listWithStatus(Status.IDENTIFY).size());
	}
	
	@Test
	public void testNoMessagesShouldNotTriggerSftp() throws Exception {
		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();
		assertEquals(0, messageDao.listWithStatus(Status.IDENTIFY).size());
		
		startCamel(routes);
		
		notify.matches(5, TimeUnit.SECONDS);
		assertEquals(0, messageDao.listWithStatus(Status.IDENTIFY).size());
	}

}