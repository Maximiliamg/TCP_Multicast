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
import java.util.Scanner;

/**
 *
 */
public class TCPService implements TCPServiceManagerCallerInterface {
    
    int PORT, MULTI_PORT;
    String ADRESS, line;

    public TCPService() {
        setTCPPort();
        new TCPServiceManager(PORT, this, MULTI_PORT, ADRESS);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TCPService();
    }
    
    public void setTCPPort(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the TCP port");
        line = scanner.nextLine();
        PORT = Integer.parseInt(line);
        System.out.println("Enter Multicast port");
        line = scanner.nextLine();
        MULTI_PORT = Integer.parseInt(line);
        System.out.println("Enter multicast adress");
        ADRESS = scanner.nextLine();
    }

    @Override
    public void messageReceiveFromClient(Socket clientSocket, Object file) {
        //System.out.println(clientSocket.getInetAddress().getHostName() + ":" +  clientSocket.getPort() + ": " + new String(data));
    }

    @Override
    public void errorHasBeenThrown(Exception error) {
        
    }
    
}
