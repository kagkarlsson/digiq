package no.bekk.digiq;

import no.bekk.digiq.activities.CreateDigipostZip;
import no.bekk.digiq.activities.GetMessagesToIdentification;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.routes.IncomingRoute;
import no.bekk.digiq.routes.SendToIdentificationRoute;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Routes extends RouteBuilder{
	
	private final StoreMessage storeMessage;
	private CreateDigipostZip createDigipostZip;
    private final GetMessagesToIdentification getMessagesToIdentification;

	@Autowired
	public Routes(StoreMessage storeMessage, MessageDao messageDao, CreateDigipostZip createDigipostZip, GetMessagesToIdentification getMessagesToIdentification) {
		this.storeMessage = storeMessage;
		this.createDigipostZip = createDigipostZip;
        this.getMessagesToIdentification = getMessagesToIdentification;
	}

	@Override
	public void configure() throws Exception {
		
		new IncomingRoute(storeMessage).configure();
		new SendToIdentificationRoute(getMessagesToIdentification, createDigipostZip).configure();
		
	}

}
