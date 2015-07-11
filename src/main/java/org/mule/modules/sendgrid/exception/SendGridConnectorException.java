package org.mule.modules.sendgrid.exception;

public class SendGridConnectorException extends Exception{

    public SendGridConnectorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendGridConnectorException(Throwable cause) {
        super(cause);
    }
}
