package com.cgs.enhancedsocket.client;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Chris on 2/16/14.
 */
public class ClientSender extends Thread{
    private PrintWriter out;
    private List<String> messageQueue;
    private EnhancedSocketClient enhancedSocketClient;
    public ClientSender(PrintWriter out, EnhancedSocketClient enhancedSocketClient)
    {
        this.out = out;
        this.enhancedSocketClient = enhancedSocketClient;
        this.messageQueue = Collections.synchronizedList(new ArrayList<String>());
    }

    public synchronized void addMessage(String message){
        this.messageQueue.add(message);
        this.notify();
    }
    private synchronized String getNextMessageFromQueue() throws InterruptedException
    {
        while (messageQueue.size()==0){
            wait();
        }
        String message = (String) messageQueue.get(0);
        messageQueue.remove(0);
        return message;
    }
    private void sendMessageToServer(String mesage)
    {
        out.print(mesage+"\r\n");
        out.flush();
    }
    public void run()
    {
        try {
            while (!isInterrupted()) {
                String message = getNextMessageFromQueue();
                this.sendMessageToServer(message);
            }
        }catch(InterruptedException e){

        }
    }
}
