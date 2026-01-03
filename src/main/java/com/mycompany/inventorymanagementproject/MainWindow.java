/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.inventorymanagementproject;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author tranb
 */
public class MainWindow extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainWindow.class.getName());
    private Connection conn = DatabaseConnection.getConnection();
    private DefaultTableModel dm;
    private int amtK;
//    private int amt;
//    private int itemID;
//    private String itemName;
    
    private populateTable x = new populateTable();
   //fill ProductList with products, sorted by the latest added product
   
    
    /**
     * Creates new form MainWindowFrame
     */
    public MainWindow() {
        
        initComponents();
        // Source - https://stackoverflow.com/a
        // Posted by BackSlash, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-11-25, License - CC BY-SA 3.0

        this.setLocationRelativeTo(null);//center of the screen
        AddProductWindow.setPreferredSize(new Dimension(240,300));
        AddProductWindow.pack();
        AddProductWindow.setLocationRelativeTo(null);
        
        
        dm = x.populateProductTable();
        productTable.setModel(dm);
    }
    
    private void clearTable() {
        dm.setRowCount(0);
        productTable.setModel(dm);
    }
    
    private void reset() {
        clearTable();
        dm = x.populateProductTable();
        productTable.setModel(dm);
    }
    private String findPrice(String name) {
        String price = "Prior Price";
        String sql = "SELECT itemPrice FROM products WHERE itemName = ? ";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double x = rs.getDouble("itemPrice");
                    price = String.valueOf(x);
                } 
            }
        } catch (SQLException e) {
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        return price;
    }
    
    private void updatePrice(String name, String price) {
        String sql = "UPDATE products SET itemPrice = ? WHERE itemName = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1,Double.parseDouble(price));
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.getLogger(MainWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    } 
    private boolean productExists(String name) {
        String ifAlreadyExists = "SELECT itemAmount FROM products WHERE itemName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(ifAlreadyExists)) {
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            } 
        } catch (SQLException e) {
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        return false;
    }
    
    private void addToInventory(String name, int amount, double price) {
        String setSafetyModeOff = "SET SQL_SAFE_UPDATES = 0;";
        try (PreparedStatement stmt = conn.prepareStatement(setSafetyModeOff)) {
            stmt.executeUpdate();
        } catch (SQLException ex) { 
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        //find if the product is already in the inventory or not.
        //SQL statement will return 0 if no such product is found in inventory
        String ifAlreadyExists = "SELECT itemAmount FROM products WHERE itemName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(ifAlreadyExists)) {
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery()){
  
            if (rs.next()) {
                // Product exists ==> update amount
                    int currentAmount = rs.getInt("itemAmount");
                    String updateSql = "UPDATE products SET itemAmount = ? WHERE itemName = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, currentAmount + amount);
                        updateStmt.setString(2, name);
                        updateStmt.executeUpdate();
                    }
            } else {
                // Product does not exist ==> insert new row
                    String insertSql = "INSERT INTO products (itemName, itemAmount, itemPrice, dateAdded) VALUES (?,?,?,NOW())";
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                        insertStmt.setString(1, name);
                        insertStmt.setInt(2, amount);
                        insertStmt.setDouble(3, price);
                        insertStmt.executeUpdate();
                    }
                } 
            }
        } catch (SQLException ex) {
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AddProductWindow = new javax.swing.JDialog();
        insertButton = new javax.swing.JButton();
        nameTextField = new javax.swing.JTextField();
        amountTextField = new javax.swing.JTextField();
        backButton = new javax.swing.JButton();
        priceTextField = new javax.swing.JTextField();
        RemoveProductWindow = new javax.swing.JDialog();
        backButtonRW = new javax.swing.JButton();
        nameTextFieldRW = new javax.swing.JTextField();
        amountTextFieldRW = new javax.swing.JTextField();
        removeButtonRW = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        deleteButtonRW = new javax.swing.JButton();
        EditPriceWindow = new javax.swing.JDialog();
        backButtonEP = new javax.swing.JButton();
        itemNameTextField = new javax.swing.JTextField();
        PriorPriceTextField = new javax.swing.JTextField();
        PostPriceTextField = new javax.swing.JTextField();
        EnterButton = new javax.swing.JButton();
        SearchField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        newTableButton = new javax.swing.JButton();
        addProductButton = new javax.swing.JButton();
        removeProductButton = new javax.swing.JButton();
        editPriceButton = new javax.swing.JButton();

        AddProductWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                AddProductWindowWindowClosed(evt);
            }
        });

        insertButton.setText("Insert");
        insertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertButtonActionPerformed(evt);
            }
        });

        nameTextField.setText("Product Name");
        nameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextFieldFocusLost(evt);
            }
        });

        amountTextField.setText("Product Amount");
        amountTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                amountTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                amountTextFieldFocusLost(evt);
            }
        });

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        priceTextField.setText("Product Price");
        priceTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                priceTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                priceTextFieldFocusLost(evt);
            }
        });

        javax.swing.GroupLayout AddProductWindowLayout = new javax.swing.GroupLayout(AddProductWindow.getContentPane());
        AddProductWindow.getContentPane().setLayout(AddProductWindowLayout);
        AddProductWindowLayout.setHorizontalGroup(
            AddProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddProductWindowLayout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addGroup(AddProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
            .addGroup(AddProductWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddProductWindowLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(insertButton)
                .addGap(83, 83, 83))
        );
        AddProductWindowLayout.setVerticalGroup(
            AddProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AddProductWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButton)
                .addGap(18, 18, 18)
                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(amountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(priceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(insertButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        RemoveProductWindow.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                RemoveProductWindowWindowClosed(evt);
            }
        });

        backButtonRW.setText("Back");
        backButtonRW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonRWActionPerformed(evt);
            }
        });

        nameTextFieldRW.setText("Product Name");
        nameTextFieldRW.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextFieldRWFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextFieldRWFocusLost(evt);
            }
        });

        amountTextFieldRW.setText("Product Amount");
        amountTextFieldRW.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                amountTextFieldRWFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                amountTextFieldRWFocusLost(evt);
            }
        });

        removeButtonRW.setText("Remove");
        removeButtonRW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonRWActionPerformed(evt);
            }
        });

        selectAllButton.setText("Select All");
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        deleteButtonRW.setText("Delete From Inventory");
        deleteButtonRW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonRWActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout RemoveProductWindowLayout = new javax.swing.GroupLayout(RemoveProductWindow.getContentPane());
        RemoveProductWindow.getContentPane().setLayout(RemoveProductWindowLayout);
        RemoveProductWindowLayout.setHorizontalGroup(
            RemoveProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RemoveProductWindowLayout.createSequentialGroup()
                .addGroup(RemoveProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(RemoveProductWindowLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(RemoveProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(amountTextFieldRW, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameTextFieldRW, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(RemoveProductWindowLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backButtonRW, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RemoveProductWindowLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(RemoveProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(selectAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButtonRW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteButtonRW))
                .addGap(37, 37, 37))
        );
        RemoveProductWindowLayout.setVerticalGroup(
            RemoveProductWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RemoveProductWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButtonRW)
                .addGap(26, 26, 26)
                .addComponent(nameTextFieldRW, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(amountTextFieldRW, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectAllButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeButtonRW)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButtonRW)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        backButtonEP.setText("Back");
        backButtonEP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonEPActionPerformed(evt);
            }
        });

        itemNameTextField.setText("Product Name");
        itemNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                itemNameTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                itemNameTextFieldFocusLost(evt);
            }
        });

        PriorPriceTextField.setText("Prior Price");

        PostPriceTextField.setText("Enter Price");
        PostPriceTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PostPriceTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PostPriceTextFieldFocusLost(evt);
            }
        });

        EnterButton.setText("Enter");
        EnterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnterButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EditPriceWindowLayout = new javax.swing.GroupLayout(EditPriceWindow.getContentPane());
        EditPriceWindow.getContentPane().setLayout(EditPriceWindowLayout);
        EditPriceWindowLayout.setHorizontalGroup(
            EditPriceWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EditPriceWindowLayout.createSequentialGroup()
                .addGroup(EditPriceWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EditPriceWindowLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(backButtonEP, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EditPriceWindowLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(EditPriceWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PriorPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PostPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(EditPriceWindowLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(EnterButton)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        EditPriceWindowLayout.setVerticalGroup(
            EditPriceWindowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EditPriceWindowLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButtonEP)
                .addGap(18, 18, 18)
                .addComponent(itemNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PriorPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PostPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EnterButton)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });

        SearchField.setText("Search...");
        SearchField.setToolTipText("");
        SearchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SearchFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                SearchFieldFocusLost(evt);
            }
        });
        SearchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                SearchFieldKeyReleased(evt);
            }
        });

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(productTable);

        newTableButton.setText("New Table");
        newTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTableButtonActionPerformed(evt);
            }
        });

        addProductButton.setText("Add Product");
        addProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductButtonActionPerformed(evt);
            }
        });

        removeProductButton.setText("Remove Product");
        removeProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeProductButtonActionPerformed(evt);
            }
        });

        editPriceButton.setText("Edit Price");
        editPriceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPriceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SearchField)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newTableButton, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(removeProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editPriceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(SearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(newTableButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(addProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(removeProductButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editPriceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTableButtonActionPerformed
        // TODO add your handling code here:
        new tableWindow().setVisible(true);
//        this.setVisible(false);
    }//GEN-LAST:event_newTableButtonActionPerformed

    private void addProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductButtonActionPerformed
        // TODO add your handling code here:
//        this.setVisible(false);
//        new AddProductWindow().setVisible(true);
          AddProductWindow.setVisible(true);
//          this.setVisible(false);
        
    }//GEN-LAST:event_addProductButtonActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus

    }//GEN-LAST:event_formWindowGainedFocus

    private void insertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertButtonActionPerformed
        // TODO add your handling code here:
        String n = nameTextField.getText();
        String a = amountTextField.getText();
        String p = priceTextField.getText();
        if (n.equals("Product Name") || a.equals("Product Amount")) {
            JOptionPane.showMessageDialog(AddProductWindow, "Please fill out all fields.");
            
            return;
        }
        int amount;
        double price = 0;
        //check if input into product ordered is integer or not
        // Source - https://stackoverflow.com/a
        // Posted by Ruchira Gayan Ranaweera
        // Retrieved 2025-12-15, License - CC BY-SA 3.0

        try {
            amount = Integer.parseInt(a);
            if (!productExists(n)) {
                price = Double.parseDouble(p);
            }
        }catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(AddProductWindow, "Please Enter ONLY Integer Values Into Product Ordered And Double Values Into Product Price","Input Error",JOptionPane.ERROR_MESSAGE);
//            nameTextField.setText("");
            return;
        }
        //
        //         amount = Integer.parseInt(a);
        //         price = Double.parseDouble(p);
        addToInventory(n,amount,price);
        JOptionPane.showMessageDialog(AddProductWindow, "Insert Successful","Input Status",JOptionPane.INFORMATION_MESSAGE);
        nameTextField.setText("Product Name");
        amountTextField.setText("Product Amount");
        priceTextField.setText("Product Price");
        AddProductWindow.dispose();
        reset();

    }//GEN-LAST:event_insertButtonActionPerformed

    private void nameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldFocusGained
        // TODO add your handling code here:
        String n = nameTextField.getText();
        if (n.equals("Product Name")) {
            nameTextField.setText("");
        }
    }//GEN-LAST:event_nameTextFieldFocusGained

    private void nameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldFocusLost
        // TODO add your handling code here:
        String n = nameTextField.getText();
        if (n.isEmpty()) {
            nameTextField.setText("Product Name");
        }
    }//GEN-LAST:event_nameTextFieldFocusLost

    private void amountTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountTextFieldFocusGained
        // TODO add your handling code here:
        String n = amountTextField.getText();
        if (n.equals("Product Amount")) {
            amountTextField.setText("");
        }
    }//GEN-LAST:event_amountTextFieldFocusGained

    private void amountTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountTextFieldFocusLost
        // TODO add your handling code here:
        String n = amountTextField.getText();
        if (n.isEmpty()) {
            amountTextField.setText("Product Amount");
        }
    }//GEN-LAST:event_amountTextFieldFocusLost

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        nameTextField.setText("Product Name");
        amountTextField.setText("Product Amount");
        priceTextField.setText("Product Price");
        AddProductWindow.dispose();
