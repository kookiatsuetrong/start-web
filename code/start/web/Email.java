package start.web;

public class Email {

	public void sendActivationCode(String target, String code) {
		EmailSender es = new EmailSender();
		es.target  = target;
		es.subject = "Activation Code";
		es.content = "The activation code is <b>" + 
						code + "</b>";
		es.start();
	}
	
	public void sendResetCode(String target, String code) {
		EmailSender es = new EmailSender();
		es.target  = target;
		es.subject = "Reset Code";
		es.content = "The reset code is <b>" + 
						code + "</b>";
		es.start();
	}
	
}
