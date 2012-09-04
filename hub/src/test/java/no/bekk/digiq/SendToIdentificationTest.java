package no.bekk.digiq;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import no.bekk.digiq.Message.Status;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.file.FileStore;
import no.bekk.digiq.handlers.CreateDigipostZip;
import no.bekk.digiq.handlers.GetMessagesToIdentification;
import no.bekk.digiq.routes.SendToIdentificationRoute;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class SendToIdentificationTest extends DigiqCamelTestBase {

	@Resource
	private GetMessagesToIdentification getMessagesToIdentification;
	@Resource
	private CreateDigipostZip createDigipostZip;
	@Resource
	private MessageDao messageDao;
	@Resource
	private FileStore fileStore;
	private MockEndpoint sftpMock;
	private SendToIdentificationRoute route;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		sftpMock = getMockEndpoint("mock:sftp");
		route = new SendToIdentificationRoute(getMessagesToIdentification, createDigipostZip);
		route.interceptSendToEndpoint("direct:sftpToIdentification").to(sftpMock).skipSendToOriginalEndpoint();
	}
	
	@Test
	public void testMessageToIdentificationShouldBeSentToSftpEndpoint() throws Exception {
		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).wereSentTo("mock:sftp").create();
		
		Message message = messageDao.create(MessageBuilder.newMessage().build());
		fileStore.store(message, "Hej".getBytes());
		assertEquals(1, messageDao.listWithStatus(Status.IDENTIFY).size());
		
		startCamel(route);
		
		notify.matches(5, TimeUnit.SECONDS);
		
		sftpMock.assertExchangeReceived(0);
		assertEquals(0, messageDao.listWithStatus(Status.IDENTIFY).size());
	}
	
	@Test
	public void testNoMessagesShouldNotTriggerSftp() throws Exception {
		NotifyBuilder notify = new NotifyBuilder(context).whenDone(1).create();
		assertEquals(0, messageDao.listWithStatus(Status.IDENTIFY).size());
		
		startCamel(route);
		
		notify.matches(5, TimeUnit.SECONDS);
		assertEquals(0, messageDao.listWithStatus(Status.IDENTIFY).size());
	}

}
