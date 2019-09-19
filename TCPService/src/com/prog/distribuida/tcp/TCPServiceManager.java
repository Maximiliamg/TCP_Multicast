/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.distribuida.tcp;

import com.prog.distribuida.comm.multicast.MulticastManager;
import com.prog.distribuida.comm.multicast.MulticastManagerCallerInterface;
import com.prog.distribuida.models.FilePart;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 */
public class TCPServiceManager extends Thread implements TCPServiceManagerCallerInterface, MulticastManagerCallerInterface {

    ServerSocket serverSocket;
    int MULTI_PORT;
    String ADRESS;
    MulticastManager manager;
    private int port;
    private TCPServiceManagerCallerInterface caller;
    boolean isEnable = true;
    Vector<ClientSocketManager> clients = new Vector<ClientSocketManager>();
    final int NUMBER_OF_THREADS = 50;

    public TCPServiceManager(int port, TCPServiceManagerCallerInterface caller, int MULTI_PORT, String ADRESS) {
        this.port = port;
        this.caller = caller;
        this.ADRESS = ADRESS;
        this.MULTI_PORT = MULTI_PORT;
        this.manager = new MulticastManager(ADRESS, MULTI_PORT, this);
        initializeThreads();
        this.start();
    }

    public void initializeThreads() {
        try {
            for (int index = 0; index < NUMBER_OF_THREADS; index++) {
                clients.add(new ClientSocketManager(this));
            }
        } catch (Exception ex) {
            caller.errorHasBeenThrown(ex);
        }
    }

    public ClientSocketManager getNotBusyClientSocketManager() {
        try {
            for (ClientSocketManager current : clients) {
                if (current != null) {
                    if (!current.isThisThreadBusy()) {
                        return current;
                    }
                }
            }
        } catch (Exception ex) {
            caller.errorHasBeenThrown(ex);
        }
        return null;
    }

    public void sendMessageToAllClients(String message) {
        for (ClientSocketManager current : clients) {
            if (current != null) {
                current.sendMessage(message);
            }
        }
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            while (this.isEnable) {
                //clients.add(new ClientSocketManager(serverSocket.accept(), this));
                Socket receivedSocket = serverSocket.accept();
                ClientSocketManager freeClientSocketManager = getNotBusyClientSocketManager();
                if (freeClientSocketManager != null) {
                    freeClientSocketManager.assignSocketToThisThread(receivedSocket);
                } else {
                    try {
                        receivedSocket.close();
                    } catch (Exception error) {
                        caller.errorHasBeenThrown(error);
                    }
                }
            }
        } catch (Exception error) {
            this.caller.errorHasBeenThrown(error);
        }
    }

    @Override
    public void messageReceiveFromClient(Socket clientSocket, Object file) {
        //sendMessageToAllClients(clientSocket.getInetAddress().getHostName()  + ":" + clientSocket.getPort() + ": " + new String(data));
        System.out.println(file);
//        String fileName = file.get(0).getFileName();
//
//        try {
//            FileOutputStream fos = new FileOutputStream(fileName, true);
//            for (FilePart fp : file) {
//                fos.write(fp.getData());
//            }
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        manager.SendThisMessage(file);
        System.out.println("Recibido");
    }

    @Override
    public void errorHasBeenThrown(Exception error) {

    }
    
    @Override
    public void messageReceivedMulticast(String sourceIpAddressOrHost,int sourcePort,byte[] data, int length){
        
    }
    
    @Override
    public void errorHasBeenThrownMulticast(Exception error){
        
    }

}
