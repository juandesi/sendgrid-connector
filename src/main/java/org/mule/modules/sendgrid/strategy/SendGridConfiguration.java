package org.mule.modules.sendgrid.strategy;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.apache.cxf.common.util.StringUtils;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.*;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.api.annotations.param.Email;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.sendgrid.model.TemplateResolver;

/**
 * OAuth2 Connection Strategy
 *
 * @author MuleSoft, Inc.
 */
@ConnectionManagement(configElementName="config",friendlyName="Configuration")
public class SendGridConfiguration
{
	private SendGrid sendgrid;
    private TemplateResolver templateResolver;

    @Email
    @FriendlyName("Sender Email")
    @Required
    @Configurable
    private String email;


    @Configurable
    @Optional
    @FriendlyName("Sender Name")
    private String senderName;

    @Connect
    public void connect(@ConnectionKey String username, @Password String password)
        throws ConnectionException
    {
        sendgrid = new SendGrid(username, password);
        templateResolver = new TemplateResolver(username,password);
    }

    @TestConnectivity(label = "Send Test Email")
    public void testConnect(@ConnectionKey String username, @Password String password)
        throws ConnectionException
    {
        connect(username,password);
        SendGrid.Email testEmail = new SendGrid.Email();
        testEmail.setFromName("Sendgrid Connector")
                .setFrom(email)
                .setSubject("Testing Connection")
                .setText("Testing Connection")
                .addTo(email);

        try {
            if(!sendgrid.send(testEmail).getMessage().contains("success")){
                throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS,"","Incorrect User or Password");
            }
        } catch (SendGridException e) {
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN,"",e.getMessage());
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

    public String getSenderName() {
        return StringUtils.isEmpty(senderName)?email:senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TemplateResolver getTemplateResolver() {
        return templateResolver;
    }

    public void setTemplateResolver(TemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }
}