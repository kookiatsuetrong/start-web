import java.io.FileReader;
import start.web.Server;
import start.web.Context;
import java.util.Map;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;

/*
List of session variable
email           ---> Email for redirecting mechanism
code            ---> Verification Code
photo-code      ---> Verification Code (BASE64)
activation-code ---> Activation Code
message         ---> Error message
*/

class Main {
	
	void start() {
		readConfiguration();
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
		
		server.handleError(Main::showError);
	}
	
	static boolean enableEmail  = true;
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
				case "emailAddress"  -> Main.emailAddress  = tokens[1];
				case "emailPassword" -> Main.emailPassword = tokens[1];
				case "emailServer"   -> Main.emailServer   = tokens[1];
				case "emailSender"   -> Main.emailSender   = tokens[1];
				case "emailPort"     -> Main.emailPort     = tokens[1];
				case "emailSecurity" -> Main.emailSecurity = tokens[1];
			}
		}	
	}
	
	static Object askEmail(Context context) {
		String code = Tool.randomPhotoCode();
		String photoCode = Tool.createPhotoCode(code);
		HttpSession s = context.request.getSession(true);
		s.setAttribute("code", code);
		s.setAttribute("photo-code", photoCode);
		return context.forward("/WEB-INF/user-ask-email.jsp");
	}
	
	static Object checkEmail(Context context) {
		HttpSession session = context.getSession(true);
		String code = (String)session.getAttribute("code");
		if (code == null) code = "";
		session.removeAttribute("code");
		String photoCode = context.getParameter("code");
		
		if (code.equals(photoCode)) {		
			String email = context.getParameter("email");
			User user = Storage.getUserByEmail(email);
			session.setAttribute("email", email);

			if (user == null) {
				// This email is a new user
				String activation = Tool.randomActivationCode();
				session.setAttribute("activation-code", activation);
				Email e = new Email();
				e.sendActivationCode(email, activation);
				return context.redirect("/user-register");
			}

			// This email is in the database, go to login
			return context.redirect("/user-login");
		}
		
		session.setAttribute("message", "Invalid photo code");
		return context.redirect("/user-check-email");
	}
	
	static Object showRegisterPage(Context context) {
		HttpSession session = context.getSession(true);
		String email = (String)session.getAttribute("email");
		if (email == null) {
			return context.redirect("/user-check-email");
		}
		return context.forward("/WEB-INF/user-register.jsp");
	}
	
	static Object createAccount(Context context) {
		HttpSession session = context.getSession(true);
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
			session.setAttribute("message", "Invalid first name");
			return context.redirect("/user-register");
		}
		
		if (lastName.length() < 2) {
			session.setAttribute("message", "Invalid last name");
			return context.redirect("/user-register");
		}
		
		if (password.length() < 8) {
			session.setAttribute("message", 
					"Password must have 8 characters or more");
			return context.redirect("/user-register");
		}
		
		if (password.matches(".*[0-9].*") == false) {
			session.setAttribute("message", "Password must have a number");
			return context.redirect("/user-register");
		}
		
		if (password.matches(".*[A-Z].*") == false) {
			session.setAttribute("message", "Password must have an uppercase");
			return context.redirect("/user-register");
		}
		
		if (password.matches(".*[a-z].*") == false) {
			session.setAttribute("message", "Password must have a lowercase");
			return context.redirect("/user-register");
		}
		
		if (code.equals(activation) == false) {
			session.setAttribute("message", "Invalid activation code");
			return context.redirect("/user-register");
		}
		
		session.removeAttribute("code");
		session.removeAttribute("email");
		session.removeAttribute("password");
		session.removeAttribute("first-name");
		session.removeAttribute("last-name");
		session.removeAttribute("activation-code");
		Storage.createAccount(email, password, firstName, lastName);
		User user = Storage.checkPassword(email, password);
		
		session.setAttribute("user", user);
		return context.redirect("/user-profile");
	}
	
	static Object showError(Context context) {
		return context.forward("/WEB-INF/error.jsp");
	}
	
	static Object showLogInPage(Context context) {
		return context.forward("/WEB-INF/user-login.jsp");
	}
	
	static Object checkPassword(Context context) {
		var email    = context.getParameter("email");
		var password = context.getParameter("password");
		
		User user = Storage.checkPassword(email, password);
		if (user == null) {
			return context.redirect("/user-login");
		} else {
			var session = context.getSession(true);
			session.setAttribute("email", email);
			session.setAttribute("user", user);
			return context.redirect("/user-profile");
		}
	}
	
	static Object showProfilePage(Context context) {
		var session = context.getSession(true);
		User user = (User)session.getAttribute("user");
		if (user == null) {
			return context.redirect("/user-check-email");
		} else {
			return context.forward("/WEB-INF/user-profile.jsp");
		}
	}
	
	static Object showLogOutPage(Context context) {
		var session = context.getSession(false);
		if (session != null) {
			session.removeAttribute("email");
			session.removeAttribute("user");
		}
		return context.forward("/WEB-INF/user-logout.jsp");
	}
}
