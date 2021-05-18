/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.database;

import com.jakubwawak.entrc.BarCodeCreator;
import com.jakubwawak.entrc.Configuration;
import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ThreadLocalRandom;
/**
 * Database object connector
 * @author jakubwawak
 */
public class Database_Connector {
    
    // version of database 
    final String version = "vC.0.6";
    // header for logging data
    // connection object for maintaing connection to the database
    Connection con;
    
    public Configuration config;
    // variable for debug purposes
    final int debug = 1;
    
    public boolean evaluation_copy;
    
    public boolean connected;                      // flag for checking connection to the database
    String ip;                              // ip data for the connector
    String database_name;                   // name of the database
    String database_user,database_password; // user data for cred
    ArrayList<String> database_log;         // collection for storing data
    
    /**
     * Constructor
     */
    public Database_Connector() throws SQLException, ClassNotFoundException, SQLException{
        con = null;
        database_log = new ArrayList<>();
        connected = false;
        ip = "";
        database_name = "";
        database_user = "";
        database_password = "";
        config = null;
        //log("Started! Database Connector initzialazed");
    }
    
    /**
     * Function for gathering database log
     * @param log 
     */
    public void log(String log) throws SQLException{
        java.util.Date actual_date = new java.util.Date();
        database_log.add("("+actual_date.toString()+")"+" - "+log);
        System.out.println("ENTRC LOG: "+database_log.get(database_log.size()-1));
        if ( con == null){
            System.out.println("Bład bazy: con=null ("+log+")");
        }
        else{
            // load log to database
            if ( debug == 1){
                String query = "INSERT INTO PROGRAM_LOG (program_log_desc) VALUES (?); ";

                PreparedStatement ppst = con.prepareStatement(query);

                try{

                    ppst.setString(1,log);

                    ppst.execute();

                }catch(SQLException e){}
            }
            // after 100 records dump to file
            if(database_log.size() > 100){
                database_log.clear();
            }
        }
    }
    
    /**
     * Function for printing info data on the screen
     * @param data 
     */
    public void info_print(String data){
        if ( debug == 1 ){
            System.out.println(data);
        }
    }
    /**
     * Function for connecting to the database
     * @param ip
     * @param database_name
     * @param user
     * @param password
     * @throws SQLException 
     */
    public void connect(String ip,String database_name,String user,String password) throws SQLException{
        this.ip = ip;
        this.database_name = database_name;
        database_user = user;
        database_password = password;
        
        String login_data = "jdbc:mysql://"+this.ip+"/"+database_name+"?"
                + "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&" +
                                   "user="+database_user+"&password="+database_password;
        try{
            con = DriverManager.getConnection(login_data);
            connected = true;
            log("Connected succesfully");
        }catch(SQLException e){
            connected = false;
            System.out.println("Failed to connect to database ("+e.toString()+")");
        }
        log("Database string: "+login_data);
    }
    
    /**
     * Function for getting MAC addreses of the local computer
     * @return
     * @throws UnknownHostException
     * @throws SocketException 
     */
    public String get_local_MACAddress() throws UnknownHostException, SocketException{
        if( System.getProperty("os.name").equals("Mac OS X")){
            return "macos - not supported";
        }
        else{
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
            byte[] hardwareAddress = ni.getHardwareAddress();
            String[] hexadecimal = new String[hardwareAddress.length];
            for (int i = 0; i < hardwareAddress.length; i++) {
                hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
            }
            return String.join("-", hexadecimal);
        }
    }
    
