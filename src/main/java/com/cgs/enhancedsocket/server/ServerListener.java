package com.cgs.enhancedsocket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;

/**
 * Created by Chris on 2/5/14.
 */
public class ServerListener extends Thread{

    private String clientId;
    private BufferedReader in;
    private EnhancedSocketServer server;
    public ServerListener(InputStream inputStream, String clientId, EnhancedSocketServer server){
        this.server = server;
        this.clientId = clientId;
        this.in = new BufferedReader(new InputStreamReader(inputStream));

    }
    public void run()
    {
        try {
            while (!isInterrupted()) {
                String message = in.readLine();
                if (message == null)
                    break;
                this.server.messageRecieved(this.clientId,message);
            }
            in.close();
        } catch (SocketException e){
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }

        this.server.removeClient(clientId);
    }

    public synchronized void close(){
        try{
            this.interrupt();
            this.in.read();
            this.in.close();
        }catch (SocketException e){

        }catch (IOException e){

        }

    }



}
