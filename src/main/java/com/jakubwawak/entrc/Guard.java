/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import com.jakubwawak.database.Database_Connector;
import java.sql.SQLException;

/**
 *Class for maintaining user data
 * @author jakubwawak
 */
public class Guard {
    
    public String pin;                             // pin entered by user
    Database_Connector database;            // connection to the database
    
    /**
     * accepted - flag for checking user credentials
     * states:
     * 1 - user accepted, pin correct
     * 0 - no attempt to accept user
     * -1 - user not accepted, pin incorrect
     */
    public int accepted;                           
    
    
    // Constructor
    public Guard(String user_pin, Database_Connector database){
        pin = user_pin;
        this.database = database;
        accepted = 0;
    }
    
    /**
     * Function for checking user data
     * @throws SQLException 
     */
    public void check_credentials() throws SQLException{
        
        if ( database.check_credentials(pin) == 1 ){
            accepted = 1;
        }
        else{
            accepted = -1;
        }
    }
}
