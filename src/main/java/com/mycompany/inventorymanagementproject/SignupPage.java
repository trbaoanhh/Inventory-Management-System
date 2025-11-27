/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.inventorymanagementproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author tranb
 */
public class SignupPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SignupPage.class.getName());
    private User user = new User();
    private Connection conn = DatabaseConnection.getConnection();

    /**
     * Creates new form SignupPage
     */
    public SignupPage() {
        initComponents();
        // Source - https://stackoverflow.com/a
        // Posted by BackSlash, modified by community. See post 'Timeline' for change history
        // Retrieved 2025-11-25, License - CC BY-SA 3.0

        this.setLocationRelativeTo(null);
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Error connecting to the database","Connection Error",JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    

    //REQUIRES: the email does not already exists in the database
    //EFFECTS: takes in name,password,email and create a new row in the database
    private void createAccount(String name, String pass, String email) {
        String sql = "INSERT INTO inventorydb.accounts (accountName,accountPass,accountEmail) VALUES (?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);){
            
            pstmt.setString(1,name);
            pstmt.setString(2,pass);
            pstmt.setString(3,email);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this,"Account cuccessfully created","Account Created!",JOptionPane.INFORMATION_MESSAGE);
            
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
//    private updateEmail() {
//        String sql = UPDATE accounts SET accountEmail = "janedoe1000@gmail.com" where accountName = "janeDoe";
//    }
    
    //EFFECTS: Check whether the email entered already exists in the database
    private boolean emailExist(String email) {
        String sql = "SELECT * FROM accounts WHERE accountEmail = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);){
            pstmt.setString(1,email);
           
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        } 
    }
    
    //EFFECTS: Returns true if all the fields are filled out, otherwise returns false
    private boolean checkFields(String user, String email, String pass) {
        if (user.equals("Enter Username") && email.equals("Enter Email") && pass.equals("Enter Password")) {
            JOptionPane.showMessageDialog(this, "Please fill out the fields","Account Creation Error",JOptionPane.ERROR_MESSAGE);
            return false;
           
        }
        //only email filled
        else if (user.equals("Enter Username") && pass.equals("Enter Password")) {
            JOptionPane.showMessageDialog(this, "Please enter your username and password","Account Creation Error",JOptionPane.ERROR_MESSAGE);
            return false;
           
        }
        //only user filled
        else if (pass.equals("Enter Password") && email.equals("Enter Email")) {
            JOptionPane.showMessageDialog(this, "Please enter your password and email","Account Creation Error",JOptionPane.ERROR_MESSAGE);
            return false;
           
        }
        //only pass filled
        else if (user.equals("Enter Username") && email.equals("Enter Email")) {
            JOptionPane.showMessageDialog(this, "Please enter your username and email","Account Creation Error",JOptionPane.ERROR_MESSAGE);
            return false;
           
        }
        //if email is not filled
        else if (email.equals("Enter Email")) {
            JOptionPane.showMessageDialog(this, "Please enter your email","Account Creation Error",JOptionPane.ERROR_MESSAGE);
            return false;
           
        }
        //if username is not filled
        else if (user.equals("Enter Username")) {
            JOptionPane.showMessageDialog(this, "Please enter your username","Account Creation Error",JOptionPane.ERROR_MESSAGE);
            return false;
           
        }
        //if password is not filled
        else if (pass.equals("Enter Password")) {
            JOptionPane.showMessageDialog(this, "Please enter your password","Account Creation Error",JOptionPane.ERROR_MESSAGE);
            return false;
           
        }
        return true;
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SignupPanel = new javax.swing.JPanel();
        UsernameText = new javax.swing.JLabel();
        PasswordText = new javax.swing.JLabel();
        UsernameField = new javax.swing.JTextField();
        SignupButton = new javax.swing.JButton();
        EmailField = new javax.swing.JTextField();
        EmailText = new javax.swing.JLabel();
        PasswordField = new javax.swing.JPasswordField();
        LoginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        UsernameText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UsernameText.setText("Username");

        PasswordText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PasswordText.setText("Password");

        UsernameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        UsernameField.setText("Enter Username");
        UsernameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                UsernameFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                UsernameFieldFocusLost(evt);
            }
        });

        SignupButton.setText("Signup");
        SignupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignupButtonActionPerformed(evt);
            }
        });

        EmailField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        EmailField.setText("Enter Email");
        EmailField.setToolTipText("");
        EmailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                EmailFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                EmailFieldFocusLost(evt);
            }
        });

        EmailText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EmailText.setText("Email");

        PasswordField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        PasswordField.setText("Enter Password");
        PasswordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PasswordFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PasswordFieldFocusLost(evt);
            }
        });

        LoginButton.setText("Login");
        LoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SignupPanelLayout = new javax.swing.GroupLayout(SignupPanel);
        SignupPanel.setLayout(SignupPanelLayout);
        SignupPanelLayout.setHorizontalGroup(
            SignupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SignupPanelLayout.createSequentialGroup()
                .addGroup(SignupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UsernameText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PasswordText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EmailText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(SignupPanelLayout.createSequentialGroup()
                        .addGroup(SignupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SignupPanelLayout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(UsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(SignupPanelLayout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(EmailField, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 49, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SignupPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(SignupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SignupPanelLayout.createSequentialGroup()
                        .addComponent(PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SignupPanelLayout.createSequentialGroup()
                        .addComponent(LoginButton)
                        .addGap(39, 39, 39)
                        .addComponent(SignupButton)
                        .addGap(73, 73, 73))))
        );
        SignupPanelLayout.setVerticalGroup(
            SignupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SignupPanelLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(UsernameText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PasswordText)
                .addGap(12, 12, 12)
                .addComponent(PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EmailText)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EmailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(SignupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SignupButton)
                    .addComponent(LoginButton))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(SignupPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SignupPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SignupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignupButtonActionPerformed
        // TODO add your handling code here:
        String user = UsernameField.getText();
        String email = EmailField.getText();
        String pass = new String(PasswordField.getPassword());
        
        if (checkFields(user,email,pass)) {
            //check if email already exist in the database, if yes then throw an error saying "Account already exists"
            if (emailExist(email)) {
            JOptionPane.showMessageDialog(this, "This email already exists","Account Creation Error",JOptionPane.ERROR_MESSAGE);
            } 
            else {
                createAccount(user,pass,email);
                this.setVisible(false);
              
                new MainWindow().setVisible(true);
                
            }
        }
        
        
        
    }//GEN-LAST:event_SignupButtonActionPerformed

    private void UsernameFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UsernameFieldFocusGained
        // TODO add your handling code here:
        if (UsernameField.getText().equals("Enter Username")) {
            UsernameField.setText("");
        }
    }//GEN-LAST:event_UsernameFieldFocusGained

    private void PasswordFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PasswordFieldFocusGained
        // TODO add your handling code here:
        String x = new String(PasswordField.getPassword());

        if (x.equals("Enter Password")) {
            PasswordField.setText("");//when the user click onto the password field, it will clear
        }
    }//GEN-LAST:event_PasswordFieldFocusGained

    private void UsernameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UsernameFieldFocusLost
        // TODO add your handling code here:
        if (UsernameField.getText().length() == 0) {
            UsernameField.setText("Enter Username");
        }
    }//GEN-LAST:event_UsernameFieldFocusLost

    private void PasswordFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PasswordFieldFocusLost
        // TODO add your handling code here:
        if (PasswordField.getPassword().length == 0) {
            PasswordField.setText("Enter Password");
        }
    }//GEN-LAST:event_PasswordFieldFocusLost

    private void EmailFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_EmailFieldFocusGained
        // TODO add your handling code here:
        if (EmailField.getText().equals("Enter Email")) {
            EmailField.setText("");
        }
    }//GEN-LAST:event_EmailFieldFocusGained

    private void EmailFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_EmailFieldFocusLost
        // TODO add your handling code here:
        if (EmailField.getText().length() == 0) {
            EmailField.setText("Enter Email");
        }
    }//GEN-LAST:event_EmailFieldFocusLost

    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButtonActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        
        new LoginPage().setVisible(true);
    }//GEN-LAST:event_LoginButtonActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
//            logger.log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> new SignupPage().setVisible(true));
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField EmailField;
    private javax.swing.JLabel EmailText;
    private javax.swing.JButton LoginButton;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JLabel PasswordText;
    private javax.swing.JButton SignupButton;
    private javax.swing.JPanel SignupPanel;
    private javax.swing.JTextField UsernameField;
    private javax.swing.JLabel UsernameText;
    // End of variables declaration//GEN-END:variables
}
