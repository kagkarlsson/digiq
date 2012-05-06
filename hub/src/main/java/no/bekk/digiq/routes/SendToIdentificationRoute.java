package no.bekk.digiq.routes;

import no.bekk.digiq.activities.CreateDigipostZip;
import no.bekk.digiq.activities.GetMessagesToIdentification;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendToIdentificationRoute extends RouteBuilder {

	private final CreateDigipostZip createDigipostZip;
    private final GetMessagesToIdentification getMessagesToIdentification;

	@Autowired
	public SendToIdentificationRoute(GetMessagesToIdentification getMessagesToIdentification, CreateDigipostZip createDigipostZip) {
		this.getMessagesToIdentification = getMessagesToIdentification;
        this.createDigipostZip = createDigipostZip;
	}

	@Override
	public void configure() throws Exception {
		from("timer://messageTimer?period=1000")
				.bean(getMessagesToIdentification)
				.policy("jdbcPolicy")
				.bean(createDigipostZip)
				.to("direct:sftpToIdentification");
	}

}
