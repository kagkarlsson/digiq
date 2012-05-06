package no.bekk.digiq;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HubConfiguration {

    private static final String SFTP_KEY_FILE_KEY = "sftp.key.file";
    private static final String SENDER_ID_KEY = "sender.id";
    @Value("${" + SENDER_ID_KEY + "}")
    private String senderId;
    @Value("${" + SFTP_KEY_FILE_KEY + "}")
    private String sftpKeyPath;

    private HubConfiguration() {
    }
    
    public HubConfiguration(String senderId, String stpKeyPath) {
        this();
        this.senderId = senderId;
        sftpKeyPath = stpKeyPath;
    }
    
    public long getSenderId() {
        return Long.parseLong(senderId);
    }

    public void validateConfiguration() {
        List<String> errors = new ArrayList<String>();
        if (senderId == null || !senderId.matches("^\\d+$")) {
            errors.add(SENDER_ID_KEY + " must be numeric: " + senderId);
        }
        
        File sftpKeyFile = new File(sftpKeyPath);
        if (!sftpKeyFile.exists()) {
            errors.add(SFTP_KEY_FILE_KEY + " does not exist: " + sftpKeyPath);
        }
        
        if (!errors.isEmpty()) {
            throwConfigurationError(StringUtils.join(errors, ", "));
        }
    }

    private void throwConfigurationError(String errorMessage) {
        throw new RuntimeException("Hub-configuration is not valid:\n"+errorMessage);
    }

}
