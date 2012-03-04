package no.bekk.digiq.routes;

import no.bekk.digiq.activities.CreateDigipostZip;
import no.bekk.digiq.dao.MessageDao;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendToIdentificationRoute extends RouteBuilder {

	private final MessageDao messageDao;
	private final CreateDigipostZip createDigipostZip;

	@Autowired
	public SendToIdentificationRoute(MessageDao messageDao, CreateDigipostZip createDigipostZip) {
		this.messageDao = messageDao;
		this.createDigipostZip = createDigipostZip;
	}

	@Override
	public void configure() throws Exception {
		from("timer://messageTimer?period=1000")
				.bean(messageDao, "reserveMessagesToIdentification").policy("jdbcPolicy")
				.choice()
				.when(simple("${body.size} == 0"))
				.stop()
				.otherwise()
				.bean(createDigipostZip)
				.to("sftp://bekk@camelon.os.ergo.no/home/bekk/tmp?password=Gh78)#b34K");
	}

}
