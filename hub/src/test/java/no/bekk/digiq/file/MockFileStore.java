package no.bekk.digiq.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import no.bekk.digiq.Message;

public class MockFileStore implements FileStore {

    @Override
    public void store(Message message, byte[] content) {
        System.out.println("Store");
    }

    @Override
    public InputStream read(Message message) {
        return new ByteArrayInputStream("Hej".getBytes());
    }

}
