/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *Main class
 * @author jakubwawak
 */
public class Entrc {
    
    final static String version = "v1.0.0GM2";
    static Configuration run_configuration;
    static Database_Connector database;
    static Scanner user_handler;
    static int debug = 0;
    
    public static void main(String[] args) throws SQLException, IOException, FileNotFoundException, URISyntaxException, ClassNotFoundException{

        // debug mode
        if (debug == 1){
            new Test();
            System.exit(0);
        }
        
	create_banner();
        database = new Database_Connector();
        run_configuration = new Configuration("config.txt");
        user_handler = new Scanner(System.in);
        try{
            if ( run_configuration.prepared ){
                database.connect(run_configuration.ip, run_configuration.database, run_configuration.databaseuser, run_configuration.databasepass);
                run_configuration.show_configuration();
                if( database.connected ){
                    new main_user_window(database);
                }
                else{
                    System.out.println("Błąd połączenia z bazą danych. Skontaktuj się z administratorem");
                }
            }
            else{
                System.out.println("Brak pliku konfiguracyjnego!");
                System.out.println("Czy chcesz podac recznie dane?(y/n)");
                if ( user_handler.nextLine().equals("y") ){
                    // user want to type own configuration
                    String ip,databasename,databaseuser,databasepassword;
                    System.out.print("ip:");
                    ip = user_handler.nextLine();
                    run_configuration.ip = ip;
                    System.out.print("database name:");
                    databasename = user_handler.nextLine();   
                    run_configuration.database = databasename;
                    System.out.print("user:");
                    databaseuser = user_handler.nextLine();
                    run_configuration.databaseuser = databaseuser;
                    System.out.print("password:");
                    databasepassword = user_handler.nextLine();
                    run_configuration.databasepass = databasepassword;
                    database.connect(ip, databasename, databaseuser,databasepassword);

                    if ( database.connected ){
                        database.config = run_configuration;
                        database.config.copy_configuration();
                        new main_user_window(database);
                    }
                    else{
                        System.out.println("Błąd połączenia z bazą danych. Skontaktuj się z administratorem");
                    }

                }
                else{
                    System.out.println("Nastapilo wyjscie z programu");
                }
            }
        
        }catch(NullPointerException e){
            System.out.println("Wystapił krytyczny bład ("+e.toString()+")");
            System.exit(0);
        }

    }
    /**
     * Function for printing welcome banner
     */
    static void create_banner(){
        String banner = " _____ _   _ _____ ____   ____ \n" +
                        "| ____| \\ | |_   _|  _ \\ / ___|\n" +
                        "|  _| |  \\| | | | | |_) | |    \n" +
                        "| |___| |\\  | | | |  _ <| |___ \n" +
                        "|_____|_| \\_| |_| |_| \\_\\\\____|";
        String addons = "VERSION: "+version + "   Jakub Wawak\n";
        
        System.out.println(banner);
        System.out.print(addons);
    }
}
