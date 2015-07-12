package org.mule.modules.sendgrid.automation.testcases;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mule.modules.sendgrid.automation.AbstractTestCase;
import org.mule.tools.devkit.ctf.junit.RegressionTests;

public class SendgridConnectorTestCases extends AbstractTestCase {

    @Test
    @Category({RegressionTests.class})
    public void testSendEmailFromTemplate() throws Exception {
    	//assertEquals(getConnector().getConfig().getEmail(), "juan.desimoni@mulesoft.com");
    }

    @Test
    @Category({RegressionTests.class})
    public void testSendBasicEmail() throws Exception {
        //assertEquals(getConnector().getConfig().getEmail(), "juan.desimoni@mulesoft.com");
    }

//    @Test
//    @Category({RegressionTests.class})
//    public void testMetaDataKeys() throws Exception {
//        TemplateMetadataCategory mocked = mock(TemplateMetadataCategory.class);
//        mocked.getMetaDataKeys()
//    }





}