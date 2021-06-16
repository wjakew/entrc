/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import com.jakubwawak.database.Database_Connector;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *Object for checking run enviroment
 * @author jakubwawak
 */
public class RuntimeChecker {
    
    String current_macaddress;
    String LICENSE_KEY = "";
    
    boolean validate_flag;
    boolean directory_tree_flag;
    boolean first_run;
    public boolean license_load;
    /**
     * Scenario:
     * 
     * before running program:
     * 1. checking if the directory tree is made
     *      yes - pass
     *      no -  create (send email that program is running for the first time)
     *            save macaddress of the machine
     *            send saved in the code number of the license and macadress
     * 2. look for the configuration file, if found - copy to the directory location
     * after loging screen
     * 1. get the current macaddress and LICENSE_KEY if match with the database continue
     *      if not prompt and close the program, send email to the administrator.
     */
    
    /**
     * Main contructor
     */
    public RuntimeChecker(){
        System.out.println("Runtime checker invoked..");
        current_macaddress = "";
        license_load = false;
    }
    
    /**
     * Function for loading licenses from file
     * @throws FileNotFoundException 
     */
    void load_license() throws FileNotFoundException{
        String path = "license.txt";
        
        try{
            File fr = new File(path);
            Scanner sc = new Scanner(fr);
            
            if (sc.hasNextLine()){
                LICENSE_KEY = sc.nextLine();
            }
            
            System.out.println("Loaded LICENSE_KEY: "+LICENSE_KEY);
            license_load = true;
        }catch(Exception e){
            System.out.println("Failed to open license file. ("+e.toString()+")");
            license_load = false;
        }
        
    }
    /**
     * Function for running before run scenario
     */
    void before_check(){
        return;
    }
    
    /**
     * Function for running after run scenario
     * @param database 
     */
    void after_check(Database_Connector database) throws UnknownHostException, SocketException, SQLException, FileNotFoundException{
        System.out.println("Running after check..");
        current_macaddress = database.get_local_MACAddress();
        load_license();
        if ( database.compare_licenses(LICENSE_KEY) == 1 ){
            System.out.println("License key compared succesfully");
            if ( database.evaluation_copy ){
                database.log("RuntimeChecker - this copy of the program is evaluation only");
            }
            validate_flag = true;
        }
        else{
            if ( database.check_license_exists() == 1){
                System.out.println("License key comparation failed");
                validate_flag = false;
            }
            else{
                System.out.println("RunetimeChecker failed to validate copy");
                validate_flag = false;
                first_run = false;
            }
        }
    }
    
    /**
     * Function for proving that copy of the program is production ready
     * @return boolean
     */
    boolean validate_runetime(){
        if ( LICENSE_KEY.equals("LICENSE")){
            System.out.println("THIS COPY OF THE PROGRAM IS INVALID");
            validate_flag = false;
            return false;
        }
        return true;
    }
    
}
