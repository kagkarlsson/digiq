package no.bekk.digiq.handlers;

import no.bekk.digiq.Forsendelse;

import org.apache.camel.Handler;

public interface StoreMessage {

	@Handler
	void store(Forsendelse forsendelse);

}