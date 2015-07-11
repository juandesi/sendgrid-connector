package org.mule.modules.sendgrid;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.apache.commons.lang.StringUtils;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.Text;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataStaticKey;
import org.mule.api.annotations.param.Optional;
import org.mule.modules.sendgrid.category.TemplateMetadataCategory;
import org.mule.modules.sendgrid.exception.SendGridConnectorException;
import org.mule.modules.sendgrid.strategy.SendGridConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Connector(name="send-grid", friendlyName="SendGrid")
public class SendGridConnector
{

    @Config
    SendGridConfiguration config;

    @Processor(friendlyName = "Send Simple EMail")
    public String sendText(List<String> to, String subject, @Text String text)
            throws SendGridConnectorException
    {
        SendGrid.Email email = buildBasicEmail(subject,to);
        email.setText(text);
        return sendMail(email);
    }

    @Processor(friendlyName = "Send Email From Customizable Template")
    @MetaDataScope(TemplateMetadataCategory.class)
    public Map<String, Object> sendFromTemplate(String to, String subject,
                                                @MetaDataStaticKey(type = "PlaceHolders") @Optional
                                                @Default("#[payload]") Map<String, Object> replacements)
            throws SendGridConnectorException
    {
        SendGrid.Email email = buildBasicEmail(subject,to);
        email.addBcc(to);
        email.addTo(to);
        String template;
        try {
            template = getTemplateAsString();
        } catch (IOException e) {
            throw new SendGridConnectorException(e);
        }

        for(String key : replacements.keySet()){
            String value = replacements.get(key).toString();
            template = template.replace(key,value);
        }

        email.setHtml(template);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("result",sendMail(email));
        return hm;
    }

    @Processor(friendlyName = "Send Email From Template")
    public String sendTemplateMail(List<String> to, String subject)
            throws SendGridConnectorException
    {
        SendGrid.Email email = buildBasicEmail(subject, to);
        try {
            email.setHtml(getTemplateAsString());
        } catch (IOException e) {
            throw new SendGridConnectorException(e);
        }

        return sendMail(email);
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

    private SendGrid.Email buildBasicEmail(String subject, List<String> to) {
        SendGrid.Email email = new SendGrid.Email();
        email.setFromName(config.getSenderName());
        email.addBcc(to.toArray(new String[to.size()]));
        email.setFrom(config.getEmail());
        email.setSubject(subject);
        return email;
    }

    private SendGrid.Email buildBasicEmail(String subject, String to) {
        return buildBasicEmail(subject,new ArrayList<>(Arrays.asList(to)));
    }

    public SendGridConfiguration getConfig() {
        return config;
    }

    public void setConfig(SendGridConfiguration config) {
        this.config = config;
    }

    public String getTemplateAsString() throws IOException {

        if(StringUtils.isEmpty(config.getTemplatePath())){
            throw new IOException("The HTML Template path is empty ");
        }

        StringBuilder builder = new StringBuilder();
        FileInputStream fis = new FileInputStream(config.getTemplatePath());
        int ch;
        while ((ch = fis.read()) != -1) {
            builder.append((char) ch);
        }
        fis.close();

        return  builder.toString();
    }
}