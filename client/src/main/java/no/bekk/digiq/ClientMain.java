package no.bekk.digiq;


import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientMain {

	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
		context.start();
		DigipostQueue producer = context.getBean(DigipostQueue.class);
		producer.sendDigipost("Fra digiq", "gustav.karlsson#123A", ClientMain.class.getResourceAsStream("/Fra_gustav.pdf"));
	}
}
