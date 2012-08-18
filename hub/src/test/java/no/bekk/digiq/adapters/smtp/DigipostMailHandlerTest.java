package no.bekk.digiq.adapters.smtp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DigipostMailHandlerTest {

	@Test
	public void skalValidereRecipient() {
		assertTrue(DigipostMailHandler.RECIPIENT_PATTERN.matcher("a.b#1234").matches());
		assertTrue(DigipostMailHandler.RECIPIENT_PATTERN.matcher("a.b#123A").matches());
	}

	
}
