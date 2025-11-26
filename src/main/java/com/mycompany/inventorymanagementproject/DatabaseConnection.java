/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author tranb
 */
public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:3306/inventorydb";
    private static final String username = "root";
    private static final String password = "1612200uU!";
    private static Connection con = null;
    
    public static Connection getConnection() {
    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
    
            con = DriverManager.getConnection(url,username,password);
            System.out.println("Connection Established Successfully");
        } catch (SQLException | ClassNotFoundException e ) {
            System.out.println(e);
            return null;
        } 
        return con;
    }
    
    
   
    
}
