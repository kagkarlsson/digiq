package no.bekk.digiq;

import java.util.ArrayList;
import java.util.List;

import no.bekk.digiq.adapters.CamelAdapter;
import no.bekk.digiq.adapters.smtp.SmtpAdapter;

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
		HubConfiguration config = springContext.getBean(HubConfiguration.class);
        config.validateConfiguration();
		
		
		final CamelContext context = new DefaultCamelContext(new ApplicationContextRegistry(springContext));


		for (RouteBuilder route : springContext.getBean(MainRoutes.class).getRouteBuilders()) {
		    context.addRoutes(route);
        }

		List<CamelAdapter> adapters = new ArrayList<CamelAdapter>();
		adapters.add(new SmtpAdapter(context, config));
		
		Runtime.getRuntime().addShutdownHook(new GracefulShutdown(springContext, context, adapters));
		
		springContext.start();
		context.start();
		
		for (CamelAdapter camelAdapter : adapters) {
            camelAdapter.start();
        }
		
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
        private final List<CamelAdapter> adapters;

		public GracefulShutdown(AbstractApplicationContext springContext, CamelContext context, List<CamelAdapter> adapters) {
			this.springContext = springContext;
			this.context = context;
            this.adapters = adapters;
		}
		
		
		@Override
		public void run() {
		    LOG.info("Shutting down hub application");
			try {
			    for (CamelAdapter a : adapters) {
			        a.stop();
                }
				context.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			springContext.stop();
		}
	}
}
