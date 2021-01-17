/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *Object for dumping data from database
 * @author kubaw
 */
public class Database_Data_Dump {
    
    /**
     * Main goals:
     * 
     * 1. dump in unified way:
     * - version for admin
     *      entrance data
     *      exit data
     *      user log data
     *      admin log data
     *   - version for client app
     *      worker data
     *      progam log
     */
    
    Database_Connector database;
    
    Database_Data_Dump(Database_Connector database){
        this.database = database;
    }
    
    /**
     * Function for dumping worker data from database;
     * @return ArrayList
     * @throws SQLException 
     */
    ArrayList<String> dump_worker_data() throws SQLException{
        ArrayList<String> dump = new ArrayList<>();
        
        String query = "SELECT * FROM WORKER;";
        
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            
            ResultSet rs = ppst.executeQuery();

            while ( rs.next()){
                if ( rs.getInt("worker_id") != 1){
                    dump.add(rs.getString("worker_name")+","+rs.getString("worker_surname")
                        +","+rs.getString("worker_position")+","+rs.getString("worker_pin"));
                }
                
            }
            return dump;
        }catch(SQLException e){
            database.log("Failed to prepare dump (worker) ("+e.toString()+")");
            return null;
        }
        
    }
    
}
