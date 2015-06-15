package com.cgs.enhancedsocket.client;

import com.cgs.enhancedsocket.LogHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Chris on 2/16/14.
 */
public class EnhancedSocketClient extends Thread {
    private String serverHostname;
    private int serverPort;
    private ClientSender clientSender;
    private ClientListener clientListener;
    private int reconnectInterval;
    private boolean isConnected;
    private MessageListener messageListener;
    private ConnectionListener connectionListener;
    private ArrayList<String> heldOutput;

    public EnhancedSocketClient(String serverHostname, int serverPort, int reconnectInterval, MessageListener messageListener){
        this.serverHostname = serverHostname;
        this.serverPort = serverPort;
        this.reconnectInterval = reconnectInterval;
        this.isConnected = false;
        this.messageListener = messageListener;
        this.heldOutput = new ArrayList<String>();
    }
    public void run(){
        startClient();
    }
    public void startClient(){
        if(!this.isConnected){
            this.isConnected = false;
            BufferedReader in = null;
            PrintWriter out = null;
            Socket socket = null;
            try {
                socket = new Socket(serverHostname, serverPort);
                in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter( new OutputStreamWriter(socket.getOutputStream()));

            } catch (IOException ioe) {
                LogHandler.logError("Can not establish connection to " + serverHostname + ":" + serverPort);
                return;
            }
            LogHandler.log("Connected to server " + serverHostname + ":" + serverPort);
            if(this.connectionListener != null){
                this.connectionListener.connectionChanged(true);
            }
            this.clientSender = new ClientSender(out, this);
            this.clientSender.start();
            this.catchUpOutput();
            this.clientListener = new ClientListener(in, this);
            this.clientListener.start();
            this.isConnected = true;
        }
    }
    public void stopClient(){
        this.clientListener.interrupt();
        this.clientSender.interrupt();
        this.isConnected = false;
    }
    public void addMessage(String message){
        if(this.clientSender != null){
            this.clientSender.addMessage(message);
        }else{
            this.heldOutput.add(message);
            LogHandler.logWarning("Client not started !");
        }

    }
    public void catchUpOutput(){
        for(String message : heldOutput){
            this.addMessage(message);
        }
    }
    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public void messageRecieved(String message){
        this.messageListener.messageRecieved(message);
    }
    public void attemptToReconnect(){
        if(connectionListener != null){
            this.connectionListener.connectionChanged(false);
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ReconnectTask(),3*1000,this.reconnectInterval);
    }

    public class ReconnectTask extends TimerTask {
        @Override
        public void run() {
            //TO-DO Illegal thread state exception
            LogHandler.log("Attempting to reconnect");
            startClient();
            if(isConnected){
                if(connectionListener != null){
                    connectionListener.connectionChanged(true);
                }
                LogHandler.log("Reconnected");
                this.cancel();
            }
        }
    }
}
