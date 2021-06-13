/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.database;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;

/**
 *Object 
 * @author jakubwawak
 */
public class Database_Photo {
    
    Database_Connector database;
    
    /**
     * Constructor
     * @param database 
     */
    public Database_Photo(Database_Connector database) throws SQLException{
        this.database = database;
        database.log("Database_Photo object invoked");
    }
    
    /**
     * Function for loading photo to database
     * @param src_path
     * @return int
     */
    public int load_photo(int worker_id,String src_path,String name) throws SQLException, FileNotFoundException{
        database.log("Trying to put photo to database");
        
        String query = "INSERT INTO PHOTO_LIB\n" +
                        "(worker_id,photo_path,photo_name,photo_source)\n" +
                        "VALUES\n" +
                        "(?,?,?,?);";
        
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            ppst.setInt(1,worker_id);
            ppst.setString(2,src_path);
            ppst.setString(3,name);
            InputStream in = new FileInputStream(src_path);
            
            ppst.setBlob(4,in);
            
            ppst.execute();
            return 1;
        
        }catch(Exception e){
            database.log("Failed to load photo on database ("+e.toString()+")");
            return -1;
        }
    }
    
    /**
     * Function for downloading photos to the computer from database
     * @param photo_id
     * @return String
     */
    public String download_photo(int photo_id) throws SQLException{
        String query = "SELECT * FROM PHOTO_LIB WHERE photo_id = ?;";
        
        try{
            PreparedStatement ppst = database.con.prepareStatement(query);
            
            ppst.setInt(1,photo_id);
            
            
            ResultSet rs = ppst.executeQuery();
            
            if ( rs.next() ){
                
                // loading photo
                Blob blob = rs.getBlob("img");
                int blobLength = (int) blob.length();  
                byte[] bytes = blob.getBytes(1, blobLength);
                blob.free();
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
                
                String name = rs.getString("photo_name");
         
                File outputfile = new File(name);
                ImageIO.write(img, "jpg", outputfile);
            
                database.log("Photo (id"+photo_id+") was downloaded by admin");
                return outputfile.getAbsolutePath();
            }
            
            else{
                return "INFO: Brak szukanego zdjęcia w bazie";
            }
            
        }catch(Exception e){
            database.log("Failed to download photo ("+e.toString()+")");
            return "ERROR "+e.toString();
        }
    }
    
}
