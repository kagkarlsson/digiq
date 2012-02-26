package no.bekk.digiq;

import no.bekk.digiq.dao.MessageDao;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StoreMessage {

	private final MessageDao messageDao;

	@Autowired
	public StoreMessage(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
	
	@Handler
	public void store(Forsendelse forsendelse) {
		messageDao.create(Message.fromForsendelse(forsendelse));
		System.out.println("Store!");
	}
}
