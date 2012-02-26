package no.bekk.digiq;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import no.bekk.digiq.dao.MessageDao;

import org.apache.camel.builder.NotifyBuilder;
import org.junit.Test;

public class RouteTest extends DigiqCamelTest {

	@Resource
	private TestProducer digiqClient;

	@Resource
	private MessageDao messageDao;

	@Test
	public void test() throws Exception {
		NotifyBuilder notify = new NotifyBuilder(context).from("activemq.*")
				.whenDone(1).wereSentTo("bean*").and().from("timer:")
				.whenDone(1).wereSentTo("bean*").create();

		digiqClient.test();

		notify.matches(5, TimeUnit.SECONDS);

		assertEquals(1, messageDao.count());

	}
	
	

	// @Test
	// public void testNoConnectionToDatabase() throws Exception {
	// RouteBuilder rb = new RouteBuilder() {
	// public void configure() throws Exception {
	// interceptSendToEndpoint("bean:storeMessage").throwException(
	// new ConnectException("Cannot connect"));
	// }
	// };
	// camelContext.getRouteDefinitions();
	// Route route = camelContext.getRoute("fromDigiqNy");
	// route.adviceWith(camelContext, rb);
	// String sql = "select count(*) from partner_metric";
	// assertEquals(0, jdbcTemplate.queryForInt(sql));
	// jmsTemplate.sendBody("activemq:queue:partners", xml);
	// Thread.sleep(5000);
	// assertEquals(0, jdbcTemplate.queryForInt(sql));
	// }

}
