package no.bekk.digiq.file;

import java.io.File;
import java.io.InputStream;

import no.bekk.digiq.Message;

public interface FileStore {

    void store(Message message, byte[] content);

    InputStream read(Message message);

    File createTempfile();

}
