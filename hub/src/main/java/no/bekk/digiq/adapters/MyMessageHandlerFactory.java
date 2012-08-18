package no.bekk.digiq.adapters;



import no.posten.dpost.api.client.DigipostClient;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

class MyMessageHandlerFactory implements MessageHandlerFactory {

    private DigipostClient client;

	public MyMessageHandlerFactory(DigipostClient client) {
		this.client = client;
	}

	public MessageHandler create(MessageContext ctx) {
        return new DigipostMessageHandler(this, ctx, client);
    }
}