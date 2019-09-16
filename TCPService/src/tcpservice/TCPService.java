/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpservice;

import com.prog.distribuida.comm.multicast.MulticastManager;
import com.prog.distribuida.comm.multicast.MulticastManagerCallerInterface;
import com.prog.distribuida.tcp.TCPServiceManager;
import com.prog.distribuida.tcp.TCPServiceManagerCallerInterface;
import java.net.Socket;

/**
 *
 * @author Administrador
 */
public class TCPService implements TCPServiceManagerCallerInterface, MulticastManagerCallerInterface{
    
    MulticastManager multicastManager;

    public TCPService(){
        new TCPServiceManager(9092,this);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TCPService();
    }
    @Override
    public void MessageReceiveFromClient(Socket clientSocket, byte[] data) {
    }

    @Override
    public void ErrorHasBeenThrown(Exception error) {
        
    }

    @Override
    public void MessageReceived(String sourceIpAddressOrHost, int sourcePort, byte[] data, int length) {
        
    }
    
}
