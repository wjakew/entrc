/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 *Object mantain loading and using configuration file
 * @author jakubwawak
 * configuration file:
 * ip%data
 * database%
 * databaseuser%
 * databasepass%
 */
public final class Configuration {
    
    String file_src = "";
    File configuration_file;
    BufferedReader file_reader;
    
    // file data in variables
    public String ip, database, databaseuser,databasepass;
    
    public boolean ex_flag;
    public boolean prepared;
    
    ArrayList<String> file_lines;
    
    //Constructor
    public Configuration(String configuration_src) throws FileNotFoundException, IOException, URISyntaxException{
        file_src = configuration_src;
        file_lines = new ArrayList<>();
        System.out.println("\nWyszukiwanie konfiguracji w:");
        System.out.println(new java.io.File(".").getCanonicalPath());
        run();
    }
    // Constructor without arguments
    public Configuration() throws IOException{
        file_src = "config.entrconf";
        file_lines = new ArrayList<>();
        System.out.println("\nZainicjalizowano bez sciezki");
    }
    
    /**
     * Function for coping new configuration file to the current app folder
     */
    public void copy_configuration() throws IOException{
        try (FileWriter writer = new FileWriter("config.entrconf")) {
            writer.write("ip%"+ip+"\n");
            writer.write("database%"+database+"\n");
            writer.write("databaseuser%"+databaseuser+"\n");
            writer.write("databasepass%"+databasepass+"\n");
        }catch(Exception e){
            System.out.println("ERROR LOG: "+e.toString());
        }
    }
    
    
    /**
     * Function for preparing main data for the object
     */
    public void run() throws FileNotFoundException, IOException{
        configuration_file = new File(file_src);
        
        if ( configuration_file.exists() && !configuration_file.isDirectory()){
            ex_flag = true;
            // we found the file
            file_reader = new BufferedReader(new FileReader(file_src));
            get_lines(); //loading lines from file
            
            //show_file();
            
            if ( validate() ){
                ip = file_lines.get(0).split("%")[1];
                database = file_lines.get(1).split("%")[1];
                databaseuser = file_lines.get(2).split("%")[1];
                databasepass = file_lines.get(3).split("%")[1];
                prepared = true;
            }
            else{
                prepared = false;
            }
        }
        else{
            ex_flag = false;
        }
    }
    
    /**
     * Function for showing raw file
     */
    public void show_file(){
        System.out.println("Showing raw file:");
        file_lines.forEach(line -> {
            System.out.println(line);
        });
    }
    
    /**
     * Function for getting lines from file
     * @return ArrayList
     */
    public void get_lines() throws IOException{
        String line = file_reader.readLine();
        while( line!= null){
            file_lines.add(line);
            line = file_reader.readLine();
        }  
    }
    
    /**
     * Function for validation data from file
     * @return boolean
     */
    public boolean validate(){
        return file_lines.size() >= 4;
    }
    
    /**
     * Function for showing configuration
     */
    public void show_configuration(){
        System.out.println("ip: "+ip);
        System.out.println("database: "+database);
        System.out.println("database user: "+databaseuser);
        System.out.println("database password: "+databasepass);
    }
}
