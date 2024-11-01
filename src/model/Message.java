/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 *
 * @author nguye
 */
public class Message implements Serializable{
    private String code; 
    private Object object;
    
    public Message(String code, Object object) {
        this.code = code;
        this.object = object;
    }
}
