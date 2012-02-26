package no.bekk.digiq.routes;

import no.bekk.digiq.StoreMessage;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IncomingRoute extends RouteBuilder{

	private StoreMessage storeMessage;

	@Autowired
	public IncomingRoute(StoreMessage storeMessage) {
		this.storeMessage = storeMessage;
	}
	
	@Override
	public void configure() throws Exception {
		from("activemq:no.bekk.digiq.ny")
		.id("fromDigiqNy")
		.policy("jmsPolicy")
//		.convertBodyTo(String.class)
//		.to("sql:insert into message(navn) values (#)?dataSourceRef=dataSource");
		.bean(storeMessage);
	}

}
