/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 *Main user GUI object
 * @author jakubwawak
 */
public class main_user_window extends javax.swing.JFrame {

    /**
     * Creates new form main_user_window
     */
    Database_Connector database;
    
    public main_user_window(Database_Connector database) {
        initComponents();
        this.database = database;
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
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

        textfield_pin = new javax.swing.JTextField();
        button_1 = new javax.swing.JButton();
        button_submit = new javax.swing.JButton();
        button_2 = new javax.swing.JButton();
        button_3 = new javax.swing.JButton();
        button_5 = new javax.swing.JButton();
        button_4 = new javax.swing.JButton();
        button_6 = new javax.swing.JButton();
        button_8 = new javax.swing.JButton();
        button_7 = new javax.swing.JButton();
        button_9 = new javax.swing.JButton();
        button_0 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ENTRC USER");
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        textfield_pin.setFont(new java.awt.Font("Dialog", 0, 48)); // NOI18N
        textfield_pin.setText("WPISZ PIN");
        textfield_pin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textfield_pinFocusGained(evt);
            }
        });

        button_1.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_1.setText("1");
        button_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_1ActionPerformed(evt);
            }
        });

        button_submit.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_submit.setText("Zatwierdz");
        button_submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_submitActionPerformed(evt);
            }
        });

        button_2.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_2.setText("2");
        button_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_2ActionPerformed(evt);
            }
        });

        button_3.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_3.setText("3");
        button_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_3ActionPerformed(evt);
            }
        });

        button_5.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_5.setText("5");
        button_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_5ActionPerformed(evt);
            }
        });

        button_4.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_4.setText("4");
        button_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_4ActionPerformed(evt);
            }
        });

        button_6.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_6.setText("6");
        button_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_6ActionPerformed(evt);
            }
        });

        button_8.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_8.setText("8");
        button_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_8ActionPerformed(evt);
            }
        });

        button_7.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_7.setText("7");
        button_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_7ActionPerformed(evt);
            }
        });

        button_9.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_9.setText("9");
        button_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_9ActionPerformed(evt);
            }
        });

        button_0.setFont(new java.awt.Font("Dialog", 0, 36)); // NOI18N
        button_0.setText("0");
        button_0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_0ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(button_submit, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(button_0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(textfield_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button_1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(button_4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button_7, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(button_8, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(button_9, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(button_2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(button_3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(button_5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(button_6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textfield_pin, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_3, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_5, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_4, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_6, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_8, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_7, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_9, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_0, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(button_submit, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_1ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"1");
    }//GEN-LAST:event_button_1ActionPerformed

    private void button_2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_2ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"2");
    }//GEN-LAST:event_button_2ActionPerformed

    private void button_3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_3ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"3");
    }//GEN-LAST:event_button_3ActionPerformed

    private void button_4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_4ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"4");
    }//GEN-LAST:event_button_4ActionPerformed

    private void button_5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_5ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"5");
    }//GEN-LAST:event_button_5ActionPerformed

    private void button_6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_6ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"6");
    }//GEN-LAST:event_button_6ActionPerformed

    private void button_7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_7ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"7");
    }//GEN-LAST:event_button_7ActionPerformed

    private void button_8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_8ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"8");
    }//GEN-LAST:event_button_8ActionPerformed

    private void button_9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_9ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"9");
    }//GEN-LAST:event_button_9ActionPerformed

    private void button_submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_submitActionPerformed
        String pin = textfield_pin.getText();
        
        if ( pin.isBlank() || pin.isEmpty() ){
            // textfield_pin is blank
            button_submit.setText("Pusto");
        }
        else{
            try{
                // textfield_pin is not blank
                if ( database.check_exitcode(pin) == 1){
                    // program has to exit
                    new message_window(this,true,"Program zostanie zamkniety");
                    System.exit(0);
                }
                else if ( database.check_resetcode(pin) == 1){
                    // pin reset invoked
                    new reset_pin_window(this,true,database);
                }
                else{
                    // here we know that user pin is entered
                    Guard user = new Guard(textfield_pin.getText(),database);
                    user.check_credentials();       // checking credentials
                    
                    if ( user.accepted == 1 ){
                        //user accepted
                        database.log_LOGIN_ACCEPT(pin);         // LOGIN_ACCEPT EVENT
                        new user_accept_window(this,true,database,user);
                        textfield_pin.setText("");
                    }
                    else{
                        //user set wrong pin
                        button_submit.setText("BŁĘDNY PIN");
                        database.log_LOGIN_FAILED(pin);         // LOGIN_FAILED EVENT
                        textfield_pin.setText("");
                    }
                }                
                
            }catch(SQLException e){
                new message_window(this,true,"Błąd akceptacji pin");
            }
        }
    }//GEN-LAST:event_button_submitActionPerformed

    private void textfield_pinFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_textfield_pinFocusGained
        textfield_pin.setText("");
        button_submit.setText("Zatwierdz");
    }//GEN-LAST:event_textfield_pinFocusGained

    private void button_0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_0ActionPerformed
        button_submit.setText("Zatwierdz");
        textfield_pin.setText(textfield_pin.getText()+"0");
    }//GEN-LAST:event_button_0ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_0;
    private javax.swing.JButton button_1;
    private javax.swing.JButton button_2;
    private javax.swing.JButton button_3;
    private javax.swing.JButton button_4;
    private javax.swing.JButton button_5;
    private javax.swing.JButton button_6;
    private javax.swing.JButton button_7;
    private javax.swing.JButton button_8;
    private javax.swing.JButton button_9;
    private javax.swing.JButton button_submit;
    private javax.swing.JTextField textfield_pin;
    // End of variables declaration//GEN-END:variables
}
