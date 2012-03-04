package no.bekk.digiq.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class CreateDigipostZip {

	@SuppressWarnings("unchecked")
	@Handler
	public void handle(Exchange exchange) {
		Message in = exchange.getIn();
		List<no.bekk.digiq.Message> toIdentification = (List<no.bekk.digiq.Message>) in
				.getBody();
		exchange.getOut().setBody(createZip(toIdentification));
	}

	public InputStream createZip(List<no.bekk.digiq.Message> toIdentification) {
		try {
			ByteArrayOutputStream zipped = new ByteArrayOutputStream();
			ZipOutputStream zipOs = new ZipOutputStream(zipped);

			zipOs.putNextEntry(new ZipEntry("mottakersplitt.xml"));
			IOUtils.write("<mottakersplitt/>", zipOs);
			zipOs.finish();
			zipOs.close();

			return new ByteArrayInputStream(zipped.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Error when creating zip-archive.", e);
		}

	}

}
