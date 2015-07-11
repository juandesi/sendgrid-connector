package org.mule.modules.sendgrid.category;

import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.*;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.sendgrid.SendGridConnector;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        keys.add(new DefaultMetaDataKey("PlaceHolders", "PlaceHolders"));
        return keys;
    }

    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        DynamicObjectBuilder<?> holders = builder.createDynamicObject(key.getId());
        for (String holder : getHolders()) {
            holders.addSimpleField(holder, DataType.STRING);
        }
        MetaDataModel model = builder.build();
        return new DefaultMetaData(model);
    }



    private Set<String> getHolders(){
        Set<String> holders = new HashSet<>();
        holders.add("User");
        holders.add("Name");
        holders.add("Name");
        return holders;
        //connector.getTemplateAsString();
    }

    public SendGridConnector getConnector() {
        return connector;
    }

    public void setConnector(SendGridConnector connector) {
        this.connector = connector;
    }
}
