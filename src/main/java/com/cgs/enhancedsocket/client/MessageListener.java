package com.cgs.enhancedsocket.client;

/**
 * Created by Chris on 3/31/14.
 */
public interface MessageListener{
    void messageRecieved(String message);
    void messageSent(String message);
}
