package no.bekk.digiq;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.spi.ApplicationContextRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HubMain {

    private static final Logger LOG = LoggerFactory.getLogger(HubMain.class);
    
	public static void main(String[] args) throws Exception {
		
	    String configFile = System.getProperty("configFile");
        if (configFile == null) {
            throw new RuntimeException("Unable to start. System property 'configFile' must be set and point to a " +
                    "java properties file with application configuration.");
        }
        
		ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		LOG.info("Validating hub configuration.");
		springContext.getBean(HubConfiguration.class).validateConfiguration();
		
		
		final CamelContext context = new DefaultCamelContext(new ApplicationContextRegistry(springContext));

		for (RouteBuilder route : springContext.getBean(Routes.class).getRouteBuilders()) {
		    context.addRoutes(route);
        }
		 
		Runtime.getRuntime().addShutdownHook(new GracefulShutdown(springContext, context));
		
		springContext.start();
		context.start();
		
		awaitTermination();
	}

    private static void awaitTermination() {
        while (true) {
		    try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
		}
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
		    LOG.info("Shutting down hub application");
			try {
				context.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			springContext.stop();
		}
	}
}
