package no.bekk.digiq;

import no.bekk.digiq.adapters.CamelAdapter;

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
        new HubMain().start();
    }

    private void start() throws Exception {
        String configFile = System.getProperty("configFile");
        if (configFile == null) {
            throw new RuntimeException("Unable to start. System property 'configFile' must be set and point to a "
                    + "java properties file with application configuration.");
        }

        ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext("applicationContext.xml");

        validateConfiguration(springContext);

        final CamelContext context = new DefaultCamelContext(new ApplicationContextRegistry(springContext));
        Runtime.getRuntime().addShutdownHook(new GracefulShutdown(springContext, context));
        springContext.start();

        configureAndStartCamelContext(springContext, context);

        awaitTermination();

    }

    private void validateConfiguration(ClassPathXmlApplicationContext springContext) {
        LOG.info("Validating hub configuration.");
        HubConfiguration config = springContext.getBean(HubConfiguration.class);
        config.validateConfiguration();
    }

    private void configureAndStartCamelContext(ClassPathXmlApplicationContext springContext, final CamelContext context) throws Exception {
        for (RouteBuilder route : springContext.getBean(MainRoutes.class).getRouteBuilders()) {
            context.addRoutes(route);
        }

        for (CamelAdapter camelAdapter : springContext.getBeansOfType(CamelAdapter.class).values()) {
            camelAdapter.addTo(context);
        }

        context.start();
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

    private static class GracefulShutdown extends Thread {
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
