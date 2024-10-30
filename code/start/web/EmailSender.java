package start.web;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.FileReader;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class EmailSender extends Thread {

	String target;
	String subject;
	String content;
	
	public EmailSender() {
		readConfiguration();
	}
	
	public static boolean enableEmail  = false;
	static String emailSender   = "";
	static String emailSecurity = "";
	static String emailServer   = "";
	static String emailPort     = "";
	static String emailAddress  = "";
	static String emailPassword = "";
	
	void readConfiguration() {
		String buffer = "";
		try (FileReader fr = new FileReader("setup.txt")) {
			while (true) {
				int k = fr.read();
				if (k == -1) break;
				buffer += (char)k;
			}
		} catch (Exception e) { }
		
		String [] lines = buffer.split("\n");
		for (String s : lines) {
			String [] tokens = s.trim().split("=");
			if (tokens.length != 2) continue;
			if (tokens[0] == null) continue;
			if (tokens[1] == null) continue;
			tokens[0] = tokens[0].trim();
			tokens[1] = tokens[1].trim();

			switch (tokens[0]) {
				case "emailAddress"  -> emailAddress  = tokens[1];
				case "emailPassword" -> emailPassword = tokens[1];
				case "emailServer"   -> emailServer   = tokens[1];
				case "emailSender"   -> emailSender   = tokens[1];
				case "emailPort"     -> emailPort     = tokens[1];
				case "emailSecurity" -> emailSecurity = tokens[1];
			}
		}
	}
	
	Map<String, String> settings = new TreeMap<>();
	
	@Override
	public void run() {

		try {
			Properties detail = new Properties();
			detail.put("mail.smtp.host",            emailServer);
			detail.put("mail.smtp.port",            emailPort);
			detail.put("mail.smtp.ssl.protocols",   emailSecurity);
			detail.put("mail.smtp.auth",            "true");
			detail.put("mail.smtp.starttls.enable", "true");
			Session session = Session.getInstance(
				detail, 
				new EmailCredential
					(emailAddress, emailPassword)
			);

			Message message = new MimeMessage(session);
			String sender = emailSender + 
							"<" + emailAddress + ">";
			message.setFrom(new InternetAddress(sender));
			message.setRecipients(
				Message.RecipientType.TO,
				InternetAddress.parse(target)
			);
			message.setSubject(subject);

			MimeBodyPart body = new MimeBodyPart();
			body.setContent(content, "text/html; charset=utf-8");
			Multipart part = new MimeMultipart();
			part.addBodyPart(body);

			message.setContent(part);
			Transport.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

class EmailCredential extends Authenticator {
	EmailCredential(String address, String password) {
		this.address = address;
		this.password = password;
	}
	
	String address;
	String password;

	@Override protected PasswordAuthentication 
	getPasswordAuthentication() {
		return new PasswordAuthentication(address, password);
	}
}
