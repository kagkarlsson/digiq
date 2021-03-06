package no.bekk.digiq.routes;

import org.apache.camel.builder.RouteBuilder;

public class SftpRoutes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:sftpToIdentification")
        .log("Uploading zip to Digipost SFTP.")
        .to("sftp://{{sftp.user}}@{{sftp.host}}:{{sftp.path}}/masseutsendelse"
             + "?privateKeyFile={{sftp.key.file}}&privateKeyFilePassphrase={{sftp.key.password}}&knownHostsFile={{known.hosts.file}}");

        from("sftp://{{sftp.user}}@{{sftp.host}}:{{sftp.path}}/masseutsendelse/kvittering"
                        + "?privateKeyFile={{sftp.key.file}}&privateKeyFilePassphrase={{sftp.key.password}}&knownHostsFile={{known.hosts.file}}"
                        + "&delete=true&readLock=changed&delay=5000&filter=#digiqFileFilter")
        .log("Downloading receipt from Digipost SFTP.")
        .to("direct:sftpPollForReceipt");

    }

}
