/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.sendgrid;

import org.mule.modules.tests.ConnectorTestCase;

import org.junit.Test;

public class SendGridConnectorTest extends ConnectorTestCase {
    
    @Override
    protected String getConfigResources() {
        return "send-grid-config.xml";
    }

    @Test
    public void testSendEmail() throws Exception {
    }
}
