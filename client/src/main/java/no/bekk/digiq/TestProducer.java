package no.bekk.digiq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import no.bekk.digiq.Forsendelse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class TestProducer {

	private JmsTemplate jmsTemplate;

	@Autowired
	public TestProducer(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void test() {
		jmsTemplate.send("no.bekk.digiq.ny", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(new Forsendelse("Emne",
						"gustav.karlsson#123A", null, "Gustav Karlsson",
						"Radarveien 21", null, "1152", "Oslo", null,
						"Pdf innhold".getBytes()));
			}
		});
	}
}
