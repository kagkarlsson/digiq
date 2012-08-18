package no.bekk.digiq.adapters;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import no.posten.dpost.api.client.DigipostClient;
import no.posten.dpost.api.client.EventLogger;
import no.posten.dpost.api.client.security.FileKeystoreSigner;
import no.posten.dpost.api.representations.DigipostAddress;
import no.posten.dpost.api.representations.Message;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.RejectException;

class SmtpHandler implements MessageHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(SmtpHandler.class);

	public static final Pattern RECIPIENT_PATTERN = Pattern.compile("^[a-z.]+#[0-9A-Z]{4}$");
	private List<String> recipients = new ArrayList<String>();
	private final MyMessageHandlerFactory messageHandler;
	MessageContext ctx;

	private final DigipostClient client;

	public SmtpHandler(
			MyMessageHandlerFactory myMessageHandlerFactory,
			MessageContext ctx, DigipostClient client) {
		messageHandler = myMessageHandlerFactory;
		this.ctx = ctx;
		this.client = client;
	}

	public void from(String from) throws RejectException {
		LOG.debug("FROM:" + from);
	}

	public void recipient(String recipient) throws RejectException {
		LOG.debug("RECIPIENT:" + recipient);
		recipients.add(recipient);
	}

	public void data(InputStream data) throws IOException {
		byte[] bytes = IOUtils.toByteArray(data);
		FileUtils.writeByteArrayToFile(new File("mail.msg"), bytes);

		LOG.debug("MAIL DATA");
		LOG.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
		LOG.debug(this.convertStreamToString(new ByteArrayInputStream(bytes)));
		LOG.debug("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");

		try {
			
			MimeMessage message = new MimeMessage(
					Session.getInstance(new Properties()), new ByteArrayInputStream(bytes));
			MimeMultipart multipart = (MimeMultipart) message.getContent();

			for (String recipient : recipients) {

				if (RECIPIENT_PATTERN.matcher(recipient).matches()) {

					for (int i = 0; i < multipart.getCount(); i++) {
						BodyPart part = multipart.getBodyPart(i);
						ContentType contentType = new ContentType(
								part.getContentType());
						if (contentType.match("application/pdf")) {
							LOG.debug("Fant pdf attachment.");
							ByteArrayOutputStream baos = new ByteArrayOutputStream();
							IOUtils.copy(part.getInputStream(), baos);
							client.sendMessage(
									new Message(String.valueOf(System.currentTimeMillis()), message.getSubject(),
											new DigipostAddress(
													recipient),
											false),
									new ByteArrayInputStream(baos.toByteArray()));
						}
					}

				} else {
					LOG.warn("Ugyldig recipient '{}'. Sender ikke.", recipient);
				}
			}
		} catch (Exception e) {
			LOG.error("Feil.", e);
		}
	}

	public void done() {
		LOG.debug("Finished");
	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}