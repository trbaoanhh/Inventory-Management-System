/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author tranb
 */
public class populateTable {
    private Connection conn = DatabaseConnection.getConnection();
    private DefaultTableModel prodTM = new DefaultTableModel();
    
    public DefaultTableModel populateProductTable() {
        String sql = "SELECT * FROM inventorydb.products ORDER BY dateAdded DESC";
        
        Vector colNames = new Vector();
        colNames.add("Product ID");
        colNames.add("Product Name");
        colNames.add("Amount");
        colNames.add("Date Added");
        colNames.add("Product Price");
        
        prodTM.setColumnIdentifiers(colNames);
//        Product[] rowData;
                
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
        
            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    int id = rs.getInt("itemID");
                    String name = rs.getString("itemName");
                    int amount = rs.getInt("itemAmount");
                    Timestamp date = rs.getTimestamp("dateAdded");
                    double price = rs.getDouble("itemPrice");
                    
            
                    Object[] rowData = {id,name,amount,date,price};
                    prodTM.addRow(rowData);
            
                }
            }      
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return prodTM;
    }
}
