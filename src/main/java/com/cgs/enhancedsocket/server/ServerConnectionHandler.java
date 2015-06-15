package com.cgs.enhancedsocket.server;



import com.cgs.enhancedsocket.LogHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Chris on 2/5/14.
 */
public class ServerConnectionHandler extends Thread {

    private ServerSocket serverSocket;
    private EnhancedSocketServer server;
    public ServerConnectionHandler(ServerSocket serverSocket, EnhancedSocketServer server){
        this.server = server;
        this.serverSocket = serverSocket;

    }
    @Override
    public void run()
    {
        while (!isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                this.server.addClient(socket);

            }catch (SocketException e ){

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stopConnectionHandler(){
        this.interrupt();
        try{
            LogHandler.log("Attempting to close Server Socket");
            serverSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
