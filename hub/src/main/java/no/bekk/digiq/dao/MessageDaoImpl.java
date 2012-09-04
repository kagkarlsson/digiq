package no.bekk.digiq.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageDaoImpl implements MessageDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Message create(Message m) {
        em.persist(m);
        return m;
    }

    @Override
    @Transactional
    public List<Message> reserveMessagesToIdentification() {
        List<Message> toIdentification = listWithStatus(Status.IDENTIFY);
        for (Message message : toIdentification) {
            updateStatus(message, Status.NOT_SENT);
        }
        return toIdentification;
    }

    @Override
    @Transactional
    public void updateStatus(Message m, Status status) {
        em.find(Message.class, m.id).status = status;
    }

    @Override
    public int count() {
        return em.createQuery("select count(1) from Message", Long.class).getSingleResult().intValue();
    }

    @Override
    public List<Message> listWithStatus(Status identify) {
        return em.createQuery("from Message m where status = :status", Message.class).setParameter("status", identify)
                .getResultList();
    }

}
