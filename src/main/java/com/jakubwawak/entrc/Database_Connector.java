/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.ZoneId;
/**
 * Database object connector
 * @author jakubwawak
 */
public class Database_Connector {
    
    // version of database 
    final String version = "vC.0.2";
    // header for logging data
    // connection object for maintaing connection to the database
    Connection con;
    
    // variable for debug purposes
    final int debug = 1;
    
    
    boolean connected;                      // flag for checking connection to the database
    String ip;                              // ip data for the connector
    String database_name;                   // name of the database
    String database_user,database_password; // user data for cred
    ArrayList<String> database_log;         // collection for storing data
    
    /**
     * Constructor
     */
    Database_Connector() throws SQLException{
        con = null;
        database_log = new ArrayList<>();
        connected = false;
        ip = "";
        database_name = "";
        database_user = "";
        database_password = "";
        //log("Started! Database Connector initzialazed");
    }
    
    /**
     * Function for gathering database log
     * @param log 
     */
    void log(String log) throws SQLException{
        java.util.Date actual_date = new java.util.Date();
        database_log.add("("+actual_date.toString()+")"+" - "+log);
    
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
    
    /**
     * Function for printing info data on the screen
     * @param data 
     */
    void info_print(String data){
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
    void connect(String ip,String database_name,String user,String password) throws SQLException{
        this.ip = ip;
        this.database_name = database_name;
        database_user = user;
        database_password = password;
        
        String login_data = "jdbc:mysql://"+this.ip+"/"+database_name+"?" +
                                   "user="+database_user+"&password="+database_password;
        try{
            con = DriverManager.getConnection(login_data);
            connected = true;
            log("Connected succesfully");
        }catch(SQLException e){
            connected = false;
            log("Failed to connect to database ("+e.toString()+")");
        }
        log("Database string: "+login_data);
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
    int get_worker_id_bypin(String pin) throws SQLException{
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
     * Function for getting worker name and surname
     * @param pin
     * @return String
     * @throws SQLException 
     * Returns null if worker don't exist
     * NOTE: less than probable, func used only after get_worker_id_bypin(String pin)
     */
    String get_worker_data(String pin) throws SQLException{
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
    
    String get_worker_data(int id) throws SQLException{
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
    int log_LOGIN_FAILED(String pin) throws SQLException{
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
     * Function for logging LOG_ACCEPT action
     * @param pin
     * @return Integer
     * @throws SQLException 
     * return codes:
     * 0 - can't find user with that pin
     * 1 - user accepted and log
     */
    int log_LOGIN_ACCEPT(String pin) throws SQLException{
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
    int log_INFO(int worker_id,String log) throws SQLException{
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
    int log_ENTER(int worker_id,String photo_src) throws SQLException{
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
    int get_lastid_logENTER(int worker_id) throws SQLException{
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
    int log_EXIT(int worker_id,String photo_src) throws SQLException{
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
    int get_lastid_logEXIT(int worker_id) throws SQLException{
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
     * Function for setting entrance to the database
     * @param worker_id
     * @param photo_src
     * @return Boolean
     * @throws SQLException 
     */
    boolean set_entrance_event(int worker_id,String photo_src) throws SQLException{
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
        String query = "SELECT * from ENTRANCE WHERE worker_id = ? and entrance finished = 0 ORDER BY entrance_id DESC LIMIT 1;";
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
    int get_lastid_ENTRANCE(int worker_id) throws SQLException{
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
    int get_lastid_EXIT(int worker_id) throws SQLException{
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
    int set_exit_event(int worker_id,String photo_src) throws SQLException{
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
    Pair<LocalDateTime,String> get_last_user_event(int worker_id) throws SQLException{
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
    int check_exitcode(String code) throws SQLException{
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
    int check_resetcode(String code) throws SQLException{
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
     * Function for checking user credentials
     * @param code
     * @return
     * @throws SQLException
     * return codes:
     * 1 - check successful - user in the database
     * 0 - check unsuccessful - wrong pin
     * -1 - database connector error
     */
    int check_credentials(String code) throws SQLException{
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
}
