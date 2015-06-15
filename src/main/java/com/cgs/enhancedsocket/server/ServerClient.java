package com.cgs.enhancedsocket.server;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Chris on 2/5/14.
 */
public class ServerClient {
    private Socket socket = null;
    private ServerListener listener = null;
    private ServerSender sender = null;
    private String clientIdentifier;


    private ServerClient(ServerSender sender, ServerListener listener, Socket socket, String clientIdentifier) {
        this.sender = sender;
        this.listener = listener;
        this.socket = socket;
        this.clientIdentifier = clientIdentifier;
    }
    public static ServerClient clientFactory(Socket socket, String clientIdentifier, EnhancedSocketServer server){
        try {
            ServerListener serverListener = new ServerListener(socket.getInputStream(),clientIdentifier, server);
            ServerSender serverSender = new ServerSender(socket.getOutputStream(),clientIdentifier, server);
            ServerClient serverClient = new ServerClient(serverSender,serverListener,socket, clientIdentifier);
            serverClient.startClient();
            return serverClient;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void startClient(){
        this.listener.start();
        this.sender.start();
    }
    public void stopClient(){
        try{
            this.sender.close();
            this.listener.close();
            this.socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public String getClientIdentifier() {
        return clientIdentifier;
    }
    public void addMessage(String message){
        this.sender.addMessage(message);
    }
    public void setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerSender getSender() {
        return sender;
    }

    public void setSender(ServerSender sender) {
        this.sender = sender;
    }

    public ServerListener getListener() {

        return listener;
    }

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }


}
