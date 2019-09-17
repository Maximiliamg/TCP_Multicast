/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.distribuida.models;

import java.io.Serializable;

/**
 *
 * @author juan-
 */
public class FilePart implements Serializable{
    
    private String fileName;
    private int partNumber;
    private byte[] data;

    public FilePart(int partNumber, byte[] data, String fileName) {
        this.partNumber = partNumber;
        this.data = data;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }
    
    
    
}
