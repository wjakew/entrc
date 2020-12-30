/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 *Class used for testing components
 * @author jakubwawak
 */
public class Test {
    
    Database_Connector dc;
    
    
    Test() throws SQLException{
        dc = new Database_Connector();        
        
        dc.connect("localhost", "entrc_database", "root", "password");
        //dc.log_LOGIN_ACCEPT("1111");
        //System.out.println(dc.get_worker_id_bypin("1111"));

        new main_user_window(dc);
        //dc.log_INFO(1, "Testuje dodawanie");
        //show_arraylist(dc.database_log);
        
    }
    
   void show_arraylist(ArrayList<String> to_show){
       System.out.println("Showing: "+to_show);
       for(String line : to_show){
           System.out.println(line);
       }
   }
    
    
    
}
