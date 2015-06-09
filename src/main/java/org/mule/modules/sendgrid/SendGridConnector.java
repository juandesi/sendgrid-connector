/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.sendgrid;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;
import org.mule.modules.sendgrid.model.SendGrid;
import org.mule.modules.sendgrid.model.SendGridException;
import org.mule.modules.sendgrid.strategy.BasicAuthenticationStrategy;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name="send-grid", friendlyName="SendGrid")
public class SendGridConnector
{

    @ConnectionStrategy
    BasicAuthenticationStrategy connectionStrategy;

    @Processor
    public String sendMail(String to, String subject, @Default("#[payload]") String htmlContent) throws SendGridException {
        
    	SendGrid.Email email = new SendGrid.Email();
	    email.addTo(to);
	    email.setFrom(connectionStrategy.getFromEmail());
        email.setHtml(htmlContent);
	    email.setSubject(subject);
	    
	    SendGrid.Response response = connectionStrategy.getSendgrid().send(email);
	    return response.getMessage();
    }
    

    public BasicAuthenticationStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(BasicAuthenticationStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }
    
    

}