package no.bekk.digiq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class TemplateResource {

    private final String resourcePath;
    private String subject;

    public TemplateResource(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public TemplateResource withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public InputStream asIS() {
        try {
            String replacedString = IOUtils.toString(getClass().getResourceAsStream(resourcePath)).replace("@SUBJECT@", subject);
            return new ByteArrayInputStream(replacedString.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
