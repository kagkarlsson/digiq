package no.bekk.digiq.dao;

import java.util.List;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;
import no.bekk.digiq.MessageBatch;

public interface MessageDao {

    public abstract Message create(Message m);

    public abstract List<Message> listWithStatus(Status identify);

    public abstract MessageBatch createMessageBatch();

    public abstract MessageBatch getBatch(String digipostJobbId);

}