package edu.mayo.phenoportal.server.utils;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.mayo.phenoportal.shared.User;

public class SmtpClient {

    private static Logger s_logger = Logger.getLogger(SmtpClient.class.getName());

    private static final String REGISTRTAION_SUCCESS_TITLE = "Phenotype Portal Registration";
    private static final String ROLE_UPGRADE_REQUEST_TITLE = "Phenotype Portal User Upgrade Request";
    private static final String ROLE_UPGRADE_RESPONSE_TITLE = "Phenotype Portal User Upgrade Response";

    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String USER_NAME = "USER_NAME";

    public static boolean sendRegistrationSuccessEmail(final String host, final String from,
            final String pw, final String port, String messageText, User user) {

        messageText = messageText.replaceAll(FIRST_NAME, user.getFirstName());
        messageText = messageText.replaceAll(LAST_NAME, user.getLastName());
        messageText = messageText.replaceAll(USER_NAME, user.getUserName());

        boolean success = true;

        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.from", from);
        properties.put("mail.smtp.auth", true);

        Session session = Session.getDefaultInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pw);
            }

        });

        try {

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set the RFC 822 "From" header field using the
            // value of the InternetAddress.getLocalAddress method.
            message.setFrom(new InternetAddress(from));

            // if (validateEmailAddress(to1)) {
            InternetAddress[] toAaddress = { new InternetAddress(user.getEmail()) };
            message.setRecipients(Message.RecipientType.TO, toAaddress);

            // Set the "Subject" header field.
            message.setSubject(REGISTRTAION_SUCCESS_TITLE);

            // Sets the given String as this part's content,
            // with a MIME type of "text/plain".
            message.setContent(messageText, "text/plain");

            // Send message
            Transport.send(message);
            s_logger.log(Level.INFO, "Registration email sent to : " + user.getFirstName() + " "
                    + user.getLastName() + " (" + user.getUserName() + ")");

        } catch (MessagingException mex) {
            success = false;
            s_logger.log(Level.SEVERE,
                    "exceptions sending out registration email: " + mex.toString());
        }

        return success;
    }

    public static boolean sendRequestPersmissionUpgradeEmailAdmin(final String host,
            final String adminEmail, final String pw, final String port, String messageText,
            User user) {

        messageText = messageText.replaceAll(FIRST_NAME, user.getFirstName());
        messageText = messageText.replaceAll(LAST_NAME, user.getLastName());
        messageText = messageText.replaceAll(USER_NAME, user.getUserName());

        boolean success = true;

        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.from", adminEmail);
        properties.put("mail.smtp.auth", true);

        Session session = Session.getDefaultInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, pw);
            }

        });

        try {

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set the RFC 822 "From" header field using the
            // value of the InternetAddress.getLocalAddress method.
            message.setFrom(new InternetAddress(adminEmail));

            // if (validateEmailAddress(to1)) {
            InternetAddress[] toAaddress = { new InternetAddress(adminEmail) };
            message.setRecipients(Message.RecipientType.TO, toAaddress);

            // Set the "Subject" header field.
            message.setSubject(ROLE_UPGRADE_REQUEST_TITLE);

            // Sets the given String as this part's content,
            // with a MIME type of "text/plain".
            message.setContent(messageText, "text/plain");

            // Send message
            Transport.send(message);
            s_logger.log(Level.INFO, "Role Upgrade Request email sent to : " + adminEmail + " "
                    + user.getLastName() + " (" + user.getUserName() + ")");

        } catch (MessagingException mex) {
            success = false;
            s_logger.log(Level.SEVERE,
                    "exceptions sending out Role Upgrade Request emaill: " + mex.getStackTrace());
        }
        return success;
    }

    public static boolean sendRequestPersmissionUpgradeEmailUser(String host,
            final String adminEmail, final String pw, final String port, String messageText,
            User user) {

        messageText = messageText.replaceAll(FIRST_NAME, user.getFirstName());
        messageText = messageText.replaceAll(LAST_NAME, user.getLastName());

        boolean success = true;

        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.from", adminEmail);
        properties.put("mail.smtp.auth", true);

        Session session = Session.getDefaultInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, pw);
            }

        });

        try {

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set the RFC 822 "From" header field using the
            // value of the InternetAddress.getLocalAddress method.
            message.setFrom(new InternetAddress(adminEmail));

            // if (validateEmailAddress(to1)) {
            InternetAddress[] toAaddress = { new InternetAddress(user.getEmail()) };
            message.setRecipients(Message.RecipientType.TO, toAaddress);

            // Set the "Subject" header field.
            message.setSubject(ROLE_UPGRADE_REQUEST_TITLE);

            // Sets the given String as this part's content,
            // with a MIME type of "text/plain".
            message.setContent(messageText, "text/plain");

            // Send message
            Transport.send(message);
            s_logger.log(Level.INFO, "Role Upgrade Request email sent to : " + adminEmail + " "
                    + user.getLastName() + " (" + user.getUserName() + ")");

        } catch (MessagingException mex) {
            success = false;
            s_logger.log(Level.SEVERE,
                    "exceptions sending out Role Upgrade Request emaill: " + mex.getStackTrace());
        }
        return success;
    }

    public static boolean sendResponsePersmissionUpgradeEmail(String host, final String adminEmail,
            final String pw, final String port, String messageText, User user) {

        messageText = messageText.replaceAll(FIRST_NAME, user.getFirstName());
        messageText = messageText.replaceAll(LAST_NAME, user.getLastName());

        boolean success = true;

        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.from", adminEmail);
        properties.put("mail.smtp.auth", true);

        Session session = Session.getDefaultInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(adminEmail, pw);
            }

        });

        try {

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set the RFC 822 "From" header field using the
            // value of the InternetAddress.getLocalAddress method.
            message.setFrom(new InternetAddress(adminEmail));

            message.setRecipients(Message.RecipientType.TO, user.getEmail());

            // Set the "Subject" header field.
            message.setSubject(ROLE_UPGRADE_RESPONSE_TITLE);

            // Sets the given String as this part's content,
            // with a MIME type of "text/plain".
            message.setContent(messageText, "text/plain");

            // Send message
            Transport.send(message);
            s_logger.log(Level.INFO, "Role Upgrade Response email sent to : " + adminEmail + " "
                    + user.getLastName() + " (" + user.getUserName() + ")");

        } catch (MessagingException mex) {
            success = false;
            s_logger.log(Level.SEVERE, "exceptions sending out Role Upgrade Response emaill: "
                    + mex.getStackTrace());
        }
        return success;
    }

    private boolean validateEmailAddress(String email) {
        Pattern rfc2822 = Pattern
                .compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
        if (rfc2822.matcher(email).matches()) {
            return true;
        }
        return false;

    }
}
