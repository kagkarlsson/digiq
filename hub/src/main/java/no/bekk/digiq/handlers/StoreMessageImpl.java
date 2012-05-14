package no.bekk.digiq.handlers;

import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.Message;
import no.bekk.digiq.dao.MessageDao;

import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StoreMessageImpl implements StoreMessage {

	private final MessageDao messageDao;

	@Autowired
	public StoreMessageImpl(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
	
	@Override
	@Handler
	@Transactional
	public void store(Forsendelse forsendelse) {
		messageDao.create(Message.fromForsendelse(forsendelse));
	}
}
