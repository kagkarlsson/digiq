package no.bekk.digiq;

import java.io.IOException;
import java.io.InputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import no.bekk.digiq.Forsendelse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class DigipostQueue {

	private JmsTemplate jmsTemplate;

	@Autowired
	public DigipostQueue(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void sendDigipost(final String emne, final String digipostadress, final InputStream pdf) throws IOException {
	    final byte[] pdfBytes = IOUtils.toByteArray(pdf);
		jmsTemplate.send("no.bekk.digiq.ny", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(new Forsendelse(emne,
						digipostadress, null, null,
						null, null, null, null, null,
						pdfBytes));
			}
		});
	}
}
