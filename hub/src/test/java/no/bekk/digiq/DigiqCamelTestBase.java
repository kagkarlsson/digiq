package no.bekk.digiq;

import java.io.File;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.CamelTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })
@Ignore
public class DigiqCamelTestBase extends CamelTestSupport implements
		ApplicationContextAware {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
	@Resource
	protected JdbcTemplate jdbcTemplate;
	private ApplicationContext applicationContext;
    protected File store;

	@Before
	public void setUp() throws Exception {
		setUseRouteBuilder(false);
		store = tempFolder.newFolder("store");
		super.setUp();
	}

	@After
	public void after() throws Exception {
		super.tearDown();
		jdbcTemplate.update("delete from message");
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new SpringCamelContext(applicationContext);
		context.setLazyLoadTypeConverters(isLazyLoadingTypeConverter());

		return context;
	}

	protected void startCamel() throws Exception {
	    startCamel(new RouteBuilder[] { });
	}
	protected void startCamel(RouteBuilder routeBuilder) throws Exception {
		startCamel(new RouteBuilder[] { routeBuilder });
	}

	protected void startCamel(RouteBuilder[] createRouteBuilders)
			throws Exception {
		for (RouteBuilder builder : createRouteBuilders) {
			log.debug("Using created route builder: " + builder);
			context.addRoutes(builder);
		}

		log.info("Starting context with {} routes.",context.getRoutes().size());
		context.start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

}
