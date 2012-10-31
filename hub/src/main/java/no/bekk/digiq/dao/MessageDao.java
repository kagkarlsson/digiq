package no.bekk.digiq.dao;

import java.util.List;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;
import no.bekk.digiq.MessageBatch;

public interface MessageDao {

    Message getMessage(long id);
    Message create(Message m);

    List<Message> listWithStatus(Status identify);

    MessageBatch createMessageBatch();

    MessageBatch getBatch(String digipostJobbId);

}