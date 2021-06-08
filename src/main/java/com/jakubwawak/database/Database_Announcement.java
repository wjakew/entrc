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
 * Object for loading storing and checking announcement data on database
 * @author jakubwawak
 */
public class Database_Announcement {
    
    Database_Connector database;
    
    /**
     * Constructor
     * @param database 
     */
    public Database_Announcement(Database_Connector database){
        this.database = database;
    }
    
    /**
     * Function for loading announcement data from database
     * @return String 
     */
    public String load_newestannouncement() throws SQLException{
        String query = "SELECT announcement_data from ANNOUNCEMENT ORDER BY announcement_id DESC LIMIT 1;";
        
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                return String.valueOf(rs.getObject("announcement_data"));
            }
            return "brak danych";
        }catch(SQLException e){
            database.log("Error getting last record from table ANNOUNCEMENT ("+e.toString()+")");
            return null;
        }
    }
    
}
