package org.mule.modules.sendgrid.model;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TemplateResolver {

    private Client client;

    public TemplateResolver(String user, String password){
        client = ClientBuilder.newClient().register(new Authenticator(user,password));
    }

    private WebTarget getWebTarget(){
        return client.target("https://api.sendgrid.com/v3/");
    }

    private String sendRequest(WebTarget webTarget) {
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        return response.readEntity(String.class);
    }

    public String getTemplateAsString(String tempID){

        WebTarget webTarget = getWebTarget().path("templates/").path(tempID);
        String output = sendRequest(webTarget);
        JSONArray versions = new JSONObject(output).getJSONArray("versions");

        for (int i = 0; i < versions.length(); i++) {
            JSONObject version = versions.getJSONObject(i);
            if(version.getInt("active") == 1){
                return version.getString("html_content");
            }
        }

        return null;
    }

    public Map<String,String> getTemplates(){

        HashMap<String, String> ids = new HashMap<String, String>();
        WebTarget webTarget = getWebTarget().path("templates");
        String output = sendRequest(webTarget);
        JSONArray templates = new JSONObject(output).getJSONArray("templates");

        for (int i = 0; i < templates.length(); i++) {
            ids.put(templates.getJSONObject(i).getString("name"), templates.getJSONObject(i).getString("id"));
        }

        return ids;
    }

    public class Authenticator implements ClientRequestFilter {

        private final String user;
        private final String password;

        public Authenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            MultivaluedMap<String, Object> headers = requestContext.getHeaders();
            final String basicAuthentication = getBasicAuthentication();
            headers.add("Authorization", basicAuthentication);
        }

        private String getBasicAuthentication() {
            String token = this.user + ":" + this.password;
            try {
                return "BASIC " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException("Cannot encode with UTF-8", ex);
            }
        }
    }
}
