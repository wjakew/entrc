/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import com.github.sarxos.webcam.Webcam;
import com.mysql.cj.conf.ConnectionUrlParser;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.imageio.ImageIO;


/**
 *Class used for testing components
 * @author jakubwawak
 */
public class Test {
    
    Database_Connector dc;
    
    
    Test() throws SQLException, IOException{
        dc = new Database_Connector();        
        dc.connect("localhost", "entrc_database", "root", "password");

        //new main_user_window(dc);
        
        Webcam webcam = Webcam.getDefault();        // loading default webcam
        webcam.open();                              // opening stream
        
        ConnectionUrlParser.Pair<String,String> database_data = dc.prepare_photo_name(dc.get_worker_id_bypin("1111"));
        String photo_name = database_data.right + database_data.left+".png";    // preparing photo name

        File photo_file = new File(photo_name);
        ImageIO.write(webcam.getImage(), "PNG", photo_file);
        
        System.out.println(photo_file.getAbsolutePath());

        
    }
    
   void show_arraylist(ArrayList<String> to_show){
       System.out.println("Showing: "+to_show);
       for(String line : to_show){
           System.out.println(line);
       }
   }
    
    
    
}
