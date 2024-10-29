import start.web.Server;
import start.web.Context;
import java.util.Map;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;

/*
List of session variable
email        ---> Email for redirecting mechanism
loggedIn     ---> TODO: Change to user
code         ---> Verification Code
photo-code   ---> Verification Code BASE64
message      ---> Error message
*/

class Main {
	
	void start() {
		var server = Server.getInstance();
		
		server.handle("/user-check-email")  .by(Main::askEmail);
		server.handle("/user-check-email")  .via("POST")
											.by(Main::checkEmail);

		server.handle("/user-register") .by(Main::showRegisterPage);
		server.handle("/user-register") .via("POST")
										.by(Main::showRegisterPage);

		server.handle("/user-profile")  .by(Main::showProfilePage);
		server.handle("/user-logout")   .by(Main::showLogOutPage);
		
		server.handle("/user-login").by(Main::showLogInPage);
		server.handle("/user-login").via("POST")
									.by(Main::checkPassword);
		
		server.handleError(Main::showError);
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
		String photoCode = context.getParameter("code");
		code = code == null ? "" : code;
		
		if (code.equals(photoCode)) {		
			String email = context.getParameter("email");
			User user = Storage.getUserByEmail(email);
			session.setAttribute("email", email);

			if (user == null) {
				return context.redirect("/user-register");
			}

			return context.redirect("/user-login");
		}
		session.setAttribute("message", "Invalid photo code");
		return context.redirect("/user-check-email");
	}
	
	static Object showRegisterPage(Context context) {
		return context.forward("/WEB-INF/user-register.jsp");
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
			session.setAttribute("loggedIn", true);
			return context.redirect("/user-profile");
		}
	}
	
	static Object showProfilePage(Context context) {
		var session = context.getSession(false);
		if (session == null) {
			return context.redirect("/user-check-email");
		}
		
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");
		if (loggedIn == null) {
			return context.redirect("/user-check-email");
		}
		
		if (loggedIn == false) {
			return context.redirect("/user-check-email");
		}

		return context.forward("/WEB-INF/user-profile.jsp");
	}
	
	static Object showLogOutPage(Context context) {
		var session = context.getSession(false);
		if (session != null) {
			session.removeAttribute("email");
			session.removeAttribute("loggedIn");
		}
		return context.forward("/WEB-INF/user-logout.jsp");
	}
}
