package no.bekk.digiq;

import java.util.List;

import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.handlers.CreateDigipostZip;
import no.bekk.digiq.handlers.GetMessagesToIdentification;
import no.bekk.digiq.handlers.NotifyListeners;
import no.bekk.digiq.handlers.ParseIdentificationReceipt;
import no.bekk.digiq.handlers.StoreMessage;
import no.bekk.digiq.routes.PollForIdentificationReceipt;
import no.bekk.digiq.routes.SendToIdentificationRoute;
import no.bekk.digiq.routes.SftpRoutes;
import no.bekk.digiq.routes.StoreIncoming;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class MainRoutes {
	
	public static final String INCOMING = "direct:incoming";
	
    private final StoreMessage storeMessage;
	private CreateDigipostZip createDigipostZip;
    private final GetMessagesToIdentification getMessagesToIdentification;
    private ParseIdentificationReceipt parseIdentificationReceipt;

    private final NotifyListeners notifyListeners;

	@Autowired
	public MainRoutes(StoreMessage storeMessage, MessageDao messageDao, CreateDigipostZip createDigipostZip, 
	        GetMessagesToIdentification getMessagesToIdentification, ParseIdentificationReceipt parseIdentificationReceipt,
	        NotifyListeners notifyListeners) {
		this.storeMessage = storeMessage;
		this.createDigipostZip = createDigipostZip;
        this.getMessagesToIdentification = getMessagesToIdentification;
        this.parseIdentificationReceipt = parseIdentificationReceipt;
        this.notifyListeners = notifyListeners;
	}
	
	public List<RouteBuilder> getRouteBuilders() {
	    return Lists.newArrayList(
	            new StoreIncoming(storeMessage), 
	            new SendToIdentificationRoute(getMessagesToIdentification, createDigipostZip), 
	            new PollForIdentificationReceipt(parseIdentificationReceipt, notifyListeners),
	            new SftpRoutes());
	}

}
