package org.mule.modules.sendgrid.category;

import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.sendgrid.SendGridConnector;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Category which can differentiate between input or output MetaDataRetriever
 */
@MetaDataCategory
public class  TemplateMetadataCategory {

    /**
     * If you have a service that describes the entities, you may want to use
     * that through the connector. Devkit will inject the connector, after
     * initializing it.
     */
    @Inject
    private SendGridConnector connector;

    /**
     * Retrieves the list of keys
     */
    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<>();
        Map<String, String> templates = connector.getConfig().getTemplateResolver().getTemplates();
        for (String template : templates.keySet()) {
            keys.add(new DefaultMetaDataKey(templates.get(template), template));
        }
        return keys;
    }

    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        String template = connector.getConfig().getTemplateResolver().getTemplateAsString(key.getId());
        DynamicObjectBuilder<?> holders = builder.createDynamicObject(key.getDisplayName());
        for (String holder : getHolders(template)) {
            holders.addSimpleField(holder, DataType.STRING);
        }
        return new DefaultMetaData(builder.build());
    }



    private Set<String> getHolders(String template){
        Set<String> holders = new HashSet<>();
        Pattern pattern = Pattern.compile("(&lt;|<)%\\S+%(&gt;|>)");
        Matcher matcher = pattern.matcher(template);
        while(matcher.find()){
            if(!matcher.group(1).equals("subject")){
                holders.add(matcher.group(1));
            }
            template = template.substring(template.indexOf(matcher.group()) + matcher.group().length());
            matcher = pattern.matcher(template);
        }
        return holders;
    }

    public SendGridConnector getConnector() {
        return connector;
    }

    public void setConnector(SendGridConnector connector) {
        this.connector = connector;
    }
}
