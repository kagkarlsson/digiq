package no.bekk.digiq.routes;

import no.bekk.digiq.handlers.CreateDigipostZip;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.springframework.stereotype.Component;

@Component
public class DigiqFileFilter<T> implements GenericFileFilter<T> {
    public boolean accept(GenericFile<T> file) {
        return file.getFileName().startsWith(CreateDigipostZip.DIGIQ);
    }
}
