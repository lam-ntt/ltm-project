/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.*;
import java.io.Serializable;

/**
 *
 * @author nguye
 */
public class Message implements Serializable{
    private String code; 
    private Object object;

    public Message(String code) {
        this.code = code;
    }
    
    public Message(String code, Object object) {
        this.code = code;
        this.object = object;
    }

    public String getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }
}
