package no.bekk.digiq;

import java.util.List;

import no.bekk.digiq.activities.CreateDigipostZip;
import no.bekk.digiq.activities.GetMessagesToIdentification;
import no.bekk.digiq.activities.ParseIdentificationReceipt;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.routes.IncomingRoute;
import no.bekk.digiq.routes.PollForIdentificationReceipt;
import no.bekk.digiq.routes.SendToIdentificationRoute;
import no.bekk.digiq.routes.SftpRoutes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class Routes {
	
	private final StoreMessage storeMessage;
	private CreateDigipostZip createDigipostZip;
    private final GetMessagesToIdentification getMessagesToIdentification;
    private ParseIdentificationReceipt parseIdentificationReceipt;

	@Autowired
	public Routes(StoreMessage storeMessage, MessageDao messageDao, CreateDigipostZip createDigipostZip, GetMessagesToIdentification getMessagesToIdentification, ParseIdentificationReceipt parseIdentificationReceipt) {
		this.storeMessage = storeMessage;
		this.createDigipostZip = createDigipostZip;
        this.getMessagesToIdentification = getMessagesToIdentification;
        this.parseIdentificationReceipt = parseIdentificationReceipt;
	}
	
	public List<RouteBuilder> getRouteBuilders() {
	    return Lists.newArrayList(
	            new IncomingRoute(storeMessage), 
	            new SendToIdentificationRoute(getMessagesToIdentification, createDigipostZip), 
	            new PollForIdentificationReceipt(parseIdentificationReceipt),
	            new SftpRoutes());
	}

}
