package com.noize.medicalcenter.email;

import com.noize.medicalcenter.notification.AlertNotification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendGmail {

    // Public method to send email, with meaningful parameter names
    public static void sendEmailWithGmail(String from, String password, String to, String subject, String messageBody) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Compose the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(messageBody);

            // Send the email
            Transport.send(message);

            // Display success notification
            new AlertNotification(
                    "Email Sent Successfully!",
                    "Email sent to your registered email. \nPlease check your email: " + to,
                    "success.png",
                    "OK"
            ).start();

        } catch (MessagingException e) {
            // Print stack trace and show error notification
            e.printStackTrace();
            new AlertNotification(
                    "Action Required!",
                    "An issue occurred while sending email. \nPlease try again.",
                    "unsuccess.png",
                    "OK"
            ).start();
        }
    }
}
