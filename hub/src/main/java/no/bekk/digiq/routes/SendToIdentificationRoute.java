package no.bekk.digiq.routes;

import no.bekk.digiq.dao.MessageDao;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendToIdentificationRoute extends RouteBuilder{

	private final MessageDao messageDao;

	@Autowired
	public SendToIdentificationRoute( MessageDao messageDao) {
		this.messageDao = messageDao;
	}
	
	@Override
	public void configure() throws Exception {
		from("timer://messageTimer?period=1000")
		.bean(messageDao, "listToIdentification")
		.policy("jdbcPolicy")
		.to("stream:out");
//		.to("sql:select * from message?dataSourceRef=dataSource");
	}

}
