package no.bekk.digiq.model;

import static org.junit.Assert.assertEquals;
import no.bekk.digiq.Message.Status;
import no.bekk.digiq.xml.MasseutsendelseResultatBuilder;

import org.junit.Test;

public class ReceiptTest {

    @Test
    public void test() {
        Receipt receipt = new Receipt(MasseutsendelseResultatBuilder.newResult().withDigipostRecipient("id1", "test.test#1234")
                .withNotDigipostRecipient("id2").withInvalidRecipient("id3").withUnknownRecipient("id4").build());
        assertEquals(Status.DIGIPOST, receipt.toStatus("id1"));
        assertEquals(Status.NOT_DIGIPOST, receipt.toStatus("id2"));
        assertEquals(Status.INVALID, receipt.toStatus("id3"));
        assertEquals(Status.UNKNOWN, receipt.toStatus("id4"));
    }

}
