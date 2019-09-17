/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.distribuida.tcp;

import com.prog.distribuida.models.FilePart;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Del Server
 * @author pjduque
 */
public class ClientSocketManager extends Thread {
        
    Socket clientSocket;
    ObjectInputStream reader;
    ObjectOutputStream writer;
    boolean isEnable = true;
    private TCPServiceManagerCallerInterface caller;
    
    boolean isNewConnection;
    
    private String serverIpAddress;
    private int port;
    
    final Object mutex = new Object();
    
    public void waitForAWhile() {
        try {
            synchronized(mutex) {
                mutex.wait();
            }
        }catch (Exception ex) {
            caller.errorHasBeenThrown(ex);
        }
    }
    
    public void notifyMutex() {
        try {
            synchronized(mutex) {
                mutex.notify();
            }
        }catch (Exception ex) {
            caller.errorHasBeenThrown(ex);
        }
    }
    
    public void restartConnection(){
        isNewConnection = true;
    }
    
    public ClientSocketManager(TCPServiceManagerCallerInterface caller) {
        this.caller = caller;
        this.start();
    }
    
    public ClientSocketManager(Socket clientSocket, TCPServiceManagerCallerInterface caller) {
        this.clientSocket = clientSocket;
        this.caller = caller;
        this.start();
    }
    
    public ClientSocketManager(String serverIpAddress, int port, TCPServiceManagerCallerInterface caller) {
        this.serverIpAddress = serverIpAddress;
        this.port = port;
        this.caller = caller;
        this.start();
    }

    public void assignSocketToThisThread(Socket socket) {
        this.clientSocket = socket;
        restartConnection();
        this.notifyMutex();
    }
    
    public boolean initializeSocket() {
        try {
            this.clientSocket = new Socket(serverIpAddress, port);
            return true;
        }catch (Exception ex) {
            caller.errorHasBeenThrown(ex);
        }
        return false;
    }
    
    public boolean initializeStreams() {
        try {
            if(clientSocket == null) {
                if(!initializeSocket()) {
                    return false;
                }
            }
            writer = new ObjectOutputStream(clientSocket.getOutputStream());
            reader = new ObjectInputStream(clientSocket.getInputStream());
            return true;
        }catch (Exception error) {
            error.printStackTrace();
            caller.errorHasBeenThrown(error);
        }
        return false;
    }

    @Override
    public void run() {
        try {
            while(isEnable) {
                if(clientSocket == null) {
                    this.waitForAWhile();
                }
                if(initializeStreams()) {
                    FilePart newMessage = null;
                    int parts = 0;
                    int currentPart = 0;

                    if(this.isNewConnection){
                        try {
                            Object part = this.reader.readObject();
                            FilePart filePart = (FilePart)part;
                            caller.messageReceiveFromClient(clientSocket, part);
                            parts = filePart.getPartNumber();
                            this.isNewConnection = false;
                        } catch (Exception e) {
                            caller.errorHasBeenThrown(e);
                            e.printStackTrace();
                        }
                    }
                    ArrayList<FilePart> file = new ArrayList<>();
                    boolean exit = false;
                    while(!exit) {
                        try {
                            newMessage = (FilePart)this.reader.readObject();
                            currentPart = newMessage.getPartNumber();
                            file.add(newMessage);
                            if(currentPart == (parts-1)) exit = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            exit = true;
                        }
                        
                    }
                    if(file.size() == parts){
                        for(FilePart fp:file){
                            caller.messageReceiveFromClient(clientSocket, fp);
                        }
                    }
                }
                clearLastSocket();
            }
        }catch (Exception error) {
            caller.errorHasBeenThrown(error);
        }
    }

    public void sendMessage(String message) {
        try {
            if(clientSocket.isConnected()) {
                writer.writeObject(message + "\n");
                writer.flush();
            }
        }catch (Exception error) {
            caller.errorHasBeenThrown(error);
        }
    }

    private void clearLastSocket() {
        try {
            writer.close();
        }catch (Exception ex) {
            caller.errorHasBeenThrown(ex);
        }
        try {
            reader.close();
        }catch (Exception ex) {
            caller.errorHasBeenThrown(ex);
        }
        try {
            clientSocket.close();
        }catch (Exception ex) {
            caller.errorHasBeenThrown(ex);
        }
        clientSocket = null;
    }
    
    public boolean isThisThreadBusy() {
        return clientSocket != null;
    }
    
}
