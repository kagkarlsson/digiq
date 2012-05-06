package no.bekk.digiq.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipHelper {
    
    private static final int BUFFER = 2048;

    public static Map<String, byte[]> unzipEntries(InputStream is) throws IOException {

        Map<String, byte[]> entries = new HashMap<String, byte[]>();
        ZipInputStream zis = new ZipInputStream(is);

        ZipEntry singleEntry;
        while ((singleEntry = zis.getNextEntry()) != null) {
            byte data[] = new byte[BUFFER];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStream dest = new BufferedOutputStream(baos, BUFFER);
            int count;
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
            entries.put(singleEntry.getName(), baos.toByteArray());
        }
        return entries;
    }

}
