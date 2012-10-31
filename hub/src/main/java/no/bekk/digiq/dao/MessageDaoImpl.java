package no.bekk.digiq.dao;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;
import no.bekk.digiq.MessageBatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageDaoImpl implements MessageDao {
    
    private static final Logger LOG = LoggerFactory.getLogger(MessageDaoImpl.class);

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
        List<MessageBatch> results = em.createQuery("from MessageBatch mb where digipostJobbId = :digipostJobbId", MessageBatch.class).setParameter("digipostJobbId", digipostJobbId).getResultList();
        if (results == null || results.size() == 0) {
            return null;
        } else if (results.size() > 1){
            LOG.warn("Found {} batches with digipostJobbId='{}'. Returning the first.", results.size(), digipostJobbId);
        }
        return results.get(0);
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
            for (Message message : messages) {
                message.status = Status.SENT_DIGIPOST;
                message.batch = messageBatch;
            }
            return messageBatch;
        }
    }

    @Override
    public Message getMessage(long id) {
        return em.find(Message.class, id);
    }

}
