package no.bekk.digiq;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HubConfiguration {

    private static final String SMTP_PORT_KEY = "smtp.port";
    private static final String SFTP_KEY_FILE_KEY = "sftp.key.file";
    private static final String SENDER_ID_KEY = "sender.id";
    private static final String STOREPATH_KEY = "store.path";
    @Value("${" + SENDER_ID_KEY + "}")
    private String senderId;
    @Value("${" + SFTP_KEY_FILE_KEY + "}")
    private String sftpKeyPath;
    @Value("${" + SMTP_PORT_KEY + "}")
    private String smtpPort;
    @Value("${" + STOREPATH_KEY + "}")
    private String storepath;

    @SuppressWarnings("unused")
    private HubConfiguration() {
    }
    
    public HubConfiguration(String storepath, String senderId, String stpKeyPath, String smtpPort) {
        this.storepath = storepath;
        this.senderId = senderId;
        sftpKeyPath = stpKeyPath;
        this.smtpPort = smtpPort;
    }
    
    public long getSenderId() {
        return Long.parseLong(senderId);
    }

    public int getSmtpPort() {
        return Integer.parseInt(smtpPort);
    }

    public int getPopPort() {
        // TODO hardcoded
        return 25001;
    }

    public void validateConfiguration() {
        List<String> errors = new ArrayList<String>();
        if (senderId == null || !numerical(senderId)) {
            errors.add(SENDER_ID_KEY + " must be numeric: " + senderId);
        }
        
        if (smtpPort == null || !numerical(smtpPort)) {
            errors.add(SMTP_PORT_KEY + " must be numeric: " + smtpPort);
        }
        
        File sftpKeyFile = new File(sftpKeyPath);
        if (!sftpKeyFile.exists()) {
            errors.add(SFTP_KEY_FILE_KEY + " does not exist: " + sftpKeyPath);
        }
        
        File storeFile = new File(storepath);
        if (!storeFile.exists()) {
            errors.add(STOREPATH_KEY + " does not exist: " + storepath);
        }
        
        if (!errors.isEmpty()) {
            throwConfigurationError(StringUtils.join(errors, ", "));
        }
    }

    private boolean numerical(String string) {
        return string.matches("^\\d+$");
    }

    private void throwConfigurationError(String errorMessage) {
        throw new RuntimeException("Hub-configuration is not valid:\n"+errorMessage);
    }

    public String getStorepath() {
        return storepath;
    }

}
