package no.bekk.digiq.routes;

import no.bekk.digiq.handlers.StoreMessage;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreIncoming extends RouteBuilder{

	private StoreMessage storeMessage;

	@Autowired
	public StoreIncoming(StoreMessage storeMessage) {
		this.storeMessage = storeMessage;
	}
	
	@Override
	public void configure() throws Exception {
		from("direct:incoming")
		.bean(storeMessage);
	}

}
