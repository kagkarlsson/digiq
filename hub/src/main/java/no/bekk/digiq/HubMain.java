package no.bekk.digiq;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.spi.ApplicationContextRegistry;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class HubMain {

	public static void main(String[] args) throws Exception {
		
		ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		springContext.registerShutdownHook();
		
		final CamelContext context = new DefaultCamelContext(new ApplicationContextRegistry(springContext));
		
		context.addRoutes(springContext.getBean(Routes.class));
		
		Runtime.getRuntime().addShutdownHook(new GracefulShutdown(springContext, context));

		springContext.start();
		context.start();
	}

	private static class GracefulShutdown extends Thread{
		private final AbstractApplicationContext springContext;
		private final CamelContext context;

		public GracefulShutdown(AbstractApplicationContext springContext, CamelContext context) {
			this.springContext = springContext;
			this.context = context;
		}
		
		
		@Override
		public void run() {
			try {
				context.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			springContext.stop();
		}
	}
}
