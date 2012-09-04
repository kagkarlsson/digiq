package no.bekk.digiq.handlers;

import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.Message;
import no.bekk.digiq.dao.MessageDao;
import no.bekk.digiq.file.FileStore;

import org.apache.camel.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StoreMessageImpl implements StoreMessage {

	private final MessageDao messageDao;
    private final FileStore fileStore;

	@Autowired
	public StoreMessageImpl(MessageDao messageDao, FileStore fileStore) {
		this.messageDao = messageDao;
        this.fileStore = fileStore;
	}
	
	@Override
	@Handler
	@Transactional
	public void store(Forsendelse forsendelse) {
	    Message created = messageDao.create(Message.fromForsendelse(forsendelse));
	    fileStore.store(created, forsendelse.pdf);
	}
}
