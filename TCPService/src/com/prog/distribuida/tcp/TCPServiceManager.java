/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.distribuida.tcp;

import com.prog.distribuida.comm.multicast.MulticastManager;
import com.prog.distribuida.comm.multicast.MulticastManagerCallerInterface;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author Administrador
 */
public class TCPServiceManager extends Thread implements TCPServiceManagerCallerInterface, MulticastManagerCallerInterface{
    
    ServerSocket serverSocket;
    MulticastManager multicastManager;
    private int port;
    private TCPServiceManagerCallerInterface caller;
    boolean isEnabled=true;
    Vector<ClientSocketManager> clients=new Vector<ClientSocketManager>();
    final int NUMBER_OF_THREADS=50;

    public TCPServiceManager(int port, 
            TCPServiceManagerCallerInterface caller) {
        this.port = port;
        this.caller = caller;
        InitializeMulticastManager();
        initializeThreads();
        this.start();
    }
    
    public void initializeThreads(){
        try{
            for(int index=0;index<NUMBER_OF_THREADS;index++){
                clients.add(new ClientSocketManager(this));
            }
        }catch (Exception ex) {
            
        }
    }
    
    public ClientSocketManager getNotBusyClientSocketManager(){
        try{
            for(ClientSocketManager current: this.clients){
                if(current!=null){
                    if(!current.isThisThreadBusy()){
                        return current;
                    }
                }
            }
        }catch (Exception ex) {
            
        }
        return null;
    }
    
    public void SendMessageToAllClients(String message){
        for(ClientSocketManager current : clients){
            if(current!=null){
                current.SendMessage(message);
            }
        }
    }
    
    public void sendMessage(String message, ClientSocketManager manager){
        manager.SendMessage(message);
    }
    
    public void InitializeMulticastManager(){
        try{
            if(multicastManager==null)
            this.multicastManager=new MulticastManager(
                    "224.0.0.2",
                    9090, this);
        }catch(Exception error){
            
        }
    }
    
    @Override
    public void run(){
        try{
            this.serverSocket=new ServerSocket(port);
            while(this.isEnabled){
                //clients.add( new ClientSocketManager( 
                  //      serverSocket.accept(),this));
                Socket receivedSocket=serverSocket.accept();
                ClientSocketManager freeClientSocketManager=
                        getNotBusyClientSocketManager();
                if(freeClientSocketManager!=null){
                    freeClientSocketManager.assignSocketToThisThread(receivedSocket);
                }else{
                    try{
                        receivedSocket.close();
                    }catch(Exception error){
                        
                    }
                }
            }
        }catch(Exception error){
            this.caller.ErrorHasBeenThrown(error);
        }
    }

    @Override
    public void MessageReceiveFromClient(Socket clientSocket, byte[] data) {
//        SendMessageToAllClients(
//                                clientSocket.getInetAddress().
//                                getHostName()+":"+clientSocket.getPort()
//                                +": "+new String(data));
        multicastManager.SendThisMessage(data);
//        System.out.println(clientSocket.getInetAddress().getHostName()
//                +":"+clientSocket.getPort()+": "+new String(data));
    }

    @Override
    public void ErrorHasBeenThrown(Exception error) {
        
    }

    @Override
    public void MessageReceived(String sourceIpAddressOrHost, int sourcePort, byte[] data, int length) {
//        System.out.println("ESTO ES DEL MULTICAST"+sourceIpAddressOrHost+":"+sourcePort+" - "+
//                new String(data, 0, length)+"\n");
    }
    
    
    
    
    
}
