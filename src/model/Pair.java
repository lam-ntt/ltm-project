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
public class Pair implements Serializable {
    private int id;
    private String image1, image2;

    public Pair() {
    }

    public Pair(String image1, String image2) {
        this.image1 = image1;
        this.image2 = image2;
    }

    public Pair(int id, String image1, String image2) {
        this.id = id;
        this.image1 = image1;
        this.image2 = image2;
    }

    public int getId() {
        return id;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }
}
