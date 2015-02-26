/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.sendgrid;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.modules.sendgrid.strategy.ConnectorConnectionStrategy;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name="send-grid", friendlyName="SendGrid")
public class SendGridConnector
{

	
	private SendGrid client = null;
	
    @ConnectionStrategy
    ConnectorConnectionStrategy connectionStrategy;

    @Processor
    public String sendMail(String to, String from, String subject, @Default("#[payload]") String content) throws SendGridException {
        
    	SendGrid.Email email = new SendGrid.Email();
	    email.addTo(to);
	    email.setFrom(from);
	    email.setSubject(subject);
	    email.setText(content);
	    
	    SendGrid.Response response = null;
	    
	    try {
	         response = client.send(email);
	        System.out.println(response.getMessage());
	      }
	      catch (SendGridException e) {
	        System.err.println(e);
	      }
    	
	    return response.getMessage();
    }
    

    public ConnectorConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(ConnectorConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
        client = connectionStrategy.getClient();
        
    }

}