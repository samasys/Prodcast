package com.primeforce.prodcast.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by sarathan732 on 8/7/2016.
 */
public class Amazon {

    static final String FROM = "prodcastadmin@samasysdev.awsapps.com";   // Replace with your "From" address. This address must be verified.
    static final String TO = "t_saravanan@hotmail.com";  // Replace with a "To" address. If your account is still in the
    // sandbox, this address must be verified.

    static final String BODY = "This email was sent through the Amazon SES SMTP interface by using Java.";
    static final String SUBJECT = "Amazon SES test (SMTP interface accessed using Java)";

    // Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
    static final String SMTP_USERNAME = "AKIAJ32EKFNBCZIPIA5A";  // Replace with your SMTP username.
    static final String SMTP_PASSWORD = "AlYhHv63z8rx8WFHJIn9a5Jh9ggNrcPGYWMSVQRIw4G/";  // Replace with your SMTP password.

    // Amazon SES SMTP host name. This example uses the US West (Oregon) region.
    static final String HOST = "email-smtp.us-east-1.amazonaws.com";

    // The port you will connect to on the Amazon SES SMTP endpoint. We are choosing port 25 because we will use
    // STARTTLS to encrypt the connection.
    static final int PORT = 25;

    public static void sendEmail(String emailId , String subject, String message) throws Exception {

        // Create a Properties object to contain connection configuration information.
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.user", SMTP_USERNAME);
        props.put("mail.smtp.password", SMTP_PASSWORD );

        // Set properties indicating that we want to use STARTTLS to encrypt the connection.
        // The SMTP session will begin on an unencrypted connection, and then the client
        // will issue a STARTTLS command to upgrade to an encrypted connection.
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.host" , HOST);

        // Create a Session object to represent a mail session with the specified properties.
        Session session = Session.getInstance(props, new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD );
            }
        });

        // Create a message with the specified information.
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailId));
        msg.setSubject(subject);
        msg.setContent(message,"text/plain");

        // Create a transport.
        Transport transport = session.getTransport("smtps");

        // Send the message.
            System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
        try{
            // Connect to Amazon SES using the SMTP username and password you specified above.
            transport.connect(HOST,  SMTP_USERNAME , SMTP_PASSWORD);
            System.out.println(transport.isConnected());
            transport.sendMessage(msg, msg.getAllRecipients());
            System.out.println("Email sent!");
        }
        finally
        {
            // Close and terminate the connection.
            transport.close();
        }
    }

    public static void main(String[] args)  throws Exception{
        Amazon.sendEmail("t_saravanan@hotmail.com", "Password email from PRODCAST" , "Your password is welcome");
    }
}