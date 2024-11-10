import start.web.Email;
import start.web.Storage;
import start.web.Tool;
import start.web.User;

import start.web.Server;
import start.web.Context;

import java.util.Map;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import start.web.EmailSender;

/*
List of session variable
user            ---> Detail of current user
email           ---> Email for redirecting mechanism
code            ---> Verification Code (4-Digit)
photo-code      ---> Verification Code (BASE64)
activation-code ---> Activation Code
message         ---> Error message
*/

class Main {
	
	void start() {
		var server = Server.getInstance();
		
		server.handle("/user-check-email")  .by(Main::askEmail);
		server.handle("/user-check-email")  .via("POST")
											.by(Main::checkEmail);

		server.handle("/user-register") .by(Main::showRegisterPage);
		server.handle("/user-register") .via("POST")
										.by(Main::createAccount);

		server.handle("/user-profile")  .by(Main::showProfilePage);
		server.handle("/user-logout")   .by(Main::showLogOutPage);
		
		server.handle("/user-login").by(Main::showLogInPage);
		server.handle("/user-login").via("POST")
									.by(Main::checkPassword);
		
		server.handle("/reset-password")
								.by(Main::showResetPasswordPage);
		server.handle("/reset-password")
								.via("POST")
								.by(Main::checkResetPassword);
		
		server.handle("/reset-password-code")
								.by(Main::showResetPasswordCode);
		
		server.handle("/reset-password-code")
								.via("POST")
								.by(Main::resetPasswordCode);
		
		server.handle("/reset-password-final")
								.by(Main::showResetPasswordFinal);
		
		server.handle("/contact").by(Main::showContactPage);
		server.handle("/contact").via("POST")
								.by(Main::saveContactDetail);
		server.handle("/contact-final").by(Main::showContactFinalPage);
		
		server.handleError(Main::showError);
	}
	
	static Object askEmail(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		String code = Tool.randomPhotoCode();
		String photoCode = Tool.createPhotoCode(code);
		session.setAttribute("code", code);
		session.setAttribute("photo-code", photoCode);
		return context.render("/WEB-INF/user-ask-email.jsp");
	}
	
	static Object checkEmail(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		String code = (String)session.getAttribute("code");
		if (code == null) code = "";
		session.removeAttribute("code");
		String photoCode = context.getParameter("code");
		
		if (code.equals(photoCode) == false) {
			session.setAttribute("message", ErrorMessage.INCORRECT_PHOTO_CODE);
			return context.redirect("/user-check-email");	
		}
		
		String email = context.getParameter("email");
		user = Storage.getUserByEmail(email);
		session.setAttribute("email", email);

		if (user == null) {
			// This email is a new email, create new account
			String activation = Tool.randomActivationCode();
			session.setAttribute("activation-code", activation);
			
			if (EmailSender.emailEnabled) {
				Email e = new Email();
				e.sendActivationCode(email, activation);
			} else {
				session.setAttribute("code", activation);
			}
			
			return context.redirect("/user-register");
		}

		// This email is in the database, go to login
		return context.redirect("/user-login");
	}
	
	static Object showRegisterPage(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		
		String email = (String)session.getAttribute("email");
		if (email == null) {
			return context.redirect("/user-check-email");
		}
		
		return context.render("/WEB-INF/user-register.jsp");
	}
	
