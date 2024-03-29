/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc_gui;

import com.jakubwawak.database.Database_Connector;
import com.jakubwawak.entrc.Configuration;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Object for adding database connection
 * @author jakubwawak
 */
public class database_connection_window extends javax.swing.JDialog {

    /**
     * Creates new form database_connection_window
     */
    Database_Connector database;
    String build_number,version;
    public database_connection_window(java.awt.Frame parent, boolean modal,Database_Connector database,String build_number,String version) {
        super(parent, modal);
        this.build_number = build_number;
        this.version = version;
        this.database = database;
        initComponents();
        this.setLocationRelativeTo(null);
        load_window();
        setVisible(true);
    }
    
    /**
     * Function for loading window components
     */
    void load_window(){
        if ( database.config != null){
            field_database.setText(database.config.database);
            field_ip.setText(database.config.ip);
            field_user.setText(database.config.databaseuser);
            field_password.setText(database.config.databasepass);
        }
        label_version.setText(version);
        label_build.setText(build_number);
    }
    
    /**
     * Function for vield validation
     * @return boolean
     */
    boolean validate_vields(){
        if ( !field_database.getText().equals("") && !field_ip.getText().equals("") && !field_user.getText().equals("") ){
            return true;
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        field_ip = new javax.swing.JTextField();
        field_database = new javax.swing.JTextField();
        field_user = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        button_connect = new javax.swing.JButton();
        label_version = new javax.swing.JLabel();
        field_password = new javax.swing.JPasswordField();
        label_build = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Brak pliku. Podaj dane bazy");

        jLabel1.setText("ip:");

        jLabel2.setText("database name:");

        jLabel3.setText("database user:");

        jLabel4.setText("database password:");

        button_connect.setText("Podłącz...");
        button_connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_connectActionPerformed(evt);
            }
        });

        label_version.setText("version");

        label_build.setText("Database version:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                                .addComponent(field_ip, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(field_database)
                                    .addComponent(field_user)
                                    .addComponent(field_password, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))))
                        .addContainerGap(28, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(button_connect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label_version, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label_build, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label_version)
                    .addComponent(label_build))
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(field_ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(field_database, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(field_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(field_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button_connect)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_connectActionPerformed
        if ( validate_vields() ){
            try{
                database.connect(field_ip.getText(), field_database.getText(), field_user.getText(), field_password.getText());
                if ( database.connected ){
                    new message_window_jdialog(this,true,"Połączono prawidłowo");
                    database.config = new Configuration();
                    database.config.database = field_database.getText();
                    database.config.databasepass = field_password.getText();
                    database.config.databaseuser = field_user.getText();
                    database.config.ip = field_ip.getText();
                    database.config.copy_configuration();
                    dispose();
                }
                else{
                    new message_window_jdialog(this,true,"Błąd, nieprawidłowe dane bazy danych");
                }
            } catch (SQLException ex) {
                new message_window_jdialog(this,true,"Błąd\n"+ex.toString());
            } catch (IOException ex) {
                new message_window_jdialog(this,true,"Błąd zapisywania pliku konfiguracyjnego\n"+ex.toString());
            }
        }else{
            new message_window_jdialog(this,true,"Błednie wypełniony formularz");
        }
                
    }//GEN-LAST:event_button_connectActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_connect;
    private javax.swing.JTextField field_database;
    private javax.swing.JTextField field_ip;
    private javax.swing.JPasswordField field_password;
    private javax.swing.JTextField field_user;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel label_build;
    private javax.swing.JLabel label_version;
    // End of variables declaration//GEN-END:variables
}
