/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *Class used for testing components
 * @author jakubwawak
 */
public class Test {
    
    Database_Connector dc;
    
    
    // Constructor for the object
    Test() throws SQLException, IOException, ClassNotFoundException, FileNotFoundException, DocumentException{
        dc = new Database_Connector();        
        dc.connect("localhost", "entrc_database", "root", "password");
        
        BarCodeCreator bcc = new BarCodeCreator("1888",dc);
        
        bcc.generate_barcode();
        
        bcc.create_pdf();
        
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
