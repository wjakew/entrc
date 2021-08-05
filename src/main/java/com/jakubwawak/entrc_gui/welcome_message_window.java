/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc_gui;

import com.jakubwawak.database.Database_Connector;
import com.jakubwawak.database.Database_ProgramCodes;
import com.jakubwawak.entrc.Configuration;
import com.jakubwawak.entrc.RuntimeChecker;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Window for creating welcome message for window
 * @author jakubwawak
 */
public class welcome_message_window extends javax.swing.JDialog {

    /**
     * Creates new form welcome_message
     */
    Database_Connector database;
    Configuration config;
    String version;
    String content;
    int database_version;
    int debug_window;
    public welcome_message_window(java.awt.Frame parent, boolean modal,Database_Connector database,String version,String database_version,int debug_window) 
            throws UnknownHostException, SocketException, IOException, FileNotFoundException, URISyntaxException {
        super(parent,modal);
        this.database = database;
        this.database_version = Integer.parseInt(database_version);
        this.debug_window = debug_window;
        config = new Configuration("config.entrconf");
        this.version = version;
        content = "";
        initComponents();
        load_window();
        this.setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
    }
    
    /**
     * Function for loading window
     */
    void load_window() throws UnknownHostException, SocketException{
        field_text.setEditable(false);
        String banner = " _____ _   _ _____ ____   ____ \n" +
                        "| ____| \\ | |_   _|  _ \\ / ___|\n" +
                        "|  _| |  \\| | | | | |_) | |    \n" +
                        "| |___| |\\  | | | |  _ <| |___ \n" +
                        "|_____|_| \\_| |_| |_| \\_\\\\____|";
        String addons = " VERSION: "+version + "   Jakub Wawak\n\n";
        addons = addons +"BUILD INFORMATION:\n";
        addons = addons +"build date: 16.06.2021\n";
        addons = addons +"machine local IP:"+get_IP_data()+"\n";
        button_changedata.setVisible(false);
        update_data(banner);
        update_data(addons);
    }
    
    /**
     * Function for updating data on component
     */
    void update_data(String data){
        content = field_text.getText();
        content = content + "\n";
        content = content + data;
        field_text.setText(content);
    }
    
    
    
    /**
     * Function for getting machine IP data
     * @return
     * @throws UnknownHostException
     * @throws SocketException 
     */
    String get_IP_data() throws UnknownHostException, SocketException{
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
          }
    }
    
    /**
     * Function for updating data
     * @throws SQLException
     * @throws UnknownHostException
     * @throws SocketException
     * @throws FileNotFoundException 
     */
    void load_connection() throws SQLException, UnknownHostException, SocketException, FileNotFoundException{
        update_data("Loading connection...");
        if ( config.prepared ){
            database.connect(config.ip, config.database, config.databaseuser, config.databasepass);
            update_data("Trying connect to "+config.ip+"/"+config.database+"as "+config.databaseuser+"...");
            if ( database.connected ){
                RuntimeChecker rtc = new RuntimeChecker();
                rtc.after_check(database);
                if ( database.evaluation_copy ){
                        update_data("Ta kopia programu jest wersją testową.");
                    }
                if (!rtc.validate_flag ){
                        if ( !rtc.license_load ){
                            update_data("Błąd podczas odczytu licencji");
                        }
                        update_data("Błędne sprawdzanie licencji programu. \nSkontaktuj się z administratorem. \nMAC: "+database.get_local_MACAddress()
                        +"\n licencja powinna być zapisana w pliku entrclient.license");
                    }
                else{
                    Database_ProgramCodes dpc = new Database_ProgramCodes(database);
                    
                    switch( dpc.check_database_version(database_version)){
                        case 0:
                            database.log("Database version not match");
                            update_data("Bład wersji bazy danych\n Wersja na maszynie jest zła."
                                + "\n Prośba o kontakt z administratorem\n"
                                + "Wymagana wersja: "+database_version);
                            System.exit(0);
                            break;
                        case 2:
                            database.log("Database version is correct");
                            try{   
                        
                        String code = dpc.get_value("CLIENTSTARTUP");
                        
                        switch(code){
                                case "NORMAL":
                                    database.log("NORMAL VIEW MODE INVOKED");
                                    new main_user_window(database,version,debug_window);
                                    break;
                                case "ANC_ADD":
                                    database.log("ANNOUNCEMENT VIEW MODE INVOKED");
                                    new main_user_ANC_window(database,version,debug_window);
                                    break;
                                default:
                                    update_data("Błąd na bazie: 000");
                                    new main_user_window(database,version,debug_window);
                                    break;
                            }

                        }catch(Exception e){
                            update_data("Save mode activated...");
                            new main_user_window(database,version,debug_window);
                        }
                            break;
                        case 1:
                            update_data("Wersja bazy jest nowsza niż aplikacji, sprawdź czy nie ma aktualizacji");
                            break;
                        case -1:
                            update_data("Błąd sprawdzania wersji bazy danych");
                            break;
                    }
                }
            }
            else{
                update_data("Brak połączenia z bazą");
                button_changedata.setEnabled(true);
            }
        }else{
            
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

        jScrollPane1 = new javax.swing.JScrollPane();
        field_text = new javax.swing.JTextArea();
        button_start = new javax.swing.JButton();
        button_changedata = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Entrc Client Runtime");

        field_text.setColumns(20);
        field_text.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        field_text.setRows(5);
        jScrollPane1.setViewportView(field_text);

        button_start.setText("Uruchom aplikacje");
        button_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_startActionPerformed(evt);
            }
        });

        button_changedata.setText("Zmień dane połączenia");

        jLabel1.setText("version");

        jLabel2.setText("build_number");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(button_changedata, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_start, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_start)
                    .addComponent(button_changedata))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_startActionPerformed
        try {
            load_connection();
        } catch (SQLException ex) {
            new message_window_jdialog(this,true,"Błąd\n"+ex.toString());
            dispose();
        } catch (UnknownHostException ex) {
            new message_window_jdialog(this,true,"Błąd\n"+ex.toString());
            dispose();
        } catch (SocketException ex) {
            new message_window_jdialog(this,true,"Błąd\n"+ex.toString());
            dispose();
        } catch (FileNotFoundException ex) {
            new message_window_jdialog(this,true,"Błąd\n"+ex.toString());
            dispose();
        }
    }//GEN-LAST:event_button_startActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_changedata;
    private javax.swing.JButton button_start;
    private javax.swing.JTextArea field_text;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
