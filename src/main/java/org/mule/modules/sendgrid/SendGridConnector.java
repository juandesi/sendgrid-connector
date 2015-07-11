package org.mule.modules.sendgrid;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.MetaDataKeyParamAffectsType;
import org.mule.modules.sendgrid.category.TemplateMetadataCategory;
import org.mule.modules.sendgrid.exception.SendGridConnectorException;
import org.mule.modules.sendgrid.strategy.SendGridConfiguration;

import java.util.List;
import java.util.Map;

@Connector(name="send-grid", friendlyName="SendGrid")
public class SendGridConnector
{

    @Config
    SendGridConfiguration config;

    @Processor(friendlyName = "Send Simple Email")
    public String sendSimpleEmail(@FriendlyName("Subject") String subject,
                                    @FriendlyName("Send to") List<String> sendTo,
                                    @Default("#[payload]") String text)
            throws SendGridConnectorException
    {
        SendGrid.Email email = buildBasicEmail(subject, sendTo);
        email.setText(text).setHtml(text);
        return sendMail(email);
    }


    @Processor(friendlyName = "Send Email From Template")
    @MetaDataScope(TemplateMetadataCategory.class)
    public String sendFromTemplate(@FriendlyName("Template ID") @MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.INPUT) String key,
                                    @FriendlyName("Subject") String subject,
                                    @FriendlyName("Send to") List<String> sendTo,
                                    @FriendlyName("Substitutions") @Default("#[payload]") Map<String, Object> substitutions)
            throws SendGridConnectorException
    {
        SendGrid.Email email = buildBasicEmail(subject, sendTo).setTemplateId(key);
        for(String substitution : substitutions.keySet()){
            if(substitution.equals("body")){
                email.setHtml(substitution).setText(substitution);
            }else {
                substitution = formatSubstitutionKey(substitution);
                email.addSubstitution(substitution, new String[]{substitutions.get(substitution).toString()});
            }
        }
        return sendMail(email);
    }

    private String formatSubstitutionKey(String key) {
        return "&lt;%"+key+"%&gt;>";
    }

    private SendGrid.Email buildBasicEmail(String subject, List<String> sendTo) {
        return new SendGrid.Email()
                .setFromName(config.getSenderName())
                .addBcc(sendTo.toArray(new String[sendTo.size()]))
                .setFrom(config.getEmail())
                .setSubject(subject);
    }

    private String sendMail(SendGrid.Email email)
            throws SendGridConnectorException
    {
        try {
            return config.getSendgrid().send(email).getMessage();
        } catch (SendGridException e) {
            throw new SendGridConnectorException(e);
        }
    }

    public SendGridConfiguration getConfig() {
        return config;
    }

    public void setConfig(SendGridConfiguration config) {
        this.config = config;
    }
}