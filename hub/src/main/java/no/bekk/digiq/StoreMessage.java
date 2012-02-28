package no.bekk.digiq;

import org.apache.camel.Handler;

public interface StoreMessage {

	@Handler
	void store(Forsendelse forsendelse);

}