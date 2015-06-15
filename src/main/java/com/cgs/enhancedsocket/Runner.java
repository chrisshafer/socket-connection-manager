package com.cgs.enhancedsocket;

import com.cgs.enhancedsocket.client.EnhancedSocketClient;
import com.cgs.enhancedsocket.client.MessageListener;
import com.cgs.enhancedsocket.server.CommunicationDetails;
import com.cgs.enhancedsocket.server.EnhancedSocketServer;
import com.cgs.enhancedsocket.server.ServerMessageListener;

/**
 * Created by Chris on 3/8/14.
 */
public class Runner {

    private static final int SERVER_PORT = 4004;
    private static final String SERVER_HOSTNAME = "localhost";

    public static void main(String args[]){

        testClientCapacity(10);

    }
    public static void testClientCapacity(int number){
        for(int i=0; i<number; i++){
            testConnection();
        }
    }
    public static void testConnection(){
        EnhancedSocketClient enhancedSocketClient = new EnhancedSocketClient(SERVER_HOSTNAME, SERVER_PORT, 1000, new MessageListener() {
            @Override
            public void messageRecieved(String message) {
                LogHandler.log("CLIENT : MESSAGE RECIEVED : "+message);
            }

            @Override
            public void messageSent(String message){

            }
        });
        enhancedSocketClient.start();
        enhancedSocketClient.addMessage("Test");


    }
    public static void testShutdown(int secondsDelay, EnhancedSocketServer commandSocketServer){
        try{
            Thread.sleep(secondsDelay*1000);
            commandSocketServer.stopServer();
        }catch (InterruptedException e){

        }
    }
    public static void pingSystemStats(){
         /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " +
                Runtime.getRuntime().freeMemory());

        /* This will return Long.MAX_VALUE if there is no preset limit */
              long maxMemory = Runtime.getRuntime().maxMemory();
        /* Maximum amount of memory the JVM will attempt to use */
              System.out.println("Maximum memory (bytes): " +
                    (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

        /* Total memory currently in use by the JVM */
              System.out.println("Total memory (bytes): " +
                      Runtime.getRuntime().totalMemory());
    }
}
