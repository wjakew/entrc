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
            return null;
        }catch(SQLException e){
            database.log("Failed to get value from ProgramCode database");
            return null;
        } 
    }
    
}
