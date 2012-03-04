package no.bekk.digiq.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import no.bekk.digiq.Message;
import no.bekk.digiq.Message.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MessageDao {

	private final JdbcTemplate template;
	
	@Autowired
	public MessageDao(JdbcTemplate template) {
		this.template = template;
	}

	public void create(Message m) {
		template.update("insert into message(digipostAddress, personalIdentificationNumber, name, addressline1, addressline2, zipcode, city, country, content, status)" +
				" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", m.digipostAddress, m.personalIdentificationNumber, m.name, m.addressline1, m.addressline2, m.zipCode, m.city, m.country, m.content, m.status.name());
	}

	public List<Message> reserveMessagesToIdentification() {
		List<Message> toIdentification = listWithStatus(Status.IDENTIFY);
		for (Message message : toIdentification) {
			updateStatus(message, Status.NOT_SENT);
		}
		return toIdentification;
	}
	
	public void updateStatus(Message m, Status status) {
		template.update("update message set status = ? where id = ?", status.name(), m.id);
	}
	
	public int count(){
		return template.queryForInt("select count(*) from message");
	}

	public List<Message> listWithStatus(Status identify) {
		return template.query("select * from message where status = ?", messageRowMapper, identify.name());
	}

	
	private static final RowMapper<Message> messageRowMapper = new RowMapper<Message>() {
		
		@Override
		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Message(
					rs.getLong("id"),
					rs.getString("digipostAddress"),
					rs.getString("personalIdentificationNumber"),
					rs.getString("name"),
					rs.getString("addressline1"),
					rs.getString("addressline2"),
					rs.getString("zipcode"),
					rs.getString("city"),
					rs.getString("country"),
					rs.getBytes("content"),
					Status.valueOf(rs.getString("status")));
		}
	};
}