//        new MainWindow().setVisible(true);
    }//GEN-LAST:event_backButtonActionPerformed

    private void priceTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_priceTextFieldFocusGained
        // TODO add your handling code here:
        String n = priceTextField.getText();
        if (n.equals("Product Price")) {
            priceTextField.setText("");
        }
    }//GEN-LAST:event_priceTextFieldFocusGained

    private void priceTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_priceTextFieldFocusLost
        // TODO add your handling code here:
        String n = priceTextField.getText();
        if (n.isEmpty()) {
            priceTextField.setText("Product Price");
        }
    }//GEN-LAST:event_priceTextFieldFocusLost

    private void AddProductWindowWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_AddProductWindowWindowClosed
        // TODO add your handling code here:
        nameTextField.setText("Product Name");
        amountTextField.setText("Product Amount");
        priceTextField.setText("Product Price");
        reset();
    }//GEN-LAST:event_AddProductWindowWindowClosed

    private void removeProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeProductButtonActionPerformed
        // TODO add your handling code here:
        RemoveProductWindow.setPreferredSize(new Dimension(240,300));
        RemoveProductWindow.pack();
        RemoveProductWindow.setLocationRelativeTo(null);
        RemoveProductWindow.setVisible(true);
    }//GEN-LAST:event_removeProductButtonActionPerformed

    private void editPriceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPriceButtonActionPerformed
        // TODO add your handling code here:
        EditPriceWindow.setPreferredSize(new Dimension(280,300));
        EditPriceWindow.pack();
        EditPriceWindow.setLocationRelativeTo(null);
        EditPriceWindow.setVisible(true);
    }//GEN-LAST:event_editPriceButtonActionPerformed

    private void SearchFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SearchFieldKeyReleased
        // TODO add your handling code here:
        String text = SearchField.getText().trim();
        clearTable();
        if (text.isEmpty()) {
            dm = x.populateProductTable();
        } else{
            dm = x.populateSearchTable(text);
            
        }
        productTable.setModel(dm);
    }//GEN-LAST:event_SearchFieldKeyReleased

    private void SearchFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchFieldFocusGained
        // TODO add your handling code here:
        String x = SearchField.getText();
        
        if (x.equals("Search...")) {
            SearchField.setText("");
        }
    }//GEN-LAST:event_SearchFieldFocusGained

    private void SearchFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_SearchFieldFocusLost
        // TODO add your handling code here:
        String x = SearchField.getText();
        if (x.isEmpty()) {
            SearchField.setText("Search...");
        }
    }//GEN-LAST:event_SearchFieldFocusLost

    private void backButtonRWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonRWActionPerformed
        // TODO add your handling code here:
        nameTextFieldRW.setText("Product Name");
        amountTextFieldRW.setText("Product Amount");
        RemoveProductWindow.dispose();
    }//GEN-LAST:event_backButtonRWActionPerformed

    private void nameTextFieldRWFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldRWFocusGained
        // TODO add your handling code here:
        String n = nameTextFieldRW.getText();
        
        if (n.equals("Product Name")) {
            nameTextFieldRW.setText("");
        }
    }//GEN-LAST:event_nameTextFieldRWFocusGained

    private void nameTextFieldRWFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFieldRWFocusLost
        // TODO add your handling code here:
        String n = nameTextFieldRW.getText();
        
        if (n.isEmpty()) {
            nameTextFieldRW.setText("Product Name");
        }
    }//GEN-LAST:event_nameTextFieldRWFocusLost

    private void removeButtonRWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonRWActionPerformed
        // TODO add your handling code here:
        
        int amount = Integer.parseInt(amountTextFieldRW.getText());
        String itemName = nameTextFieldRW.getText();
        int amtK = 0;
        
        String x = "SELECT itemAmount FROM products WHERE itemName = ?";
        try(PreparedStatement stmt = conn.prepareStatement(x)) {
            stmt.setString(1,itemName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    amtK = rs.getInt("ItemAmount");
                }
       
            }
        } catch (SQLException ex) {        
            System.getLogger(MainWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        if (amtK-amount < 0) {
            JOptionPane.showMessageDialog(RemoveProductWindow, "Amount Removed Exceeds Current Amount","Remove Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sql = "UPDATE inventorydb.products SET itemAmount = GREATEST(0,itemAmount - ?) WHERE itemName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1,amount);
            stmt.setString(2,itemName);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        RemoveProductWindow.dispose();
        
    }//GEN-LAST:event_removeButtonRWActionPerformed

    private void amountTextFieldRWFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountTextFieldRWFocusLost
        // TODO add your handling code here:
        String n = amountTextFieldRW.getText();

        if (n.isEmpty()) {
            amountTextFieldRW.setText("Product Amount");
        }
    }//GEN-LAST:event_amountTextFieldRWFocusLost

    private void amountTextFieldRWFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_amountTextFieldRWFocusGained
        // TODO add your handling code here:
        String n = amountTextFieldRW.getText();

        if (n.equals("Product Amount")) {
            amountTextFieldRW.setText("");
        }
    }//GEN-LAST:event_amountTextFieldRWFocusGained

    private void productTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseClicked
        // TODO add your handling code here:
