package no.bekk.digiq.activities;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class CreateDigipostZipTest {

	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test() throws IOException {
		CreateDigipostZip activity = new CreateDigipostZip();

		InputStream is = activity.createZip(null);
		assertEquals("mottakersplitt.xml", new ZipInputStream(is).getNextEntry().getName());
	}
	
}