	static Object createAccount(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		String activation = (String)session.getAttribute("activation-code");
		
		String code      = context.getParameter("activation-code");
		String email     = context.getParameter("email");
		String password  = context.getParameter("password");
		String firstName = context.getParameter("first-name");
		String lastName  = context.getParameter("last-name");
		if (code      == null) code      = "";
		if (email     == null) email     = "";
		if (password  == null) password  = "";
		if (firstName == null) firstName = "";
		if (lastName  == null) lastName  = "";
		
		session.setAttribute("code",       code);
		session.setAttribute("email",      email);
		session.setAttribute("password",   password);
		session.setAttribute("first-name", firstName);
		session.setAttribute("last-name",  lastName);
		
		if (firstName.length() < 2) {
			session.setAttribute("message", ErrorMessage.INVALID_FIRST_NAME);
			return context.redirect("/user-register");
		}
		
		if (lastName.length() < 2) {
			session.setAttribute("message", ErrorMessage.INVALID_LAST_NAME);
			return context.redirect("/user-register");
		}
		
		if (password.length() < 8) {
			session.setAttribute("message", ErrorMessage.PASSWORD_TOO_SHORT);
			return context.redirect("/user-register");
		}
		
		if (password.matches(".*[0-9].*") == false) {
			session.setAttribute("message", ErrorMessage.PASSWORD_NUMBER);
			return context.redirect("/user-register");
		}
		
		if (password.matches(".*[A-Z].*") == false) {
			session.setAttribute("message", ErrorMessage.PASSWORD_UPPERCASE);
			return context.redirect("/user-register");
		}
		
		if (password.matches(".*[a-z].*") == false) {
			session.setAttribute("message", ErrorMessage.PASSWORD_LOWERCASE);
			return context.redirect("/user-register");
		}
		
		if (code.equals(activation) == false) {
			session.setAttribute("message", 
						ErrorMessage.INCORRECT_ACTIVATION_CODE);
			return context.redirect("/user-register");
		}
		
		session.removeAttribute("code");
		session.removeAttribute("email");
		session.removeAttribute("password");
		session.removeAttribute("first-name");
		session.removeAttribute("last-name");
		session.removeAttribute("activation-code");
		Storage.createAccount(email, password, firstName, lastName);
		
		user = Storage.checkPassword(email, password);
		session.setAttribute("user", user);
		
		if (EmailSender.emailEnabled) {
			Email e = new Email();
			e.sendWelcome(email);
		}
		return context.redirect("/user-profile");
	}
	
	static Object showError(Context context) {
		return context.render("/WEB-INF/error.jsp");
	}
	
	static Object showLogInPage(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		String email = (String)session.getAttribute("email");
		if (email == null) {
			context.redirect("/user-check-email");
		}
		return context.render("/WEB-INF/user-login.jsp");
	}
	
	static Object checkPassword(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		var email    = context.getParameter("email");
		var password = context.getParameter("password");
		
		user = Storage.checkPassword(email, password);
		if (user == null) {
			session.setAttribute("message", ErrorMessage.INCORRECT_PASSWORD);
			return context.redirect("/user-login");
		}
		
		session.setAttribute("email", email);
		session.setAttribute("user", user);
		return context.redirect("/user-profile");
	}
	
	static Object showProfilePage(Context context) {
		var session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user == null) {
			// ask user to login before continue
			return context.redirect("/user-check-email");
		}
		
