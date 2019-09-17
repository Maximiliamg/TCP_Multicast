/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.distribuida.tcp;

import com.prog.distribuida.models.FilePart;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author edangulo
 */
public interface TCPServiceManagerCallerInterface {
    
    public void messageReceiveFromClient(Socket clientSocket, Object file);
    public void errorHasBeenThrown(Exception error);
    
}
