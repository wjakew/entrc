/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import com.jakubwawak.database.Database_Connector;
import com.itextpdf.text.DocumentException;
import com.jakubwawak.entrc_gui.welcome_message_2_window;
import com.jakubwawak.entrc_gui.welcome_message_window;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *Class used for testing components
 * @author jakubwawak
 */
public class Test {
    
    Database_Connector dc;
    
    
    // Constructor for the object
    Test() throws SQLException, IOException, ClassNotFoundException, FileNotFoundException, DocumentException, UnknownHostException, SocketException, URISyntaxException{
        dc = new Database_Connector();        
        
        new welcome_message_2_window(dc,"test","130");
        
    }
    
    /**
     * Function for showing arraylist
     * @param to_show 
     */
   void show_arraylist(ArrayList<String> to_show){
       System.out.println("Showing: "+to_show);
       to_show.forEach(line -> {
           System.out.println(line);
        });
    }
}