		// display user profile page
		return context.render("/WEB-INF/user-profile.jsp");
	}
	
	static Object showLogOutPage(Context context) {
		var session = context.getSession(false);
		if (session != null) {
			session.removeAttribute("email");
			session.removeAttribute("user");
			session.invalidate();
		}
		return context.render("/WEB-INF/user-logout.jsp");
	}
	
	static Object showResetPasswordPage(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		String code = Tool.randomPhotoCode();
		String photoCode = Tool.createPhotoCode(code);
		session.setAttribute("code", code);
		session.setAttribute("photo-code", photoCode);
		return context.render("/WEB-INF/reset-password.jsp");
	}
	
	static Object checkResetPassword(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		String email = context.getParameter("email");
		String code  = context.getParameter("code");
		if (email == null) email = "";
		if (code  == null) code  = "";
		
		session.setAttribute("email", email);
		
		String value = (String)session.getAttribute("code");
		
		user = Storage.getUserByEmail(email);
		if (user == null) {
			session.setAttribute("message", ErrorMessage.EMAIL_NOT_FOUND);
			return context.redirect("/reset-password");
		}
		
		if (code.equals(value) == false) {
			session.setAttribute("message", ErrorMessage.INCORRECT_PHOTO_CODE);
			return context.redirect("/reset-password");
		}
		
		String activation = Tool.randomActivationCode();
		session.setAttribute("activation-code", activation);
		session.setAttribute("email", email);
		
		if (EmailSender.emailEnabled) {
			Email e = new Email();
			e.sendResetCode(email, activation);
		}
		
		// TODO: How user can reset password without email system
		// e.g. let administrator to send reset code directly to the email
		
		return context.redirect("/reset-password-code");
	}

	static Object showResetPasswordCode(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		String activation = (String)session.getAttribute("activation-code");
		String email = (String)session.getAttribute("email");
		
		if (activation == null || email == null) {
			return context.redirect("/reset-password");
		}
		return context.render("/WEB-INF/reset-password-code.jsp");
	}
	
	static Object resetPasswordCode(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		
		String email = (String)session.getAttribute("email");
		String code  = context.getParameter("activation-code");
		if (code == null) code = "";
		
		String value = (String)session.getAttribute("activation-code");
		if (code.equals(value) == false) {
			session.setAttribute("message", ErrorMessage.INCORRECT_RESET_CODE);
			return context.redirect("/reset-password-code");
		}
		
		String password = context.getParameter("password");
		String confirm = context.getParameter("confirm");
		
		if (password == null || password.length() < 8) {
			session.setAttribute("message", 
						"Password is at least 8 characters");
			return context.redirect("/reset-password-code");
		}
		
		if (password.equals(confirm) == false) {
			session.setAttribute("message", 
					ErrorMessage.INCORRECT_CONFIRM_PASSWORD);
			return context.redirect("/reset-password-code");
		}
		
		if (password.matches(".*[0-9].*") == false) {
			session.setAttribute("message", ErrorMessage.PASSWORD_NUMBER);
			return context.redirect("/reset-password-code");
		}
		
		if (password.matches(".*[A-Z].*") == false) {
			session.setAttribute("message", ErrorMessage.PASSWORD_UPPERCASE);
			return context.redirect("/reset-password-code");
		}
		
		if (password.matches(".*[a-z].*") == false) {
			session.setAttribute("message", ErrorMessage.PASSWORD_LOWERCASE);
			return context.redirect("/reset-password-code");
		}
		
		Storage.resetPassword(email, password);
		
		if (EmailSender.emailEnabled) {
			Email e = new Email();
			e.sendResetConfirmation(email);
		}
		
		session.setAttribute("message", ErrorMessage.PASSWORD_RESET_SUCCESS);
		return context.redirect("/reset-password-final");
	}
	
	static Object showResetPasswordFinal(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			return context.redirect("/user-profile");
		}
		return context.render("/WEB-INF/reset-password-final.jsp");
	}
	
	static Object showContactPage(Context context) {
		HttpSession session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user != null) {
			session.setAttribute("email", user.email);
		}
		String code = Tool.randomPhotoCode();
		String photoCode = Tool.createPhotoCode(code);
		session.setAttribute("code", code);
		session.setAttribute("photo-code", photoCode);
		return context.render("/WEB-INF/contact.jsp");
	}

	static Object saveContactDetail(Context context) {
		String topic  = context.getParameter("topic");
		String detail = context.getParameter("detail");
		String email  = context.getParameter("email");
		String code   = context.getParameter("code");
		
		if (topic  == null) topic  = "";
		if (detail == null) detail = "";
		if (email  == null) email  = "";
		if (code   == null) code   = "";
		
		HttpSession session = context.getSession(true);
		String photoCode = (String)session.getAttribute("code");
		session.removeAttribute("code");
		
		if (code.equals(photoCode) == false) {
			session.setAttribute("topic", topic);
			session.setAttribute("detail", detail);
			session.setAttribute("email", email);
			session.setAttribute("message", ErrorMessage.INCORRECT_PHOTO_CODE);
			return context.redirect("/contact");
		}
	
		int record = Storage.saveContactMessage(topic, detail, email);
		if (EmailSender.emailEnabled) {
			Email e = new Email();
			e.sendContactSavedConfirmation(email);
		}
		
		String path = context.request.getServletContext().getRealPath("");
		path += File.separator + "uploaded";
		int n = 1;
		try {
			for (Part part : context.request.getParts()) {
				String name = part.getName();
				String type = part.getContentType();
				System.out.println(name + " " + type);
				if (type == null) continue;
				
				String file = path + File.separator + 
								"file-" + record + "-" + n;
				switch (type) {
					case "image/png"  -> file += ".png";
					case "image/jpg"  -> file += ".jpg";
					case "image/jpeg" -> file += ".jpg";
				}
				part.write(file);
				n++;
			}
		} catch (Exception e) { }
		
		session.setAttribute("message", ErrorMessage.CONTACT_SAVE_SUCCESS);
		return context.redirect("/contact-final");
	}
	
	static Object showContactFinalPage(Context context) {
		return context.render("/WEB-INF/contact-final.jsp");
	}
	
}
