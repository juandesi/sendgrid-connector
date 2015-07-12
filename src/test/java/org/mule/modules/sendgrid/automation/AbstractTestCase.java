package org.mule.modules.sendgrid.automation;

import org.junit.Before;
import org.mule.modules.sendgrid.SendGridConnector;
import org.mule.tools.devkit.ctf.mockup.ConnectorDispatcher;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

public abstract class AbstractTestCase {
	
	private SendGridConnector connector;
	private ConnectorDispatcher<SendGridConnector> dispatcher;

	protected SendGridConnector getConnector() {
		return connector;
	}
	protected ConnectorDispatcher<SendGridConnector> getDispatcher() {
		return dispatcher;
	}

	@Before
	public void init() throws Exception {
        ConnectorTestContext.initialize(SendGridConnector.class, false);
		ConnectorTestContext<SendGridConnector> context = ConnectorTestContext.getInstance(SendGridConnector.class);
        dispatcher = context.getConnectorDispatcher();
		connector = dispatcher.createMockup();
	}

}
