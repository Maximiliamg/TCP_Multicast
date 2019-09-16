/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.distribuida.comm.multicast;

/**
 *
 * @author asaad
 */
public interface MulticastManagerCallerInterface {
    
    public void MessageReceived(String sourceIpAddressOrHost,int sourcePort,byte[] data, int length);
    public void ErrorHasBeenThrown(Exception error);
    
}