    /**
     * Function for comparing program license with database
     * @param license
     * @return Integer
     * @throws UnknownHostException
     * @throws SocketException 
     * return codes:
     *  1 - license found
     *  0 - license not found
     * -1 - database error
     */
    public int compare_licenses(String license) throws UnknownHostException, SocketException, SQLException{
        String query = "SELECT * FROM RUNTIME WHERE runtime_macaddress = ? and "
                + "runtime_license = ?;";
        
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            
            ppst.setString(1,get_local_MACAddress());
            ppst.setString(2,license);
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                if ( rs.getString("runtime_license").equals("LICENSE")){
                    evaluation_copy = true;
                }
                return 1;
            }
            return 0;
            
        }catch(SQLException e){
            log("Failed to check licenses ("+e.toString()+")");
            return -1;
        }
    }
    
    /**
     * Function for checking lincese exixts
     * @return int
     */
    public int check_license_exists() throws SQLException, UnknownHostException, SocketException{
        String query = "SELECT * FROM RUNTIME WHERE runtime_macaddress = ?;";
        
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            
            ppst.setString(1,get_local_MACAddress());
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return 1;
            }
            return 0;
            
        }catch(SQLException e){
            log("Failed to check license ("+e.toString()+")");
            return -1;
        }
    }
    
    /**
     * Function for inserting licenses
     * @param license
     * @return Integer
     * @throws SQLException
     * @throws UnknownHostException
     * @throws SocketException 
     * return codes:
     * 1 - license inserted
     * -1 - database error
     */
    public int insert_license(String license) throws SQLException, UnknownHostException, SocketException{
        String query = "INSERT INTO RUNTIME (runtime_license,runtime_macaddress)\n" +
                        "VALUES\n" +
                        "(?,?);";
        
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.setString(1,license);
            ppst.setString(2,get_local_MACAddress());
            
            ppst.execute();
            return 1;
        }catch(SQLException e){
            log("Failed to insert license to database! ("+license+") ("+e.toString()+")");
            return -1;
        }
    }
    /**
     * Function for getting PIN data for managing client window
     * @return ArrayList
     * @throws SQLException 
     */
    public ArrayList<String> get_admin_PIN_data() throws SQLException{
        ArrayList<String> data_to_get = new ArrayList<>();
        
        String query = "SELECT * from CONFIGURATION;";
        
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                data_to_get.add(rs.getString("entrc_user_exit_pin"));
                data_to_get.add(rs.getString("entrc_user_ask_pin"));
                data_to_get.add(rs.getString("entrc_user_manage_pin"));
                data_to_get.add(rs.getString("entrc_admin_manage_pin"));
                return data_to_get;
            }
            return null;
            
        }catch(SQLException e){
            log("Failed to get admin PIN data ("+e.toString());
            return null;
        }
    }
    
    /**
     * Function for loading admin PIN data for managing client window
     * @param data
     * @return boolean
     */
    public boolean load_admin_PIN_data(ArrayList<String> data) throws SQLException{
        String query = "INSERT INTO CONFIGURATION\n" +
                        "(entrc_user_exit_pin,entrc_user_ask_pin,entrc_user_manage_pin,entrc_admin_manage_pin)\n" +
                        "VALUES\n" +
                        "(?,?,?,?);";
        
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            
            ppst.setString(1,data.get(0));
            ppst.setString(2,data.get(1));
            ppst.setString(3,data.get(2));
            ppst.setString(4,data.get(3));
            
            ppst.execute();
            return true;
        
        }catch(SQLException e){
            log("Failed to update PIN on database ("+e.toString()+")");
            return false;
        }
    }
        
    
    
    /**
     * Function to generate user login
     * @return String
     */
    String login_generator(String name,String surname) throws SQLException{
        int numbers[] = {1,2,3,4,5,6,7,8,9,10,11};
        String login = "";
        if ( surname.length() >=5 ){
            login = surname.substring(0, 5);
            login = login + name.charAt(0);
        }
        else{
            int size = surname.length();
            login = surname;
            login = login + name.substring(0,5-size);
        }
        
        String query = "SELECT worker_login FROM WORKER;";
        
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            
            ResultSet rs = ppst.executeQuery();
            int i = 0;
            while(rs.next()){
                if ( rs.getString("worker_login").equals(login)){
                    login = login + Integer.toString(numbers[i]);
                }
            }
            return login;
        }catch(SQLException e){
            log("Failed to generate login ("+e.toString()+")");
            return null;
        }
    }
    
    /**
     * Function for logging ERRORS to the database
     * @param error
     * @param error_code
     * @return boolean
     * @throws SQLException 
     */
    public boolean log_ERROR(String error,String error_code) throws SQLException{
        String query = "INSERT INTO ERROR_LOG \n"
                + " (error_log_code,error_log_desc)\n"
                + " VALUES"
                + " (?,?);";
        
        try{
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.setString(1, error_code);
            ppst.setString(2,error);
            
            
            ppst.execute();
            return true;
        
        }catch(SQLException e){
            System.out.println("Failed to log error ("+e.toString()+")");
            return false;
        }
    }
    
    /**
     * Function for getting worker id by pin
     * @param pin
     * @return Integer
     * @throws SQLException 
     * return codes:
     * any - worker id
     * 0 - no worker with given id
     * -1 - database connector failed
     */
    public int get_worker_id_bypin(String pin) throws SQLException{
        String query = "SELECT worker_id FROM WORKER WHERE worker_pin=?";
        
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1,pin);
        try{
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return rs.getInt("worker_id");
            }
            return 0;
        }catch(SQLException e){
            log("Failed to get worker_id ("+e.toString());
            return -1;
        }
    }
    
    /**
     * Function for getting worker pin by given id
     * @param worker_id
     * @return String
     * @throws SQLException 
     */
    private String get_worker_PIN_byid(int worker_id) throws SQLException{
        String query = "SELECT worker_pin FROM WORKER WHERE worker_id = ?;";
        
        try{
            log("Invoked private function for getting worker PIN ");
            PreparedStatement ppst = con.prepareStatement(query);
            
            ppst.setInt(1,worker_id);
            
            ResultSet rs = ppst.executeQuery();
            
            if(rs.next()){
                return rs.getString("worker_pin");
            }
            return "blank";
        }catch(SQLException e){
            log("Failed to get worker pin by given id ("+worker_id+") ("+e.toString()+")");
            return null;
        }
    }
    
    /**
     * Function for getting worker name and surname
     * @param pin
     * @return String
     * @throws SQLException 
     * Returns null if worker don't exist
     * NOTE: less than probable, func used only after get_worker_id_bypin(String pin)
     */
    public String get_worker_data(String pin) throws SQLException{
        String query = "SELECT worker_name,worker_surname FROM WORKER where worker_pin=?;";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1,pin);
        
        try{
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return rs.getString("worker_name") + " " + rs.getString("worker_surname");
            }
            return null;
        
        }catch(SQLException e){
            log("Failed to get worker data ("+e.toString());
            return null;
        }   
    }
    
    /**
     * Function for getting worker data by id
     * @param id
     * @return String
     * @throws SQLException 
     */
    public String get_worker_data(int id) throws SQLException{
        String query = "SELECT worker_name,worker_surname FROM WORKER where worker_id=?;";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1,id);
        
        try{
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return rs.getString("worker_name") + " " + rs.getString("worker_surname");
            }
            return null;
        
        }catch(SQLException e){
            log("Failed to get worker data ("+e.toString());
            return null;
        }   
    }
    /**
     * Function for logging LOGIN_FAILED action
     * @param pin
     * @return Integer
     * @throws SQLException 
     * return codes:
     * 1 - successfully added log record
     * -1 - error on the database
     */
    public int log_LOGIN_FAILED(String pin) throws SQLException{
        LocalDateTime todayLocalDate = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        String query = "INSERT INTO USER_LOG\n" +
                       "(user_log_date,worker_id,user_log_action,user_log_desc,user_log_photo_src)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?);";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        try{
            ppst.setObject(1, todayLocalDate);
            ppst.setInt(2,1);
            ppst.setString(3,"LOGIN_FAILED");
            ppst.setString(4,"Failed to log user.Pin used (pin:"+pin+")");
            ppst.setString(5,"-");
            
            log("Trying to add:"+ppst.toString());
            ppst.execute();
            return 1;
        
        }catch(SQLException e){
            log("Failed to log LOGIN_FAILED ("+e.toString());
            return -1;
        }
    }
    
    /**
     * Function for logging event PIN_FORGOT
     * @param worker_id
     * @return
     * @throws SQLException 
     */
    public int log_PIN_FORGOT(int worker_id) throws SQLException{
        LocalDateTime todayLocalDate = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        String query = "INSERT INTO USER_LOG\n" +
                       "(user_log_date,worker_id,user_log_action,user_log_desc,user_log_photo_src)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?);";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        try{
            ppst.setObject(1, todayLocalDate);
            ppst.setInt(2,worker_id);
            ppst.setString(3, "PIN_FORGOT");
            ppst.setString(4, "User (id:"+worker_id+") reset pin");
            ppst.setString(5, "-");

            log("Trying to add: "+ppst.toString());
            ppst.execute();
            return 1;

        }catch(SQLException e){
            return -1;
        }
    }
    
    /**
     * Function for logging LOG_ACCEPT action
     * @param pin
     * @return Integer
     * @throws SQLException 
     * return codes:
     * 0 - can't find user with that pin
     * 1 - user accepted and log
     */
    public int log_LOGIN_ACCEPT(String pin) throws SQLException{
        LocalDateTime todayLocalDate = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        String query = "INSERT INTO USER_LOG\n" +
                       "(user_log_date,worker_id,user_log_action,user_log_desc,user_log_photo_src)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?);";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        if ( get_worker_id_bypin(pin) > 0 ){
            // found user
            try{
                ppst.setObject(1, todayLocalDate);
                ppst.setInt(2,get_worker_id_bypin(pin));
                ppst.setString(3, "LOGIN_ACCEPT");
                ppst.setString(4, "User (id:"+get_worker_id_bypin(pin)+") succesfuly logged to the app");
                ppst.setString(5, "-");
                
                log("Trying to add: "+ppst.toString());
                ppst.execute();
                return 1;
            
            }catch(SQLException e){
                return -1;
            }
        }
        return 0;
    }
    /**
     * Function for logging INFO data
     * @param worker_id
     * @param log
     * @return Integer
     * @throws SQLException
     * return codes:
     * 1 - successfully added log record
     * -1 - error on the database
     */
    public int log_INFO(int worker_id,String log) throws SQLException{
        LocalDateTime todayLocalDate = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        String query = "INSERT INTO USER_LOG\n" +
                       "(user_log_date,worker_id,user_log_action,user_log_desc,user_log_photo_src)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?);";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        try{
            ppst.setObject(1, todayLocalDate);
            ppst.setInt(2,worker_id);
            ppst.setString(3,"INFO");
            ppst.setString(4,log);
            ppst.setString(5,"-");
            
            log("Trying to add:"+ppst.toString());
            ppst.execute();
            return 1;
        
        }catch(SQLException e){
            log("Failed to log LOGIN_FAILED ("+e.toString());
            return -1;
        }  
    }
    /**
     * Function for logging ENTER data
     * @param worker_id
     * @param photo_src
     * @return Integer
     * return codes:
     * 1 - successfully added log record
     * -1 - error on the database
     */
    public int log_ENTER(int worker_id,String photo_src) throws SQLException{
        LocalDateTime todayLocalDate = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        String query = "INSERT INTO USER_LOG\n" +
                       "(user_log_date,worker_id,user_log_action,user_log_desc,user_log_photo_src)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?);";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        try{
            ppst.setObject(1, todayLocalDate);
            ppst.setInt(2,worker_id);
            ppst.setString(3,"ENTER");
            ppst.setString(4,"User entered.");
            ppst.setString(5,photo_src);
            
            log("Trying to add:"+ppst.toString());
            ppst.execute();
            return 1;
        
        }catch(SQLException e){
            log("Failed to log LOGIN_FAILED ("+e.toString());
            return -1;
        }  
    }
    
    /**
     * Function for getting last ENTER log id
     * @param worker_id
     * @return Integer
     * @throws SQLException
     * return codes:
     * any - last id of USER_LOG table
     * 0 - ENTER log for given worker_id not found
     * -1 - database error
     */
    public int get_lastid_logENTER(int worker_id) throws SQLException{
        String query = "SELECT * from USER_LOG WHERE worker_id = ? AND user_log_action = 'ENTER' ORDER BY user_log_id DESC LIMIT 1;";
        PreparedStatement ppst = con.prepareStatement(query);
        try{
            ppst.setInt(1, worker_id);
            
            ResultSet rs = ppst.executeQuery();
            
            if( rs.next() ){
                return rs.getInt("user_log_id");
            }
            else{
                return 0;
            }
        }catch(SQLException e){
            log("Failed to get last id of USER_LOG table ("+e.toString());
            return -1;
        }
    }
    /**
     * Function for logging ENTER data
     * @param worker_id
     * @param photo_src
     * @return Integer
     * return codes:
     * 1 - successfully added log record
     * -1 - error on the database
     */
    public int log_EXIT(int worker_id,String photo_src) throws SQLException{
        LocalDateTime todayLocalDate = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        String query = "INSERT INTO USER_LOG\n" +
                       "(user_log_date,worker_id,user_log_action,user_log_desc,user_log_photo_src)\n" +
                       "VALUES\n" +
                       "(?,?,?,?,?);";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        try{
            ppst.setObject(1, todayLocalDate);
            ppst.setInt(2,worker_id);
            ppst.setString(3,"EXIT");
            ppst.setString(4,"User exit.");
            ppst.setString(5,photo_src);
            
            log("Trying to add:"+ppst.toString());
            ppst.execute();
            return 1;
        
        }catch(SQLException e){
            log("Failed to log LOGIN_FAILED ("+e.toString());
            return -1;
        }  
    }
    /**
     * Function for getting last ENTER log id
     * @param worker_id
     * @return Integer
     * @throws SQLException
     * return codes:
     * any - last id of USER_LOG table
     * 0 - ENTER log for given worker_id not found
     * -1 - database error
     */
    public int get_lastid_logEXIT(int worker_id) throws SQLException{
        String query = "SELECT * from USER_LOG WHERE worker_id = ? AND user_log_action = 'EXIT' ORDER BY user_log_id DESC LIMIT 1;";
        PreparedStatement ppst = con.prepareStatement(query);
        try{
            ppst.setInt(1, worker_id);
            
            ResultSet rs = ppst.executeQuery();
            
            if( rs.next() ){
                return rs.getInt("user_log_id");
            }
            else{
                return 0;
            }
        }catch(SQLException e){
            log("Failed to get last id of USER_LOG table ("+e.toString());
            return -1;
        }
    }
    
    /**
     * Function for getting worker pin by given id
     * @param barcode
     * @return String 
     * @throws SQLException 
     */
    public String get_worker_pin_by_barcode(String barcode) throws SQLException{
        String query = "SELECT WORKER_ID FROM BARCODE_DATA WHERE BARCODE_RAW_DATA = ?;";
        
        try{
            log("Tying to find WORKER PIN by given barcode |"+barcode+"|");
            PreparedStatement ppst = con.prepareStatement(query);
            ppst.setString(1,barcode);
            
            ResultSet rs = ppst.executeQuery();
            
            if (rs.next()){
                int worker_id = rs.getInt("worker_id");
                return this.get_worker_PIN_byid(worker_id);
            }
            return "blank";
        }catch(SQLException e){
            log("Failed to get worker pin by barcode: |"+barcode+"|");
            return null;
        }
    }
    
    /**
     * Function for setting entrance to the database
     * @param worker_id
     * @param photo_src
     * @return Boolean
     * @throws SQLException 
     */
    public boolean set_entrance_event(int worker_id,String photo_src) throws SQLException{
        log_ENTER(worker_id,photo_src);
        LocalDateTime entrance_time = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        String query =  "INSERT INTO ENTRANCE\n" +
                        "(worker_id,log_id,entrance_time,entrance_finished)\n" +
                        "VALUE\n" +
                        "(?,?,?,?);";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        try{
            ppst.setInt(1,worker_id);
            ppst.setInt(2,get_lastid_logENTER(worker_id));
            ppst.setObject(3, entrance_time);
            ppst.setInt(4, 0);
            
            log("Query: "+ppst.toString());
            ppst.execute();
            return true;
        }catch(SQLException e){
            log("Failed to set entrance user(id:"+worker_id+") time: "+entrance_time.toString());
            return false;
        }
    }
    
    /**
     * Function for updating entrance event (by giving exit_id)
     * @param worker_id
     * @param exit_id
     * @return Integer
     * @throws SQLException
     * return codes:
     *  1 - successfully updated ENTRANCE
     * -1 - last entrance not found
     * -2 - database error
     */
    int update_entrance_event(int worker_id,int exit_id) throws SQLException{
        String query = "SELECT * from ENTRANCE WHERE worker_id = ? and entrance_finished = 0 ORDER BY entrance_id DESC LIMIT 1;";
        PreparedStatement ppst = con.prepareStatement(query);
        int entrance_id;
        
        try{
            
            ppst.setInt(1,worker_id);
            
            ResultSet rs = ppst.executeQuery();
            
            if(rs.next()){
                // found last user entrance
                entrance_id = rs.getInt("entrance_id");
                
                query = "UPDATE ENTRANCE SET entrance_finished = ? WHERE entrance_id = ? ;";
                ppst = con.prepareStatement(query);
                ppst.setInt(1,exit_id);
                ppst.setInt(2,entrance_id);
                
                ppst.execute();
                return 1;
                
            }
            else{
                log("Can't find uptade entrance event for user(id:"+worker_id+")");
                return -1;
            }

        }catch(SQLException e){
            log("Failed to update entrance event! ("+e.toString()+")");
            return -2;
        }
    }
    
    /**
     * Function for getting id of last entrance of the user
     * @param worker_id
     * @return Integer
     * @throws SQLException 
     * any - returned last entrance_id
     *  0 - last entrance not found
     * -1 - error on the database
     */
    public int get_lastid_ENTRANCE(int worker_id) throws SQLException{
        String query = "SELECT * from ENTRANCE WHERE worker_id = ? ORDER BY user_log_id DESC LIMIT 1;";
        PreparedStatement ppst = con.prepareStatement(query);
        try{
            ppst.setInt(1, worker_id);
            
            ResultSet rs = ppst.executeQuery();
            
            if( rs.next() ){
                return rs.getInt("entrance_id");
            }
            else{
                return 0;
            }
        }catch(SQLException e){
            log("Failed to get last id of ENTRANCE table ("+e.toString());
            return -1;
        }
    }
    
    /**
     * Function for getting id of last entrance of the user
     * @param worker_id
     * @return Integer
     * @throws SQLException 
     * any - returned last entrance_id
     *  0 - last entrance not found
     * -1 - error on the database
     */
    public int get_lastid_EXIT(int worker_id) throws SQLException{
        String query = "SELECT * from ENTRANCE_EXIT WHERE worker_id = ? ORDER BY user_log_id DESC LIMIT 1;";
        PreparedStatement ppst = con.prepareStatement(query);
        try{
            ppst.setInt(1, worker_id);
            
            ResultSet rs = ppst.executeQuery();
            
            if( rs.next() ){
                return rs.getInt("entrance_exit_id");
            }
            else{
                return 0;
            }
        }catch(SQLException e){
            log("Failed to get last id of ENTRANCE_EXIT table ("+e.toString()+")");
            return -1;
        }
    }
    
    /**
     * Function for setting exit event
     * @param worker_id
     * @param photo_src
     * @return Integer
     * @throws SQLException
     * return codes:
     * 1 - succesfully added exit event
     * -1 - database fail
     */
    public int set_exit_event(int worker_id,String photo_src) throws SQLException{
        log_EXIT(worker_id,photo_src);
        LocalDateTime exit_time = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        
        String query = "INSERT INTO ENTRANCE_EXIT\n" +
                "(worker_id,user_log_id,entrance_exit_time)\n" +
                "VALUES\n" +
                "(?,?,?);";
        try{
            PreparedStatement ppst = con.prepareStatement(query);

            ppst.setInt(1,worker_id);
            ppst.setInt(2,get_lastid_logEXIT(worker_id));
            ppst.setObject(3,exit_time);
            
            ppst.execute();

            update_entrance_event(worker_id,get_lastid_EXIT(worker_id));    // making pair with entrance
            return 1;
        
        }catch(SQLException e){
            log("Failed to set exit event for user(id:"+worker_id+") ("+e.toString()+")");
            return -1;
        }
    }
    
    
    /**
     * Function for preparing name for the photo
     * @param worker_id
     * @return Pair
     * Function gathers last event name and name + surname combo
     */
    public Pair<String,String> prepare_photo_name(int worker_id) throws SQLException{
        
        // getting last event name
        String query = "SELECT * FROM USER_LOG where worker_id = ? ORDER BY user_log_id DESC LIMIT 1;";
        PreparedStatement ppst = con.prepareStatement(query);
        
        ppst.setInt(1,worker_id);
        
        String evt_name,name,surname = null;
        
        try{
            ResultSet rs = ppst.executeQuery();
            
            // getting event name to evt_name
            if (rs.next()){
                evt_name = rs.getString("user_log_action") +"-"+rs.getObject("user_log_date").toString().split(" ")[0];
            }
            else{
                evt_name = null;
            }
            
            query = "SELECT worker_name,worker_surname FROM WORKER where worker_id=?;";
            ppst = con.prepareStatement(query);
            
            
            ppst.setInt(1, worker_id);
            
            rs = ppst.executeQuery();
            
            // getting name and surname from WORKER table
            if (rs.next()){
                name = rs.getString("worker_name");
                surname = rs.getString("worker_surname");
            }
            else{
                name = null;
                surname = null;
            }
            
            if ( evt_name != null && name != null && surname != null){
                return new Pair(evt_name, name+"-"+surname);
            }
            return null;
        
        }catch(SQLException e){
            log("Failed to prepare photo name from database! ("+e.toString()+")");
            return null;
        }
    }
    
    /**
     * Function for getting last time ad event category made by user
     * @param worker_id
     * @return Pair
     * @throws SQLException
     * return objects:
     * </date/,/EVENT_CODE/>
     * EVENT_CODES:
     * ENTR     - for entrance events
     * EXIT     - for exit events
     * ENTR_F   - last entrance has no pair
     * EXIT_F   - last exit has no pair
     * NEW      - no last records
     */
    public Pair<LocalDateTime,String> get_last_user_event(int worker_id) throws SQLException{
        LocalDateTime time_entrance,time_exit;
        LocalDateTime time_now = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        
        String query = "SELECT entrance_time FROM ENTRANCE where worker_id = ? ORDER BY entrance_id DESC LIMIT 1;";
        
        PreparedStatement ppst = con.prepareStatement(query);
        info_print("Checking last user event for id:"+worker_id);
        try{
            ppst.setInt(1,worker_id);
            
            ResultSet rs = ppst.executeQuery();
            
            // now rs represents ENTRANCE
            info_print("entrance_time: using query("+ppst.toString()+")");
            if ( rs.next() ){
                // we found last ENTRANCE record 
                time_entrance = rs.getObject("entrance_time", LocalDateTime.class);
                info_print("entrance_time found ("+time_entrance.toString()+")");
            }
            else{
                time_entrance = null;
                info_print("entrace_time is null");
            }
            
            query = "SELECT entrance_exit_time FROM ENTRANCE_EXIT where worker_id = ? ORDER BY entrance_exit_id DESC LIMIT 1;";
            
            ppst = con.prepareStatement(query);
            
            ppst.setInt(1,worker_id);
            info_print("exit_time: using query("+ppst.toString()+")");
            // now rs represents EXIT
            
            rs = ppst.executeQuery();
            
            if ( rs.next() ){
                // we found last EXIT record
                time_exit = rs.getObject("entrance_exit_time",LocalDateTime.class);
                info_print("exit_time found ("+time_exit.toString()+")");
            }
            else{
                time_exit = null;
                info_print("exit_time is null");
                
            }
            
            
            /**
             * scenarios:
             *  ENTRANCE +  EXIT
             *          check what younger
             *  ENTRANCE +   X
             *          check if ENTRANCE - 17 H ( check if ENTRANCE isn't older than 12 hours  if is log event ) 
             *          get ENTR_F if older than 17 hours
             *          get ENTRANCE if not older than 17 hours
             *     X     +  EXIT
             *          get EXIT
             *     X     +   X
             */
            
            
            // ENTRANCE + EXIT
            if ( time_exit != null && time_entrance != null){
                
                // exit after entrance
                if ( time_exit.isAfter(time_entrance) ){
                    return new Pair(time_exit,"EXIT");
                }
                // exit before entrance
                else{
                    return new Pair(time_entrance,"ENTR");
                }
            }
            else{
                // ENTRANCE + X
                if ( time_entrance != null){
                    // -12 h after entrance ENTRANCE OK
                    LocalDateTime check_17 = time_now.minusHours(17);
                    if (check_17.isBefore(time_entrance)){
                        info_print("event set for: ENTR");
                        return new Pair(time_entrance,"ENTR");
                    }
                    // -12 h before entrance ENTRANCE FAIL
                    else{
                        log_INFO(worker_id,"No exit for entrance! Date: "+time_entrance.toString()+
                                " user: "+get_worker_data(worker_id));// log entrance without exit
                        info_print("event set for: ENTR_F");
                        return new Pair(time_entrance,"ENTR_F");
                    }
                }
                // X + EXIT
                else if (time_exit != null){
                    log_INFO(worker_id,"Failed to get ENTRANCE data before");// log no entrance ( DATABASE FAIL )
                    info_print("event set for EXIT_F");
                    return new Pair(time_exit,"EXIT_F");
                }
                // X + X
                else{
                    // log first user login
                    info_print("event set for NEW");
                    return new Pair(null,"NEW");
                }
            }
        }catch(SQLException e){
            log("Failed to gather last user (id:"+worker_id+") event ("+e.toString()+")");
            return null;
        }
        
    }
    /**
     * Function for checking user app exit code
     * @param code
     * @return int
     * @throws SQLException 
     * return codes:
     * 1 - pin accepted on the database - pin correct
     * 0 - pin is not the same - pin rejected
     * -1 - failed to check on the database, check log
     */
    public int check_exitcode(String code) throws SQLException{
        String query = "SELECT * FROM CONFIGURATION where entrc_user_exit_pin = ?";
        
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1,code);
        
        try{
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return 1;
            }
            return 0;
            
        }catch(SQLException e){
            log("Failed to check CONFIGURATION table ("+e.toString());
            return -1;
        }
    }
    /**
     * Function for checking user app reset code
     * @param code
     * @return int
     * @throws SQLException 
     * return codes:
     * 1 - pin accepted on the database - pin correct
     * 0 - pin is not the same - pin rejected
     * -1 - failed to check on the database, check log
     */
    public int check_resetcode(String code) throws SQLException{
        String query = "SELECT * FROM CONFIGURATION where entrc_user_ask_pin = ?";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1,code);       
        try{
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return 1;
            }
            return 0;
            
        }catch(SQLException e){
            log("Failed to check CONFIGURATION table ("+e.toString());
            return -1;
        }
    }
    
    /**
     * Function from checking show ip code user input
     * @param code
     * @return Integer
     * @throws SQLException 
     */
    public int check_showipcode(String code) throws SQLException{
        String query = "SELECT * FROM CONFIGURATION where entrc_admin_show_ip = ?";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1,code);       
        try{
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return 1;
            }
            return 0;
            
        }catch(SQLException e){
            log("Failed to check CONFIGURATION table ("+e.toString());
            return -1;
        }
    }
    /**
     * Function for checking admin setting enable code
     * @param code
     * @return int
     * @throws SQLException 
     * return codes:
     * 1 - pin accepted on the database - pin correct
     * 0 - pin is not the same - pin rejected
     * -1 - failed to check on the database, check log
     */
    public int check_admincode(String code) throws SQLException{
        String query = "SELECT * FROM CONFIGURATION where entrc_admin_manage_pin = ?";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1,code);       
        try{
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return 1;
            }
            return 0;
            
        }catch(SQLException e){
            log("Failed to check CONFIGURATION table ("+e.toString());
            return -1;
            }
    }
    /**
     * Function for checking manage code for opening managment window
     * @param code
     * @return
     * @throws SQLException 
     */
    public int check_managecode(String code) throws SQLException{
        String query = "SELECT * FROM CONFIGURATION where entrc_user_manage_pin = ?";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1,code);       
        try{
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return 1;
            }
            return 0;
            
        }catch(SQLException e){
            log("Failed to check CONFIGURATION table ("+e.toString());
            return -1;
            }
    }
    
    /**
     * Function for checking user credentials
     * @param code
     * @return
     * @throws SQLException
     * return codes:
     * 1 - check successful - user in the database
     * 0 - check unsuccessful - wrong pin
     * -1 - database connector error
     */
    public int check_credentials(String code) throws SQLException{
        String query = "SELECT * FROM WORKER WHERE worker_pin = ?";
        
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setString(1, code);
        
        try{
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return 1;
            }
            else{
                return 0;
            }
        
        }catch(SQLException e){
            log("Failed to check credentials ("+e.toString());
            return -1;
        }   
    }
    
    /**
     * Function for checking messages to user ( for showing in the logging screen )
     * @param worker_id
     * @return String
     * @throws SQLException 
     */
    public String check_message(int worker_id) throws SQLException{
        String query = "SELECT user_message_content FROM USER_MESSAGE where worker_id = ? or worker_id = 1 and user_message_seen = 0;";
        PreparedStatement ppst = con.prepareStatement(query);
        ppst.setInt(1, worker_id);
        
        try{
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return rs.getString("user_message_content");
            }
            return null;
        }
        catch(SQLException e){
            log("Failed to get message to user: "+e.toString());
            return null;
        }
    }
    
    /**
     * Function for updating message for worker ( setting it seen )
     * @param worker_id
     * @return
     * @throws SQLException 
     */
    public boolean set_message_seen(int worker_id) throws SQLException{
        String query = "UPDATE USER_MESSAGE SET user_message_seen = 1 where worker_id = ?;";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        ppst.setInt(1,worker_id);
        
        try{
            ppst.execute();
            return true;
        }catch(SQLException e){
            log("Failed to set message seen ("+e.toString()+")");
            return false;
        }
    }
    
    /**
     * Function for getting all workers from database
     * @return
     * @throws SQLException 
     */
    public ArrayList<String> get_all_workers() throws SQLException{
        ArrayList<String> data_toRet = new ArrayList<>();
        String query = "SELECT * from WORKER;";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        try{
            ResultSet rs = ppst.executeQuery();
            
            
            while ( rs.next() ){
                if ( rs.getInt("worker_id") != 1 ){
                    data_toRet.add(rs.getString("worker_name")+" "+rs.getString("worker_surname")+"(id:"
                        +Integer.toString(rs.getInt("worker_id"))+":)");
                }
            }
            
            return data_toRet;
        }catch(SQLException e){
            log("Failed to gather all workers ("+e.toString()+")");
            return null;
        }
    }
    /**
     * Function for generating random pin
     * @return String
     */
    String random_pin_generator(){
       
       String data = "";
       
       for (int i = 0 ; i < 4 ; i++){
           int new_int = ThreadLocalRandom.current().nextInt(0, 9);
           
           data = data + Integer.toString(new_int);
       }
       return data;
   }
    
    /**
     * Function for enrolling new pin for the user
     * @return
     * @throws SQLException 
     */
    String enroll_pin() throws SQLException{
        String query = "SELECT worker_pin from WORKER";
        ArrayList<String> pin_collection = new ArrayList<>();
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        try{
            
            ResultSet rs = ppst.executeQuery();
            
            while( rs.next() ){
                pin_collection.add(rs.getString("worker_pin"));
            }
            
            String pin = "";
            
            while(pin_collection.contains(pin) || pin.equals("")){
                pin = random_pin_generator();
            }
            
            return pin;
        }catch(SQLException e){
            log("Failed to enroll pin ("+e.toString()+")");
            return null;
        }
    }
    
    /**
     * Function for reset pin for user
     * @param worker_id
     * @return String
     * @throws SQLException 
     */
    public String reset_pin(int worker_id) throws SQLException{
        String new_pin = enroll_pin();
        
        String query = "UPDATE WORKER SET worker_pin = ? WHERE worker_id = ?;";
        
        PreparedStatement ppst = con.prepareStatement(query);
        
        ppst.setString(1,new_pin);
        ppst.setInt(2,worker_id);
        
        try{
            ppst.execute();
            return new_pin;
        }catch(SQLException e){
            log("Failed to reset pin for user(id:"+worker_id+") ("+e.toString()+")");
            return null;
        }
    }
    
    /**
     * Function for setting barcodes in database
     * @param to_set
     * @return
     * @throws SQLException 
     */
    boolean set_barcode_data(BarCodeCreator to_set) throws SQLException{
        LocalDateTime todayLocalDate = LocalDateTime.now( ZoneId.of( "Europe/Warsaw" ) );
        String query = "INSERT INTO BARCODE_DATA "
                + "(barcode_date,worker_id,barcode_raw_data)\n"
                + "VALUES\n"
                + "(?,?,?);";
        
        try{
            PreparedStatement ppst = con.prepareStatement(query);
        
            ppst.setObject(1,todayLocalDate);
            ppst.setInt(2,get_worker_id_bypin(to_set.worker_pin));
            to_set.generate_barcode();
            //ppst.setString(3,to_set.barcode_object.toString());
            
            ppst.execute();
            return true;
            
        }catch(SQLException e){
            log("Failed to set barcode data ("+e.toString()+")");
            return false;
        }
    }
}
