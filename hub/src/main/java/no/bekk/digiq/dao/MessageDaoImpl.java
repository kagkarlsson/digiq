package no.bekk.digiq.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageDaoImpl implements MessageDao {

    @PersistenceContext
    private EntityManager em;

    private final JdbcTemplate template;

    @Autowired
    public MessageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.template = jdbcTemplate;
    }

    @Override
    @Transactional
    public void create(Message m) {
        em.persist(m);
        // template.update("insert into message(SUBJECT, DIGIPOSTADDRESS, PERSONAL_IDENTIFICATION_NUMBER, NAME, ADDRESSLINE1, ADDRESSLINE2, ZIPCODE, CITY, COUNTRY, CONTENT, STATUS)"
        // +
        // " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", m.subject,
        // m.digipostAddress, m.personalIdentificationNumber, m.name,
        // m.addressline1, m.addressline2, m.zipCode, m.city, m.country,
        // m.content, m.status.name());
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
        // template.update("update message set status = ? where id = ?",
        // status.name(), m.id);
    }

    @Override
    public int count() {
        return em.createQuery("select count(1) from Message", Long.class).getSingleResult().intValue();
        // return template.queryForInt("select count(*) from message");
    }

    @Override
    public List<Message> listWithStatus(Status identify) {
        List<Message> temp = em.createQuery("from Message", Message.class).getResultList();
        System.out.println("GUSTAV\n\n\n\n");
        for (Message message : temp) {
            System.out.println("Message: " + message.status.name());
        }
        return em.createQuery("from Message m where status = :status", Message.class).setParameter("status", identify)
                .getResultList();
        // return template.query("select * from message where status = ?",
        // messageRowMapper, identify.name());
    }

//    private static final RowMapper<Message> messageRowMapper = new RowMapper<Message>() {
//
//        @Override
//        public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
//            return new Message(rs.getLong("ID"), rs.getString("SUBJECT"), rs.getString("DIGIPOSTADDRESS"),
//                    rs.getString("PERSONAL_IDENTIFICATION_NUMBER"), rs.getString("NAME"), rs.getString("ADDRESSLINE1"),
//                    rs.getString("ADDRESSLINE2"), rs.getString("ZIPCODE"), rs.getString("CITY"), rs.getString("COUNTRY"),
//                    rs.getBytes("CONTENT"), Status.valueOf(rs.getString("STATUS")));
//        }
//    };
}
