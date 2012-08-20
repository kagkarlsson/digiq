package no.bekk.digiq.dao;

import java.util.List;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;

public interface MessageDao {

    public abstract void create(Message m);

    public abstract List<Message> reserveMessagesToIdentification();

    public abstract void updateStatus(Message m, Status status);

    public abstract int count();

    public abstract List<Message> listWithStatus(Status identify);

}