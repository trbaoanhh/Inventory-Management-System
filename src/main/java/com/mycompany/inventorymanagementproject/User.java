/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementproject;

/**
 *
 * @author tranb
 */
public class User {
    private String name;
    private String password;
    private String email;
    
    public User() {
        name = null;
        password = null;
        email = null;
    }
    
    public User(String n, String pass) {
        name = n;
        password = pass;
        email = null;
    }
    
    public User(String n, String pass, String em) {
        name = n;
        password = pass;
        email = em;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public void setName(String x) {
        name = x;
    }
    public void setPassword(String x) {
        password = x;
    }
    public void setEmail(String x) {
        email = x;
    }
}
