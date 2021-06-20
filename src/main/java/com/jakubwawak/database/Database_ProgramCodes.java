/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *Class for mantaining programcodes
 * @author jakubwawak
 */
public class Database_ProgramCodes {
    
    Database_Connector database;
    
    /**
     * Constructor
     * @param database 
     */
    public Database_ProgramCodes(Database_Connector database){
        this.database = database;
    }
    
    /**
     * Function for checking database version
     * @return int
     * return codes:
     * 1 - database version newer but still accepted
     * 2 - database version is the same as implemented
     * 0 - database version not match - too old
     * -1 - database check error
     */
    public int check_database_version(int version) throws SQLException{
        String loaded_version = get_value("DATABASEVERSION");
        try{
            int database_version = Integer.parseInt(loaded_version);
            if ( database_version > version)
                return 1;
            if ( database_version == version){
                return 2;
            }
            return 0;
        }catch(Exception e){
            database.log("Failed to check database version ("+e.toString()+")");
            return -1;
        }
    }
    
    
    /**
     * Function for checking if key exists
     * @param key
     * @return Int
     * @throws SQLException 
     */
    int key_exists(String key) throws SQLException{
        String query = "SELECT * from PROGRAMCODES WHERE programcodes_key = ?;";
        
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            
            ppst.setString(1,key);
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() )
                return 1;
            return 0;
        }catch(SQLException e){
            database.log("Error checking if key exists ("+e.toString()+")");
            return -1;
        }
    }
    
    /**
     * Function for getting value from database
     * @param code
     * @return String
     */
    public String get_value(String code) throws SQLException{
        String query = "SELECT programcodes_value from PROGRAMCODES WHERE programcodes_key = ?;";        
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            
            ppst.setString(1,code);
        
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return rs.getString("programcodes_value");
            }
            return "null";
        }catch(SQLException e){
            database.log("Failed to get value from ProgramCode database");
            return "databaseerror";
        } 
    }
    
    /**
     * Function for setting data on table
     * @param key
     * @param code
     * @return Integer
     * @throws SQLException 
     */
    public int set_value(String key,String code) throws SQLException{
        if ( key_exists(key) == 1 ){
            update_value(key,code);
            return 1;
        }
        else if (key_exists(key) == 0){
            insert_value(key,code);
            return 2;
        }
        else{
            return -1;
        }
    }
    /**
     * Function for updating value on database
     * @param key
     * @param code
     * @return Int
     */
    int update_value(String key,String code) throws SQLException{
        String query = "UPDATE PROGRAMCODES SET programcodes_value = ? WHERE programcodes_key = ?;";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            
            ppst.setString(2,key);
            ppst.setString(1,code);
            
            ppst.execute();
            return 1;
        
        }catch(SQLException e){
            database.log("Failed to update value to PROGRAMCODES ("+e.toString()+")");
            return -1;
        }
    }
    
    /**
     * Function for inserting value to database
     * @param key
     * @param code
     * @return Int
     */
    int insert_value(String key,String code) throws SQLException{
        String query = "INSERT INTO PROGRAMCODES (programcodes_key,programcodes_value)"
                            + "\nVALUES"
                            + "\n(?,?);";
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            
            ppst.setString(1,key);
            ppst.setString(2,code);
            
            ppst.execute();
            return 1;
        
        }catch(SQLException e){
            database.log("Failed to insert value to PROGRAMCODES ("+e.toString()+")");
            return -1;
        }
    }
    
}
