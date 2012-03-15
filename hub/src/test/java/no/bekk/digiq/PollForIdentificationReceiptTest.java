package no.bekk.digiq;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import no.bekk.digiq.activities.ParseIdentificationReceipt;
import no.bekk.digiq.routes.PollForIdentificationReceipt;

import org.apache.camel.builder.NotifyBuilder;
import org.junit.Test;

public class PollForIdentificationReceiptTest extends DigiqCamelTestBase {

    @Resource
    private ParseIdentificationReceipt parseIdentificationReceipt;
    private PollForIdentificationReceipt routes;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        routes = new PollForIdentificationReceipt(parseIdentificationReceipt);
    }

    @Test
    public void testMessageToIdentificationShouldBeSentToSftpEndpoint() throws Exception {
        startCamel(routes);
        NotifyBuilder notify = new NotifyBuilder(context).whenDone(100).create();
        notify.matches(10, TimeUnit.SECONDS);
    }

}
