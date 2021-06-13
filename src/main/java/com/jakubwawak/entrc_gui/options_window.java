/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc_gui;

import com.jakubwawak.database.Database_Connector;
import com.jakubwawak.database.Database_Data_Dump;
import com.jakubwawak.entrc.Mail_Sender2;
import java.util.ArrayList;
import java.util.Date;

/**
 *Object for creeating options window
 * @author kubaw
 */
public class options_window extends javax.swing.JDialog {

    /**
     * Creates new form options_window
     */
    Database_Connector database;
    
    public options_window(javax.swing.JDialog parent, boolean modal,Database_Connector database,String version) {
        super(parent, modal);
        this.database = database;
        initComponents();
        label_version.setText(version);
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        send_log_button = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        label_version = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Opcje programu");

        send_log_button.setText("Wyślij logi do administratora");
        send_log_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                send_log_buttonActionPerformed(evt);
            }
        });

        jLabel1.setText("by Jakub Wawak");

        jLabel2.setText("kubawawak@gmail.com");

        label_version.setText("v1.0.3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(send_log_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(label_version)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 128, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(120, 120, 120))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(137, 137, 137))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(send_log_button)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label_version)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void send_log_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_send_log_buttonActionPerformed
        Database_Data_Dump ddd = new Database_Data_Dump(database);
        try {
            Date actual_date = new Date();
            ArrayList<String> logs = ddd.dump_program_log();
            
            String file_src = ddd.dump_to_file(logs, "program_log.txt");
            
            Mail_Sender2 mail_connector = new Mail_Sender2("kubawawak@gmail.com","main.tes.instruments@gmail.com");
            
            mail_connector.set_title("Program log (entrc) from: "+actual_date.toString());
            
            mail_connector.set_message("W załączniku log programu Entrc");
            
            mail_connector.set_attachment(file_src);
            
            mail_connector.send_message();
            new message_window_jdialog(this,true,"Wysłano do administratorów");
            send_log_button.setText("Wysłano");
        }catch (Exception ex) {
            new message_window_jdialog(this,true,"Błąd: "+ex.toString());
            send_log_button.setText("Błąd");
            send_log_button.setEnabled(false);
        }
        
    }//GEN-LAST:event_send_log_buttonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel label_version;
    private javax.swing.JButton send_log_button;
    // End of variables declaration//GEN-END:variables
}
