/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;


import com.jakubwawak.database.Database_Connector;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.Date;

/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */

/**
 *Object for creating barcodes
 * @author kubaw
 */
public class BarCodeCreator {
    
    public String worker_pin;
    Date date;
    public Barcode128 barcode_object;
    Database_Connector database;
    
    //Constructor
    BarCodeCreator(String worker_pin,Database_Connector database){
        this.worker_pin = worker_pin;
        this.database = database;
        date = new Date();
        barcode_object = null;
    }
    
    /**
     * Function for parsing date object
     * @return String
     */
    String parse_date(){
        String[] elements = date.toString().split(" ");
        String[] time = elements[3].split(":");
        return elements[2]+time[0]+time[1]+time[2];
    }
    
    /**
     * Function for generating barcode
     */
    public void generate_barcode(){
        barcode_object = new Barcode128();
        String data = parse_date()+worker_pin;
        barcode_object.setGenerateChecksum(true);
        barcode_object.setCode(data);
    }
    
    /**
     * Function for creating pdf
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws SQLException 
     */
    void create_pdf() throws FileNotFoundException, DocumentException, SQLException{
        
        try{
            Document document = new Document(new Rectangle(PageSize.A4));   
            String file_name = parse_date()+worker_pin+".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file_name));
            document.open();
            document.add(new Paragraph(database.get_worker_data(worker_pin))); 
        
            document.add(barcode_object.createImageWithBarcode(writer.getDirectContent(),null,null));
            
            document.close();
        }catch(NullPointerException e){
            database.log("No generated barcode ("+e.toString()+")");
        }
    }
}
