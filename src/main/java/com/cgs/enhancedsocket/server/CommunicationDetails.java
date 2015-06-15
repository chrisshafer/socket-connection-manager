package com.cgs.enhancedsocket.server;

/**
 * Created by Chris on 3/31/14.
 */
public class CommunicationDetails {
    private String clientId;
    private String message;

    public CommunicationDetails(String clientId, String message) {
        this.clientId = clientId;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getClientId() {
        return clientId;
    }
}
