package no.bekk.digiq;

import static org.junit.Assert.assertTrue;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

public class TestUtil {
    public static  void assertPdfContent(byte[] pdf) throws Exception {
        MagicMatch magicMatch = Magic.getMagicMatch(pdf);
        assertTrue(magicMatch.mimeTypeMatches("application/pdf"));
    }
    

}
