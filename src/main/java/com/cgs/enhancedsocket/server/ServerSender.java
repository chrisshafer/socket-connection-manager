package com.cgs.enhancedsocket.server;


import com.cgs.enhancedsocket.LogHandler;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chris on 2/5/14.
 */
public class ServerSender extends Thread{
    private String clientId;
    private PrintWriter out;
    private List<String> messageQueue;
    private EnhancedSocketServer server;
    public ServerSender(OutputStream outputStream, String clientId, EnhancedSocketServer server){
        this.clientId = clientId;
        this.server = server;
        this.out = new PrintWriter(outputStream,true);
        this.messageQueue = Collections.synchronizedList(new ArrayList<String>());
    }
    private synchronized String getNextMessageFromQueue() throws InterruptedException
    {
        while (messageQueue.size()==0){
            wait();
        }
        String message = messageQueue.get(0);
        messageQueue.remove(0);
        return message;
    }
    public synchronized void addMessage(String message){
        this.messageQueue.add(message);
        this.notify();
    }
    private void sendMessageToClient(String mesage)
    {
        LogHandler.log("SERVER -> " + clientId + " : " + mesage);
        out.print(mesage+"\r\n");
        out.flush();
    }

    public void run()
    {
        try {
            while (!isInterrupted()) {
                String message = getNextMessageFromQueue();
                sendMessageToClient(message);
            }
            this.out.close();
        } catch (InterruptedException e){

        }finally {
            this.server.removeClient(this.clientId);
        }


    }
    public synchronized void close(){
        this.interrupt();
        this.out.close();

    }

}
