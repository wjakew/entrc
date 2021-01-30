/*
by Jakub Wawak
kubawawak@gmail.com
all rights reserved
 */
package com.jakubwawak.entrc;

import java.io.File;
import javax.mail.PasswordAuthentication;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *Object for maintaining e-mail connections
 * @author kubaw
 */
public class Mail_Sender2 {
    
    // variables for e-mail addresses
    String to;
    String from;
    // variables for storing data
    Properties properties;
    Session session;
    
    // variables for maintaining 
    String message,title;
    File attachment;
    boolean attachment_set;
    
    // variables for administration data
    String administration_mail = "main.tes.instruments@gmail.com";
    String administration_pass = "minidysk";
    
    //Constructor
    /**
     *Main object constructor
     * @param e_mail_to
     * @param e_mail_from 
     */
    Mail_Sender2(String e_mail_to,String e_mail_from){
        to = e_mail_to;
        from = e_mail_from;
        attachment = null;
        properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        
        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(administration_mail, administration_pass);
            }
        });
        session.setDebug(true);
    }
    
    /**
     * Function for setting title
     * @param title 
     */
    void set_title(String title){
        this.title = title;
    }
    
    /**
     * Function for setting message
     * @param message 
     */
    void set_message(String message){
        this.message = message;
    }
    
    /**
     * Function for adding 
     * @param attachment_src
     * @return boolean
     */
    boolean set_attachment(String attachment_src){
        attachment = new File(attachment_src);
        return attachment.exists();
    }
    
    /**
     * Function for sending e-mail
     */
    void send_message() throws MessagingException{
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(title);

            // Now set the actual message
            message.setText(this.message);
            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    
}
