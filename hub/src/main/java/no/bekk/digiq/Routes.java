package no.bekk.digiq;

import no.bekk.digiq.activities.CreateDigipostZip;
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
	private CreateDigipostZip createDigipostZip;

	@Autowired
	public Routes(StoreMessage storeMessage, MessageDao messageDao, CreateDigipostZip createDigipostZip) {
		this.storeMessage = storeMessage;
		this.messageDao = messageDao;
		this.createDigipostZip = createDigipostZip;
	}

	@Override
	public void configure() throws Exception {
		
		new IncomingRoute(storeMessage).configure();
		new SendToIdentificationRoute(messageDao, createDigipostZip).configure();
		
	}

}
