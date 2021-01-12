/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *Class used for testing components
 * @author jakubwawak
 */
public class Test {
    
    Database_Connector dc;
    
    
    Test() throws SQLException, IOException, ClassNotFoundException{
        dc = new Database_Connector();        
        dc.connect("localhost", "entrc_database", "root", "password");

        //new main_user_window(dc);
        //new reset_pin_window(null,true,dc);
        
    }
    
   void show_arraylist(ArrayList<String> to_show){
       System.out.println("Showing: "+to_show);
       for(String line : to_show){
           System.out.println(line);
       }
    }
}
