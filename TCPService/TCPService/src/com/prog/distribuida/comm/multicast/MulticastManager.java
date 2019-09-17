/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.distribuida.comm.multicast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asaad
 */
public class MulticastManager extends Thread{
    
    MulticastSocket multicastSocket;
    private String ipAddress;
    private int port;
    private MulticastManagerCallerInterface caller;
    private boolean isEnable=true;

    public MulticastManager(String ipAddress, int port, MulticastManagerCallerInterface caller) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.caller = caller;
        this.start();
    }
    
    public boolean InitializeMulticastSocket(){
        try{
            this.multicastSocket=new MulticastSocket(port);            
            InetAddress inetAddress=InetAddress.getByName(ipAddress);
            multicastSocket.joinGroup(inetAddress);
            return true;
        }catch (Exception ex) {
            caller.errorHasBeenThrownMulticast(ex);
            return false;
        }
    }
    
    
    @Override
    public void run(){
        DatagramPacket datagramPacket=new DatagramPacket(new byte[1500], 1500);
        if(InitializeMulticastSocket()){
            while(isEnable){
                try {
                    multicastSocket.receive(datagramPacket);
                    caller.messageReceivedMulticast(datagramPacket.getAddress().toString(),
                            datagramPacket.getPort(), datagramPacket.getData(), datagramPacket.getLength());
                } catch (IOException ex) {
                    caller.errorHasBeenThrownMulticast(ex);
                    Logger.getLogger(MulticastManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public boolean SendThisMessage(String destAddress,int dstPort,byte[] payload){
        try{
            DatagramPacket outgoingPacket=new DatagramPacket(payload, payload.length);
            outgoingPacket.setAddress(InetAddress.getByName(destAddress));
            outgoingPacket.setPort(dstPort);
            outgoingPacket.setData(payload);
            multicastSocket.send(outgoingPacket);
            return true;
        }catch (Exception ex) {
            caller.errorHasBeenThrownMulticast(ex);
            return false;
        }
    }
    
    public boolean SendThisMessage(Object part){
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(part);
            byte[] payload = baos.toByteArray();
            DatagramPacket outgoingPacket=new DatagramPacket(payload, payload.length);
            outgoingPacket.setAddress(InetAddress.getByName(ipAddress));
            outgoingPacket.setPort(port);
            outgoingPacket.setData(payload);
            multicastSocket.send(outgoingPacket);
            return true;
        }catch (Exception ex) {
            caller.errorHasBeenThrownMulticast(ex);
            return false;
        }
    }
    
    
    
}
