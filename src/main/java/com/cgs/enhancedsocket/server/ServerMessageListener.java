package com.cgs.enhancedsocket.server;

import com.cgs.enhancedsocket.server.CommunicationDetails;

/**
 * Created by Chris on 3/31/14.
 */
public interface ServerMessageListener {
    void messageRecieved(CommunicationDetails message);
}
