/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementproject;

import java.sql.Timestamp;

/**
 *
 * @author tranb
 */
public class Product {
    
    private int id;
    private String name;
    private int amount;
    private Timestamp date;
    
    public Product() {
        id = -1;
        name = null;
        amount = -1;
        date = null;
        
    }
    
    public Product(int i, String n, int a, Timestamp d) {
        id = i;
        name = n;
        amount = a;
        date = d;
    }
    
    public int getID() {
        return id;
    }
    
    public void setID(int i) {
        id = i;
    }
    public String getName(){
        return name;
    }
    public void setName(String n) {
        name = n;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int a) {
        amount = a;
    }
    
    public Timestamp getDate() {
        return date;
    }
    
    
    //MAYBE SHOULDN"T ALLOW DATE TO BE MODIFIED?
    public void setDate(Timestamp d) {
        date = d;
    }
}