//        int row = productTable.getSelectedRow();
//        DefaultTableModel model = (DefaultTableModel)productTable.getModel();
//        RemoveProductWindow.setPreferredSize(new Dimension(240,300));
//        RemoveProductWindow.pack();
//        RemoveProductWindow.setLocationRelativeTo(null);
//        RemoveProductWindow.setVisible(true);
//        nameTextFieldRW.setText(model.getValueAt(row,0).toString());
////        amt = (int) model.getValueAt(row,1);
//        amtstring = model.getValueAt(row,1).toString();
////        itemID = (int) model.getValueAt(row,0);
//itemName = model.getValueAt(row,0).toString();
    }//GEN-LAST:event_productTableMouseClicked

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        // TODO add your handling code here:
//        amountTextFieldRW.setText(amtstring);
        String n = nameTextFieldRW.getText();
        String sql = "SELECT itemAmount FROM products WHERE itemName = ?";
        int amount = 0;
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, n);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    amount = rs.getInt("itemAmount");
                }
                
            }
        } catch (SQLException ex) {
            System.getLogger(MainWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        String amt = Integer.toString(amount);
        amtK = amount;
        amountTextFieldRW.setText(amt);
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void deleteButtonRWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonRWActionPerformed
        // TODO add your handling code here:
        String n = nameTextFieldRW.getText();
        String sql = "DELETE FROM products WHERE itemName = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,n);
            stmt.executeQuery();
        } catch (SQLException ex) {
            System.getLogger(MainWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        RemoveProductWindow.dispose();
    }//GEN-LAST:event_deleteButtonRWActionPerformed

    private void RemoveProductWindowWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_RemoveProductWindowWindowClosed
        // TODO add your handling code here:
        nameTextFieldRW.setText("Product Name");
        amountTextFieldRW.setText("Product Amount");
//        System.out.println("Closed successfully");
        reset();
    }//GEN-LAST:event_RemoveProductWindowWindowClosed

    private void backButtonEPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonEPActionPerformed
        // TODO add your handling code here:
        PriorPriceTextField.setText("Prior Price");
        PostPriceTextField.setText("Enter Price");
        itemNameTextField.setText("Product");
        EditPriceWindow.dispose();
    }//GEN-LAST:event_backButtonEPActionPerformed

    private void itemNameTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemNameTextFieldFocusGained
        // TODO add your handling code here:
        String n = itemNameTextField.getText();

        if (n.equals("Product Name")) {
            itemNameTextField.setText("");
        } 
    }//GEN-LAST:event_itemNameTextFieldFocusGained

    private void itemNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_itemNameTextFieldFocusLost
        // TODO add your handling code here:
        String n = itemNameTextField.getText();

        if (n.isEmpty()) {
            itemNameTextField.setText("Product Name");
        } else {
            PriorPriceTextField.setText(findPrice(n));
        }
    }//GEN-LAST:event_itemNameTextFieldFocusLost

    private void PostPriceTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PostPriceTextFieldFocusGained
        // TODO add your handling code here:
        String n = PostPriceTextField.getText();

        if (n.equals("Enter Price")) {
            PostPriceTextField.setText("");
        }
    }//GEN-LAST:event_PostPriceTextFieldFocusGained

    private void PostPriceTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PostPriceTextFieldFocusLost
        // TODO add your handling code here:
        String n = PostPriceTextField.getText();

        if (n.isEmpty()) {
            PostPriceTextField.setText("Enter Price");
        }
    }//GEN-LAST:event_PostPriceTextFieldFocusLost

    private void EnterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnterButtonActionPerformed
        // TODO add your handling code here:
        String n = itemNameTextField.getText();
        String p = PostPriceTextField.getText();
        updatePrice(n,p);
        
        PriorPriceTextField.setText("Prior Price");
        PostPriceTextField.setText("Enter Price");
        itemNameTextField.setText("Product");
        EditPriceWindow.dispose();
        reset();
    }//GEN-LAST:event_EnterButtonActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainWindow().setVisible(true));
    }
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog AddProductWindow;
    private javax.swing.JDialog EditPriceWindow;
    private javax.swing.JButton EnterButton;
    private javax.swing.JTextField PostPriceTextField;
    private javax.swing.JTextField PriorPriceTextField;
    private javax.swing.JDialog RemoveProductWindow;
    private javax.swing.JTextField SearchField;
    private javax.swing.JButton addProductButton;
    private javax.swing.JTextField amountTextField;
    private javax.swing.JTextField amountTextFieldRW;
    private javax.swing.JButton backButton;
    private javax.swing.JButton backButtonEP;
    private javax.swing.JButton backButtonRW;
    private javax.swing.JButton deleteButtonRW;
    private javax.swing.JButton editPriceButton;
    private javax.swing.JButton insertButton;
    private javax.swing.JTextField itemNameTextField;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField nameTextFieldRW;
    private javax.swing.JButton newTableButton;
    private javax.swing.JTextField priceTextField;
    private javax.swing.JTable productTable;
    private javax.swing.JButton removeButtonRW;
    private javax.swing.JButton removeProductButton;
    private javax.swing.JButton selectAllButton;
    // End of variables declaration//GEN-END:variables
}
