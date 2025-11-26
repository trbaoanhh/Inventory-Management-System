/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementproject;

import java.sql.Connection;

/**
 *
 * @author tranb
 */
public class InventoryManagementProject {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        Connection conn = db.getConnection();
        // your startup code here
        LoginPage login = new LoginPage();
        login.setVisible(true);
    }
}
