package org.mule.modules.sendgrid;

import org.junit.Test;
import org.mule.tck.junit4.FunctionalTestCase;

public class SendGridTest extends FunctionalTestCase {

    @Override
    protected String getConfigFile() {
        return "flows.xml";
    }

    @Test
    public void testSendEmail() throws Exception {
        runFlowWithPayloadAndExpect("sendEmail", "{\"message\":\"success\"}", "");
    }
}
