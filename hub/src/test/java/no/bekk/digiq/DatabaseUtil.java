package no.bekk.digiq;

import org.springframework.jdbc.core.JdbcTemplate;

public class DatabaseUtil {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseUtil(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clearDatabase() {
        jdbcTemplate.update("delete from message");
        jdbcTemplate.update("delete from message_batch");
    }

}
