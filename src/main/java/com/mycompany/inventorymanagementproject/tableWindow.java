/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.inventorymanagementproject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author tranb
 */
public class tableWindow extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(tableWindow.class.getName());
    private populateTable popu = new populateTable();
    private DefaultTableModel dm = new DefaultTableModel();

    
    private Connection conn = DatabaseConnection.getConnection();
    
    private void removeAllFromList() {
        String sql = "DELETE FROM inventorydb.currentList";
        
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void clearTable() {
        dm.setRowCount(0);
        productTable.setModel(dm);
    }
    
    private DefaultTableModel populateList() {
        dm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        
        String sql = "SELECT * FROM currentList;";
        Vector colNames = new Vector();
        colNames.add("Product");
        colNames.add("Amount");
        colNames.add("Total Cost");
        
        dm.setColumnIdentifiers(colNames);
        
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
        
            try(ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    String n = rs.getString("Product");
                    int a = rs.getInt("Amount");
                    double c = rs.getDouble("TotalCost");
                    
                    
            
                    Object[] rowData = {n,a,c};
                    dm.addRow(rowData);
            
                }
            }      
        
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dm;
    }
    
    private void addToList(String name, int amount) {
        String setSafetyModeOff = "SET SQL_SAFE_UPDATES = 0;";
        try (PreparedStatement stmt = conn.prepareStatement(setSafetyModeOff)) {
            stmt.executeUpdate();
        } catch (SQLException ex) { 
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        String ifAlreadyExists = "SELECT COUNT(*) AS cnt FROM products WHERE itemName = ?"; //will return 0 if no such product is found in inventory
        try (PreparedStatement stmt = conn.prepareStatement(ifAlreadyExists)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            
            
            int currentAmount = 0;
            double price = 0;
            if (rs.next()) {
                //fetching price + amount if product already exists in database
                if (rs.getInt("cnt") > 0) {
                    String getPrice = "SELECT itemPrice FROM products WHERE itemName = ?";
                    try (PreparedStatement stmt2 = conn.prepareStatement(getPrice)) {
                        stmt2.setString(1, name);
                        ResultSet rs2 = stmt2.executeQuery();

                        if (rs2.next()) {
                            price = rs2.getDouble("itemPrice");
                        }
                    }
                    
                } 
                
                
                //price != 0 means that the product exists in database
                if (price != 0) {
                    
                    //have to check if them item already exists in currentList
                    String existInList = "SELECT COUNT(*) AS count FROM currentList WHERE Product = ?";
                    try (PreparedStatement stmt2 = conn.prepareStatement(existInList)) {
                        stmt2.setString(1, name);
                        ResultSet rs2 = stmt2.executeQuery();
                        
                        //if product is available in stock, but is not in currentList yet then we add into the list
                        if (rs2.next()) {
                            if (rs2.getInt("count")==0){
                                String update = "INSERT currentList VALUES (?,?,?)";
                                try(PreparedStatement stmt3 = conn.prepareStatement(update)) {
                                    stmt3.setString(1, name);
                                    stmt3.setInt(2,amount);
                                    stmt3.setDouble(3,price*amount);
                                    stmt3.executeUpdate();
                                    dm =populateList();
                                    currentProductList.setModel(dm);

                                }
                            }
                            //if product is avaialble in stock and is already in currentList
                            else {
                                String getAmount = "SELECT Amount FROM currentList WHERE Product = ?";
                                try (PreparedStatement stmt3 = conn.prepareStatement(getAmount)) {
                                    stmt3.setString(1, name);
                                    ResultSet rs3 = stmt3.executeQuery();

                                    if (rs3.next()) {
                                        currentAmount = rs3.getInt("Amount");
                                    }
                                }
                                int newAmount = currentAmount + amount;
                                double newTotalCost = newAmount * price;
                                String update = "UPDATE currentList SET TotalCost = ?, Amount = ?  WHERE Product = ?";
                                try (PreparedStatement stmt3 = conn.prepareStatement(update)) {
                                    stmt3.setDouble(1,newTotalCost);
                                    stmt3.setInt(2, newAmount);
                                    stmt3.setString(3, name);
                                    stmt3.executeUpdate();
                                    dm =populateList();
                                    currentProductList.setModel(dm);

                                }
                            }
                        }
                    }
   
                }//if product does not already exist
                else {
                    JOptionPane.showMessageDialog(this, "This product does not exist in storage","NO PRODUCT",JOptionPane.ERROR_MESSAGE);
                }
                
            }
        } catch (SQLException ex) {
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }        
    } 
    
    private void removeFromList(String name) {
        String setSafetyModeOff = "SET SQL_SAFE_UPDATES = 0;";
        try (PreparedStatement stmt = conn.prepareStatement(setSafetyModeOff)) {
            stmt.executeUpdate();
        } catch (SQLException ex) { 
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        String sql = "DELETE FROM currentList WHERE Product = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,name);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dm =populateList();
        currentProductList.setModel(dm);
    }
    
    private double totalCostUpdate() {
        
        String sql = "SELECT SUM(TotalCost) as total FROM currentList;";
        double total = 0;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    total += rs.getDouble("total");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
    private int totalAmountUpdate() {
        String sql = "SELECT SUM(Amount) as total FROM currentList;";
        int total = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    total += rs.getInt("total");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
    
   //-------------------------------------------------------------------------------------------------------
    /**
     * Creates new form tableWindow
     */
    public tableWindow() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Error connecting to the database","Connection Error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        dm = popu.populateProductTable();
        productTable.setModel(dm);
        
        removeAllFromList();
        DefaultTableModel dm1 = populateList();
        currentProductList.setModel(dm1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backButon = new javax.swing.JButton();
        currentProductLabel = new javax.swing.JLabel();
        printButton = new javax.swing.JButton();
        searchProductField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        productNameField = new javax.swing.JTextField();
        productAmountField = new javax.swing.JTextField();
        productsInventory = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        currentProductList = new javax.swing.JTable();
        totalAmountField = new javax.swing.JTextField();
        totalCostField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(724, 493));

        backButon.setText("Back");
        backButon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButonActionPerformed(evt);
            }
        });

        currentProductLabel.setText("Current Products");

        printButton.setText("Print");
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        searchProductField.setText("Search product...");
        searchProductField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchProductFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchProductFieldFocusLost(evt);
            }
        });
        searchProductField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchProductFieldKeyReleased(evt);
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
        jScrollPane2.setViewportView(productTable);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        productNameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        productNameField.setText("Product Name");
        productNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                productNameFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                productNameFieldFocusLost(evt);
            }
        });

        productAmountField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        productAmountField.setText("Product Amount");
        productAmountField.setToolTipText("");
        productAmountField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                productAmountFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                productAmountFieldFocusLost(evt);
            }
        });

        productsInventory.setText("Products Inventory");

        currentProductList.setModel(new javax.swing.table.DefaultTableModel(
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
        currentProductList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentProductListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(currentProductList);

        totalAmountField.setText("Total Amount");

        totalCostField.setText("Total Cost");
        totalCostField.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(searchProductField, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                            .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(productAmountField)
                            .addComponent(productNameField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backButon, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(currentProductLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(productsInventory)
                .addGap(115, 115, 115))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(printButton)
                .addGap(18, 18, 18)
                .addComponent(totalAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalCostField, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(316, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(backButon, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentProductLabel)
                    .addComponent(productsInventory))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchProductField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(productNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(productAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(printButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(totalCostField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
                    .addComponent(totalAmountField, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backButonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButonActionPerformed
        // TODO add your handling code here:
        // Get rid of this current window
        this.dispose();
        
    }//GEN-LAST:event_backButonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_printButtonActionPerformed

    private void productTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseClicked
        // TODO add your handling code here:
        
        // when user select a product from the table on the right, the chosen product's name will be displayed
        // in the product name field.
        //Code gotten from: https://www.youtube.com/watch?v=3_QuB8heXkw
        int row = productTable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel)productTable.getModel();
        productNameField.setText(model.getValueAt(row,0).toString());

    }//GEN-LAST:event_productTableMouseClicked

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        String n = productNameField.getText();
        String a = productAmountField.getText();
        int amountint;
        
        // Check if input into product ordered is integer or not
        
        // Source - https://stackoverflow.com/a
        // Posted by Ruchira Gayan Ranaweera
        // Retrieved 2025-12-15, License - CC BY-SA 3.0

         try {
           int x = Integer.parseInt(a); 
         }catch(NumberFormatException e) {
           JOptionPane.showMessageDialog(this, "Please Enter ONLY Integer Values Into Products Ordered","Input Error",JOptionPane.ERROR_MESSAGE); 
           productAmountField.setText("");
         } 

        //Check if there are things entered into the fields, if not i want the default texts to display
        
        if (n.equals("Product Name") &&a.equals("Product Amount") ) {
            JOptionPane.showMessageDialog(this, "Please Enter a Product Name and Product Amounts","Input Error",JOptionPane.ERROR_MESSAGE);
        }
        else if (n.equals("Product Name")) {
            JOptionPane.showMessageDialog(this, "Please Enter a Product Name","Input Error",JOptionPane.ERROR_MESSAGE);
        } else if (a.equals("Product Amount")) {
            JOptionPane.showMessageDialog(this, "Please Enter Product Amount","Input Error",JOptionPane.ERROR_MESSAGE);
        } 
        else {
            //if all the validations are good, then i will add the item inputted inot the list on the left
            amountint = Integer.parseInt(a);
            addToList(n,amountint);
            productNameField.setText("Product Name");
            productAmountField.setText("Product Amount");
        }
        
        //update the total amount of products used and also the total cost
        totalAmountField.setText(Integer.toString(totalAmountUpdate()));
        totalCostField.setText(Double.toString(totalCostUpdate()));
        
        
    }//GEN-LAST:event_addButtonActionPerformed

    private void productAmountFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_productAmountFieldFocusGained
        // TODO add your handling code here:
        String text = productAmountField.getText();
        if (text.equals("Product Amount")) {
            productAmountField.setText("");
        }
    }//GEN-LAST:event_productAmountFieldFocusGained

    private void searchProductFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchProductFieldFocusGained
        // TODO add your handling code here:
        String text = searchProductField.getText();
        if (text.equals("Search product...")) {
            searchProductField.setText("");
        }
    }//GEN-LAST:event_searchProductFieldFocusGained

    private void searchProductFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchProductFieldFocusLost
        // TODO add your handling code here:
        String text = searchProductField.getText();
        if (text.isEmpty()) {
            searchProductField.setText("Search product...");
        }
    }//GEN-LAST:event_searchProductFieldFocusLost

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        // TODO add your handling code here:
        String n = productNameField.getText();
        String a = productAmountField.getText();
        
        System.out.println(a + n);
        
        if (n.equals("Product Name") &&a.equals("Product Amount") ) {
            JOptionPane.showMessageDialog(this, "No Product To Remove","Remove Error",JOptionPane.ERROR_MESSAGE);
        }else if (n.equals("Product Name") ||a.equals("Product Amount") ) {
            JOptionPane.showMessageDialog(this, "Please select an available product from the available ","Remove Error",JOptionPane.ERROR_MESSAGE);
        } else {
            removeFromList(n);
            productNameField.setText("Product Name");
            productAmountField.setText("Product Amount");
            totalAmountField.setText(Integer.toString(totalAmountUpdate()));
            totalCostField.setText(Double.toString(totalCostUpdate()));
        }
        
    }//GEN-LAST:event_removeButtonActionPerformed

    private void currentProductListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_currentProductListMouseClicked
        // TODO add your handling code here:
        
        //Code gotten from: https://www.youtube.com/watch?v=3_QuB8heXkw
        int row = currentProductList.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel)currentProductList.getModel();
        productNameField.setText(model.getValueAt(row,0).toString());
        productAmountField.setText(model.getValueAt(row,1).toString());
    }//GEN-LAST:event_currentProductListMouseClicked

    private void productNameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_productNameFieldFocusGained
        // TODO add your handling code here:
        String n = productNameField.getText();
        if (n.equals("Product Name")) {
            productNameField.setText("");
        }
    }//GEN-LAST:event_productNameFieldFocusGained

    private void productAmountFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_productAmountFieldFocusLost
        // TODO add your handling code here:
        String text = productAmountField.getText();
        if (text.isEmpty()) {
            productAmountField.setText("Product Amount");
        }
    }//GEN-LAST:event_productAmountFieldFocusLost

    private void productNameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_productNameFieldFocusLost
        // TODO add your handling code here:
        String n = productNameField.getText();
        if (n.isEmpty()) {
            productNameField.setText("Product Name");
        }
    }//GEN-LAST:event_productNameFieldFocusLost

    private void searchProductFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchProductFieldKeyReleased
        // TODO add your handling code here:
        String text = searchProductField.getText().trim();
        clearTable();
        if (text.isEmpty()) {
            dm = popu.populateProductTable();
        } else{
            dm = popu.populateSearchTable(text);
            
        }
        productTable.setModel(dm);
    }//GEN-LAST:event_searchProductFieldKeyReleased

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
        java.awt.EventQueue.invokeLater(() -> new tableWindow().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton backButon;
    private javax.swing.JLabel currentProductLabel;
    private javax.swing.JTable currentProductList;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton printButton;
    private javax.swing.JTextField productAmountField;
    private javax.swing.JTextField productNameField;
    private javax.swing.JTable productTable;
    private javax.swing.JLabel productsInventory;
    private javax.swing.JButton removeButton;
    private javax.swing.JTextField searchProductField;
    private javax.swing.JTextField totalAmountField;
    private javax.swing.JTextField totalCostField;
    // End of variables declaration//GEN-END:variables
}
