package org.mule.modules.sendgrid.model;

public class SendGridException extends Exception {
    public SendGridException(Exception e) {
        super(e);
    }
}
