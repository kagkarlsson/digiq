package no.bekk.digiq;


import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientMain {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
		context.start();
		TestProducer producer = context.getBean(TestProducer.class);
		producer.test();
	}
}
