package no.bekk.digiq.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import no.bekk.digiq.HubConfiguration;
import no.bekk.digiq.Message;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FilesystemFileStore implements FileStore {

    private File store;

    @Autowired 
    public FilesystemFileStore(HubConfiguration config) {
        store = new File(config.getStorepath());
    }
    
    @Override
    public void store(Message message, byte[] content) {
        
        try {
            FileUtils.writeByteArrayToFile(new File(store, getFilename(message)), content);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing message to filestore.",e);
        }
    }

    @Override
    public InputStream read(Message message) {
        File source = new File(store, getFilename(message));
        try {
            return FileUtils.openInputStream(source);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading message from filestore", e);
        }
    }

    private String getFilename(Message message) {
        return String.valueOf(message.id);
    }

}
