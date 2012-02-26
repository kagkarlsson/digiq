package no.bekk.digiq;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.CamelTestSupport;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@Transactional
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
public class DigiqCamelTest extends CamelTestSupport implements
		ApplicationContextAware {

	@Resource
	protected JmsTemplate jmsTemplate;
	@Resource
	protected JdbcTemplate jdbcTemplate;
	private ApplicationContext applicationContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new SpringCamelContext(applicationContext);

		context.setLazyLoadTypeConverters(isLazyLoadingTypeConverter());
		
		return context;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
