/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.sendgrid.strategy;

import com.sendgrid.SendGrid;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.*;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.param.ConnectionKey;

/**
 * OAuth2 Connection Strategy
 *
 * @author MuleSoft, Inc.
 */
@ConnectionManagement(configElementName="config",friendlyName="Configuration")
public class BasicAuthenticationStrategy
{

	private SendGrid sendgrid;
    private String fromEmail;
	
    @Connect
    @TestConnectivity(active= true)
    public void connect(@ConnectionKey String username, @Password String password, String email)
        throws ConnectionException {
        try{
        	sendgrid = new SendGrid(username, password);
            fromEmail = email;
        }catch(Exception e){
        	throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, "", e.getMessage());
        }
      
    }


    @Disconnect
    public void disconnect() {
    	sendgrid = null;
    }


    @ValidateConnection
    public boolean isConnected() {
        return sendgrid != null;
    }


    @ConnectionIdentifier
    public String connectionId() {
        return sendgrid.toString();
    }


	public SendGrid getSendgrid() {
		return sendgrid;
	}

	public void setSendgrid(SendGrid sendgrid) {
		this.sendgrid = sendgrid;
	}

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

}