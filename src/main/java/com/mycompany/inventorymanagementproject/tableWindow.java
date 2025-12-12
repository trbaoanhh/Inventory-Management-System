/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.inventorymanagementproject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author tranb
 */
public class tableWindow extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(tableWindow.class.getName());
    private populateTable popu = new populateTable();

    
    private Connection conn = DatabaseConnection.getConnection();
    
    private void clearList() {
        String sql = "DELETE FROM inventorydb.currentList";
        
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private DefaultTableModel populateList() {
        DefaultTableModel dm = new DefaultTableModel();
        
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
    
    private void addToList(String name, int amount) {//fix this so that if item already exists, we just update the total cost.
        
        //maybe use the sql below to find item price of the product (if it already exists)
        //SELECT itemPrice FROM products WHERE EXISTS (SELECT Product FROM currentList WHERE currentList.Product = products.itemName);
        //however, this wouldn't really work for a specific row.
        
        
        String ifAlreadyExists = "SELECT COUNT(*) FROM currentList WHERE Product = ?"; //will return 0 if no such product is found
        try (PreparedStatement stmt = conn.prepareStatement(ifAlreadyExists)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            
            //fetching price
            int price = 0;
            String getPrice = "SELECT itemPrice FROM products WHERE itemName = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(getPrice)) {
                stmt2.setString(1, name);
                ResultSet rs2 = stmt2.executeQuery();

                if (rs2.next()) {
                    price = rs2.getInt("itemPrice");
                }
            }
            
            //fetching amount
            int currentAmount = 0;
            String getAmount = "SELECT Amount FROM currentList WHERE Product = ?";
            try (PreparedStatement stmt2 = conn.prepareStatement(getAmount)) {
                stmt2.setString(1, name);
                ResultSet rs2 = stmt2.executeQuery();

                if (rs2.next()) {
                    currentAmount = rs2.getInt("Amount");
                }
            }
            
            
            if (rs.next()) {
                //if the producvt is already available, then keep everything the same and only update the total cost.
                // I will do this by fetching the price using a seperate query, and then updating the table.
                if (rs.getInt("COUNT(*)") != 0) {
                    
                    
                    //updating
                    String update = "UPDATE currentList SET TotalCost = ? * ?, Amount = ?+? WHERE Product = ?";
                    try (PreparedStatement stmt2 = conn.prepareStatement(update)) {
                        stmt2.setInt(1, amount);
                        stmt2.setInt(2,price);
                        stmt2.setInt(3, amount);
                        stmt2.setInt(4, currentAmount);
                        stmt2.setString(5, name);
                        stmt2.executeUpdate();
                        DefaultTableModel dm =populateList();
                        currentProductList.setModel(dm);
                    }
                    
                }
                //if product is not available, I will add a new row to currentList
                else if (rs.getInt("COUNT(*)") ==0) {
                    String insert = "INSERT INTO inventorydb.currentList (Product,Amount,TotalCost) VALUES (?,?,?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insert)){

                        pstmt.setString(1,name);
                        pstmt.setInt(2,amount);
                        pstmt.setInt(3,price*amount);
                        pstmt.executeUpdate();
                        DefaultTableModel dm =populateList();
                        currentProductList.setModel(dm);
                    } 
                }
            }
            
            
        } catch (SQLException ex) {
            System.getLogger(tableWindow.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
       
        
        
//        
//        
//        
//        int price = 0;
//        //Check if product is already available in the currentList
//        String sql1 = "SELECT ? from currentList";
//        try (PreparedStatement stmt = conn.prepareStatement(sql1)) {
//            stmt.setString(1,name);
//            ResultSet rs = stmt.executeQuery();    
//        
//            if (rs.next()) {//if the product IS already available in currentList
//                String sql2 = "UPDATE currentList JOIN products ON products.itemName = currentList.Product SET currentList.TotalCost = currentList.Amount * products.itemPrice"; //DO THISSS
//                
//                try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
////                    ResultSet rs2 = stmt2.executeQuery();
////                    if (rs2.next()) {
////                        price = rs2.getInt("itemPrice");
////                    }
////                    DefaultTableModel dm =populateList();
////                    currentProductList.setModel(dm);
//                }
//            } else {//if product is not in list
//                String sql2 = "SELECT * FROM products";
//                try (PreparedStatement stmt2 = conn.prepareStatement(sql2)) {
//                    try (ResultSet rs2 = stmt2.executeQuery()) {
//                        if (rs2.next()) {
//                            price = rs2.getInt("itemPrice");
//                        }
//                    }
//                }
//                String sql = "INSERT INTO inventorydb.currentList (Product,Amount,TotalCost) VALUES (?,?,?)";
//                try (PreparedStatement pstmt = conn.prepareStatement(sql)){
//
//                    pstmt.setString(1,name);
//                    pstmt.setInt(2,amount);
//                    pstmt.setInt(3,price*amount);
//            
//            
//                    pstmt.executeUpdate();
//    
//                } catch (SQLException e){
//                    e.printStackTrace();
//                }
//                    }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        DefaultTableModel dm = populateList();
//        currentProductList.setModel(dm);
    } 
    
    private void removeFromList(String name) {
        String sql = "DELETE FROM invetorydb.currentList WHERE Product = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,name);
            
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private double totalUpList() {
        
        String sql = "SELECT itemPrice FROM currentList";
        int total = 0;
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    total += rs.getInt("TotalCost");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
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
        
        DefaultTableModel dm = popu.populateProductTable();
        productTable.setModel(dm);
        
        clearList();
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
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchProductFieldKeyTyped(evt);
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
        productAmountField.setText("Product Ordered");
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
        this.dispose();
        
    }//GEN-LAST:event_backButonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_printButtonActionPerformed

    private void productTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseClicked
        // TODO add your handling code here:
        
        //Code gotten from: https://www.youtube.com/watch?v=3_QuB8heXkw
        int row = productTable.getSelectedRow();
        DefaultTableModel model = (DefaultTableModel)productTable.getModel();
        productNameField.setText(model.getValueAt(row,1).toString());

    }//GEN-LAST:event_productTableMouseClicked

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        String n = productNameField.getText();
        String a = productAmountField.getText();
        int amountint;
        if (n.equals("Product Name") &&a.equals("Product Ordered") ) {
            JOptionPane.showMessageDialog(this, "Please Enter a Product Name and Product Ordered","Input Error",JOptionPane.ERROR_MESSAGE);
        }
        else if (n.equals("Product Name")) {
            JOptionPane.showMessageDialog(this, "Please Enter a Product Name","Input Error",JOptionPane.ERROR_MESSAGE);
        } else if (a.equals("Product Ordered")) {
            JOptionPane.showMessageDialog(this, "Please Enter Product Ordered","Input Error",JOptionPane.ERROR_MESSAGE);
        }
        else {
            amountint = Integer.parseInt(a);
//            clearList();
            addToList(n,amountint);
            productNameField.setText("Product Name");
            productAmountField.setText("Product Ordered");
        }
        
        totalCostField.setText(Double.toString(this.totalUpList()));
        
        
    }//GEN-LAST:event_addButtonActionPerformed

    private void productAmountFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_productAmountFieldFocusGained
        // TODO add your handling code here:
        String text = productAmountField.getText();
        if (text.equals("Product Ordered")) {
            productAmountField.setText("");
        }
    }//GEN-LAST:event_productAmountFieldFocusGained

    private void searchProductFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchProductFieldKeyTyped
        // TODO add your handling code here: TEST IF SEARCH FIELD WORKS
        String text = searchProductField.getText();
        DefaultTableModel dm = new DefaultTableModel();
        String sql = "SELECT * FROM inventorydb.products WHERE itemName LIKE '%?%'";
        
        Vector colNames = new Vector();
        colNames.add("Product ID");
        colNames.add("Product Name");
        colNames.add("Amount");
        colNames.add("Date Added");
        
        dm.setColumnIdentifiers(colNames);
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,text);
            
            
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("itemID");
                    String name = rs.getString("itemName");
                    int amount = rs.getInt("itemAmount");
                    Timestamp date = rs.getTimestamp("dateAdded");
                    
                    Object[] rowData = {id,name,amount,date};
                    dm.addRow(rowData);
                    currentProductList.setModel(dm);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_searchProductFieldKeyTyped

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
        if (text.equals("")) {
            searchProductField.setText("Search product...");
        }
    }//GEN-LAST:event_searchProductFieldFocusLost

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        // TODO add your handling code here:
        String n = productNameField.getText();
        String a = productAmountField.getText();
        
        if (n.equals("Product Name") &&a.equals("Product Ordered") ) {
            JOptionPane.showMessageDialog(this, "No Product To Remove","Remove Error",JOptionPane.ERROR_MESSAGE);
        }else if (n.equals("Product Name") ||a.equals("Product Ordered") ) {
            JOptionPane.showMessageDialog(this, "Please select an available product from the available ","Remove Error",JOptionPane.ERROR_MESSAGE);
        } else {
            removeFromList(n);
        
            productNameField.setText("Product Name");
            productAmountField.setText("Product Ordered");
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
        if (text.equals("")) {
            productAmountField.setText("Product Ordered");
        }
    }//GEN-LAST:event_productAmountFieldFocusLost

    private void productNameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_productNameFieldFocusLost
        // TODO add your handling code here:
        String n = productNameField.getText();
        if (n.equals("")) {
            productNameField.setText("Product Name");
        }
    }//GEN-LAST:event_productNameFieldFocusLost

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
