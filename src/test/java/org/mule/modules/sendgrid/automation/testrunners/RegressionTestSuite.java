package org.mule.modules.sendgrid.automation.testrunners;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.sendgrid.SendGridConnector;
import org.mule.modules.sendgrid.automation.testcases.SendgridConnectorTestCases;
import org.mule.tools.devkit.ctf.junit.RegressionTests;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Categories.class)
@IncludeCategory(RegressionTests.class)
@SuiteClasses({
	SendgridConnectorTestCases.class
})

public class RegressionTestSuite {
	
	@BeforeClass
	public static void initialiseSuite(){
		ConnectorTestContext.initialize(SendGridConnector.class);
	}
	
	@AfterClass
    public static void shutdownSuite() {
        ConnectorTestContext.shutDown();
    }
	
}