/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import com.jakubwawak.database.Database_Connector;
import com.jakubwawak.entrc_gui.main_user_window;
import com.formdev.flatlaf.FlatLightLaf;
import com.itextpdf.text.DocumentException;
import com.jakubwawak.database.Database_ProgramCodes;
import com.jakubwawak.entrc_gui.main_user_ANC_window;
import com.jakubwawak.entrc_gui.message_window;
import com.jakubwawak.entrc_gui.welcome_message_2_window;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *Main class
 * @author jakubwawak
 */
public class Entrc {
    
    final static String version = "v1.3.0";
    final static int databaseversion = 131;
    final static String build_date = "08.11.2021";
    static Configuration run_configuration;
    static Database_Connector database;
    static Scanner user_handler;
    static int debug = 0;
    static int debug_window = 0;
    public static void main(String[] args) throws SQLException, IOException, FileNotFoundException, URISyntaxException, ClassNotFoundException, DocumentException{
        // debug mode
        FlatLightLaf.setup();
        if (debug == 1){
            new Test();
            //System.exit(0);
        }
        else{
            create_banner();
            database = new Database_Connector();
            run_configuration = new Configuration("config.entrconf");
            user_handler = new Scanner(System.in);
            
            if ( args.length == 0){
                System.out.println("No arguments - gui is starting...");
                new welcome_message_2_window(database,version,Integer.toString(databaseversion),build_date);
            }
            else{
                if ( args.length == 1){
                    System.out.println("Given arguments: "+args[0]);
                    if ( args[0].equals("nogui") ){
                        System.out.println("Argument found: nogui - console is starting");
                        try{
                            if ( run_configuration.prepared ){
                                database.connect(run_configuration.ip, run_configuration.database, run_configuration.databaseuser, run_configuration.databasepass);
                                run_configuration.show_configuration();
                                if( database.connected ){

                                    // RuntimeChecker 
                                    RuntimeChecker rtc = new RuntimeChecker();
                                    rtc.after_check(database);
                                    if ( database.evaluation_copy ){
                                        new message_window(null,true,"Ta kopia programu jest wersją testową.");
                                    }
                                    if (!rtc.validate_flag ){
                                        if ( !rtc.license_load ){
                                            new message_window(null,true,"Błąd podczas odczytu licencji");
                                        }
                                        new message_window(null,true,"Błędne sprawdzanie licencji programu.\nSkontaktuj się z administratorem.\nMAC: "+database.get_local_MACAddress()
                                        +"\nlicencja powinna być zapisana w pliku entrclient.license");
                                        System.exit(0);
                                    }
                                    //end of RuntimeChecker

                                    Database_ProgramCodes dpc = new Database_ProgramCodes(database);

                                    switch( dpc.check_database_version(databaseversion)){
                                        case 0:
                                            database.log("Database version not match");
                                            new message_window(null,true,"Bład wersji bazy danych\n Wersja na maszynie jest zła."
                                                + "\n Prośba o kontakt z administratorem\n"
                                                + "Wymagana wersja: "+databaseversion);
                                            System.exit(0);
                                            break;
                                        case 2:
                                            database.log("Database version is correct");
                                            break;
                                        case 1:
                                            new message_window(null,true,"Wersja bazy jest nowsza niż aplikacji, sprawdź czy nie ma aktualizacji");
                                            break;
                                        case -1:
                                            new message_window(null,true,"Błąd sprawdzania wersji bazy danych");
                                            System.exit(0);
                                            break;
                                    }

                                    try{   

                                        String code = dpc.get_value("CLIENTSTARTUP");

                                        switch(code){
                                            case "NORMAL":
                                                database.log("NORMAL VIEW MODE INVOKED");
                                                new main_user_window(database,version,debug_window);
                                                break;
                                            case "ANC_ADD":
                                                database.log("ANNOUNCEMENT VIEW MODE INVOKED");
                                                new main_user_ANC_window(database,version,debug_window);
                                                break;
                                            default:
                                                new message_window(null,true,"Błąd na bazie: 000");
                                                new main_user_window(database,version,debug_window);
                                                break;
                                        }

                                    }catch(Exception e){
                                        new message_window(null,true,"Save mode activated...");
                                        new main_user_window(database,version,debug_window);
                                    }
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

                                        // RuntimeChecker 
                                        RuntimeChecker rtc = new RuntimeChecker();
                                        rtc.after_check(database);
                                        if ( database.evaluation_copy ){
                                            new message_window(null,true,"Ta kopia programu jest wersją testową.");
                                        }
                                        if (!rtc.validate_flag ){
                                            new message_window(null,true,"Błędne sprawdzanie licencji programu. Skontaktuj się z administratorem");
                                            System.exit(0);
                                        }
                                        if (rtc.first_run ){
                                            new message_window(null,true,"Dziękujemy za dodanie licencji programu Entrc Client");
                                        }
                                        //end of RuntimeChecker

                                        new main_user_window(database,version,debug_window);
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
                    else{
                        System.out.println("Wrong starting argument.");
                        System.out.println("Check documentation");
                    }
                }
            }
        }
    }
    /**
     * Function for printing welcome banner
     */
    static void create_banner() throws UnknownHostException, SocketException{
        String banner = " _____ _   _ _____ ____   ____ \n" +
                        "| ____| \\ | |_   _|  _ \\ / ___|\n" +
                        "|  _| |  \\| | | | | |_) | |    \n" +
                        "| |___| |\\  | | | |  _ <| |___ \n" +
                        "|_____|_| \\_| |_| |_| \\_\\\\____|";
        String addons = " VERSION: "+version + "   Jakub Wawak\n\n";
        addons = addons +"BUILD INFORMATION:\n";
        addons = addons +"build date: "+build_date+"\n";
        addons = addons +"machine local IP:"+get_IP_data()+"\n";
        System.out.println(banner);
        System.out.print(addons);
    }
    /**
     * Function for loading local machine IP
     */
    static String get_IP_data() throws UnknownHostException, SocketException{
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
          }
    }
}
