package no.bekk.digiq.channel.smtp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.TemplateResource;
import no.bekk.digiq.TestUtil;
import no.bekk.digiq.channel.IncomingMessageListener;
import no.bekk.digiq.channel.smtp.DigipostMailHandler;

import org.junit.Before;
import org.junit.Test;

public class DigipostMailHandlerTest {

    private static final String RECIPIENT_DP_ADDRESS = "test.testsson#0000";
    private IncomingMessageListener mockListener;
    private Forsendelse received;

    @Before
    public void setUp() {
        received = null;
        mockListener = new IncomingMessageListener() {

            @Override
            public void received(Forsendelse forsendelse) {
                received = forsendelse;
            }
        };
    }

    @Test
    public void skalValidereRecipient() {
        assertTrue(DigipostMailHandler.RECIPIENT_PATTERN.matcher("a.b#1234").matches());
        assertTrue(DigipostMailHandler.RECIPIENT_PATTERN.matcher("a.b#123A").matches());
    }

    @Test
    public void shouldConvertMailToForsendelse() throws Exception {
        DigipostMailHandler messageHandler = new DigipostMailHandler(null, mockListener);
        messageHandler.recipient(RECIPIENT_DP_ADDRESS);
        messageHandler.data(new TemplateResource("/outlook_mail_template.msg").withSubject("Hello").asIS());
        
        TestUtil.assertPdfContent(received.pdf);
        assertEquals(RECIPIENT_DP_ADDRESS, received.digipostAdresse);
        assertEquals("Hello", received.subject);
    }

}
