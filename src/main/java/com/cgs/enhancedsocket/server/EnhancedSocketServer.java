package com.cgs.enhancedsocket.server;

import com.cgs.enhancedsocket.LogHandler;
import com.cgs.enhancedsocket.Runner;
import com.cgs.enhancedsocket.UniqueIdentifierGenerator;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chris on 3/8/14.
 */
public class EnhancedSocketServer {

    private ServerSocket serverSocket;
    private ServerConnectionHandler serverConnectionHandler;
    private Map<String, ServerClient> clientMap;
    private int port;
    private ServerMessageListener serverMessageListener;

    public EnhancedSocketServer(int port, ServerMessageListener serverMessageListener){
        this.clientMap = Collections.synchronizedMap(new HashMap<String, ServerClient>());
        this.port = port;
        this.serverMessageListener = serverMessageListener;
    }
    public void startServer(){
        LogHandler.log("Starting Server");
        try {
            this.serverSocket = new ServerSocket(port);
            LogHandler.log("Created SocketServer at : " + serverSocket.getLocalPort());
            this.serverConnectionHandler = new ServerConnectionHandler(serverSocket,this);
            this.serverConnectionHandler.start();
        } catch (BindException e){
            LogHandler.logError("Port [" + port + "] in use, cannot start server");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public synchronized void stopServer(){

        if(this.serverConnectionHandler != null){
            this.serverConnectionHandler.stopConnectionHandler();
        }
        LogHandler.log("Closing clients");
        for(ServerClient serverClient : this.clientMap.values()){
            serverClient.stopClient();
        }

        LogHandler.log("Server Stopped");

    }
    public void addClient(Socket socket){
        Runner.pingSystemStats();
        ServerClient newServerClient = ServerClient.clientFactory(socket, createId(), this);
        this.clientMap.put(newServerClient.getClientIdentifier(), newServerClient);
        LogHandler.log("Added client: " + newServerClient.getClientIdentifier());
    }
    public synchronized void removeClient(String clientId){
        if(this.clientMap.containsKey(clientId)){
            this.clientMap.get(clientId).stopClient();
            this.clientMap.remove(clientId);
            LogHandler.log("client ["+clientId+"] disconnected");
        }
    }
    public ServerClient getClient(String clientID){
        return this.clientMap.get(clientID);
    }
    public ArrayList<String> getListOfClientIDs(){
        return new ArrayList<String>(this.clientMap.keySet());
    }
    public String createId(){
        String uniqueId = UniqueIdentifierGenerator.generateUniqueKey(32);
        while (this.clientMap.containsKey(uniqueId)){
            uniqueId = UniqueIdentifierGenerator.generateUniqueKey(32);
            LogHandler.logWarning("ServerClient ID of "+uniqueId+" exists, regenerating");
        }
        return uniqueId;
    }
    public void sendToAll(String message){
        for(ServerClient serverClient : clientMap.values()){
            serverClient.addMessage(message);
        }
    }
    public void messageRecieved(String clientId, String message){
        this.serverMessageListener.messageRecieved(new CommunicationDetails(clientId,message));
    }

}
