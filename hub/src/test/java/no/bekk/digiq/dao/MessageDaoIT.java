package no.bekk.digiq.dao;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;
import no.bekk.digiq.MessageBatch;
import no.bekk.digiq.MessageBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
public class MessageDaoIT extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    private MessageDao messageDao;
    @PersistenceContext
    private EntityManager em;
    
    @Test
    public void test() {
        messageDao.create(MessageBuilder.newMessage().withStatus(Status.IDENTIFY).build());
        em.flush();
        
        MessageBatch batch = messageDao.createMessageBatch();
        assertEquals(1, batch.getMessages().size());
        em.flush();
        
        MessageBatch fetchedBatch = messageDao.getBatch(batch.digipostJobbId);
        
    }
    
}
