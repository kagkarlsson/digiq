package no.bekk.digiq.routes;

import no.bekk.digiq.dao.MessageDao;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.kahadb.util.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendToIdentificationRoute extends RouteBuilder {

	private final MessageDao messageDao;

	@Autowired
	public SendToIdentificationRoute(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	@Override
	public void configure() throws Exception {
		from("timer://messageTimer?period=1000")
				.bean(messageDao, "listToIdentification").policy("jdbcPolicy")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						Message in = exchange.getIn();
						in.setBody(new ByteArrayInputStream("Testing".getBytes()));
					}
					
				})
				.to("sftp://bekk@camelon.os.ergo.no/home/bekk/tmp?password=Gh78)#b34K");
	}

}
