package com.cgs.enhancedsocket.client;

import com.cgs.enhancedsocket.LogHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;

public class ClientListener extends Thread {

		private BufferedReader in;
        private EnhancedSocketClient enhancedSocketClient;
        private MessageListener messageListener;
		public ClientListener(BufferedReader in, EnhancedSocketClient enhancedSocketClient ) {
            this.enhancedSocketClient = enhancedSocketClient;
			this.in = in;
		}

		public void run() {
            try {
                while(!isInterrupted()){
                    String message = in.readLine();
                    if (message == null){
                        break;
                    }
                    this.enhancedSocketClient.messageRecieved(message);
                }
            } catch (SocketException e){
                LogHandler.logWarning("Lost connection to server");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                enhancedSocketClient.stopClient();
                LogHandler.logWarning("lost connection to server");
                enhancedSocketClient.attemptToReconnect();
            }
        }
}