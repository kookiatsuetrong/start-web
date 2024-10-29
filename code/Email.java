import java.util.Properties;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.InternetAddress;
import java.util.Map;
import java.util.TreeMap;

class Email {

	void sendActivationCode(String target, String code) {
		EmailSender es = new EmailSender();
		es.target  = target;
		es.subject = "Activation Code";
		es.content = "The activation code is <b>" + 
						code + "</b>";
		es.start();
	}
}

class EmailSender extends Thread {

	String target;
	String subject;
	String content;
	
	Map<String, String> settings = new TreeMap<>();
	
	@Override
	public void run() {

		try {
			Properties detail = new Properties();
			detail.put("mail.smtp.host",            Main.emailServer);
			detail.put("mail.smtp.port",            Main.emailPort);
			detail.put("mail.smtp.ssl.protocols",   Main.emailSecurity);
			detail.put("mail.smtp.auth",            "true");
			detail.put("mail.smtp.starttls.enable", "true");
			Session session = Session.getInstance(
				detail, 
				new EmailCredential
					(Main.emailAddress, Main.emailPassword)
			);

			Message message = new MimeMessage(session);
			String sender = Main.emailSender + 
							"<" + Main.emailAddress + ">";
			message.setFrom(new InternetAddress(sender));
			message.setRecipients(
				Message.RecipientType.TO,
				InternetAddress.parse(target));
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
