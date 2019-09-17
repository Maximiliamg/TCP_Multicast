/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.distribuida.main;

import com.prog.distribuida.models.FilePart;
import java.net.Socket;
import com.prog.distribuida.tcp.TCPServiceManager;
import com.prog.distribuida.tcp.TCPServiceManagerCallerInterface;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public class TCPService implements TCPServiceManagerCallerInterface {

    public TCPService() {
        new TCPServiceManager(9092, this);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TCPService();
    }

    @Override
    public void messageReceiveFromClient(Socket clientSocket, Object file) {
        //System.out.println(clientSocket.getInetAddress().getHostName() + ":" +  clientSocket.getPort() + ": " + new String(data));
    }

    @Override
    public void errorHasBeenThrown(Exception error) {
        
    }
    
}
