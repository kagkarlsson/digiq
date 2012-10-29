package no.bekk.digiq.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;
import no.bekk.digiq.MessageBatch;

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
    public List<Message> listWithStatus(Status identify) {
        return em.createQuery("from Message m where status = :status", Message.class).setParameter("status", identify)
                .getResultList();
    }

    @Override
    public MessageBatch getBatch(String digipostJobbId) {
        return em.createQuery("from MessageBatch mb where digipostJobbId = :digipostJobbId", MessageBatch.class).setParameter("digipostJobbId", digipostJobbId).getSingleResult();
    }

    @Override
    @Transactional
    public MessageBatch createMessageBatch() {
        List<Message> messages = listWithStatus(Status.IDENTIFY);
        if (messages.size() == 0){
            return MessageBatch.EMPTY;
        } else  {
            MessageBatch messageBatch = new MessageBatch(UUID.randomUUID().toString(), messages);
            em.persist(messageBatch);
            return messageBatch;
        }
    }

}
