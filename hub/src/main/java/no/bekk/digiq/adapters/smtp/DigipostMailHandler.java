package no.bekk.digiq.adapters.smtp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import no.bekk.digiq.Forsendelse;
import no.bekk.digiq.adapters.IncomingMessageListener;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.RejectException;

public class DigipostMailHandler implements MessageHandler{
    
    public static final Pattern RECIPIENT_PATTERN = Pattern.compile("^[a-z.]+#[0-9A-Z]{4}$");
    private static final Logger LOG = LoggerFactory.getLogger(DigipostMailHandler.class);
    
    private List<String> recipients = new ArrayList<String>();
    private final MessageContext ctx;
    private final IncomingMessageListener listener;
    
    public DigipostMailHandler(MessageContext ctx, IncomingMessageListener listener) {
        this.ctx = ctx;
        this.listener = listener;
    }

    public void from(String from) throws RejectException {
        LOG.debug("Incoming mail from:" + from);
    }

    public void recipient(String recipient) throws RejectException {
        LOG.debug("Incoming mail recipient:" + recipient);
        recipients.add(recipient);
    }

    public void data(InputStream data) throws IOException {
        byte[] dataBytes = IOUtils.toByteArray(data);

        LOG.debug("Incoming mail data:");
        LOG.debug(new String(dataBytes));

        try {
            MimeMessage message = new MimeMessage(
                    Session.getInstance(new Properties()), new ByteArrayInputStream(dataBytes));
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
                            listener.received(new Forsendelse("subject", recipient, null, null, null, null, null, null, null, baos.toByteArray()));
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