package no.bekk.digiq;

import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.routes.IncomingRoute;
import no.bekk.digiq.routes.SendToIdentificationRoute;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Routes extends RouteBuilder{
	
	private final StoreMessage storeMessage;
	private final MessageDao messageDao;

	@Autowired
	public Routes(StoreMessage storeMessage, MessageDao messageDao) {
		this.storeMessage = storeMessage;
		this.messageDao = messageDao;
	}

	@Override
	public void configure() throws Exception {
		
		new IncomingRoute(storeMessage).configure();
		new SendToIdentificationRoute(messageDao).configure();
		
	}

}
