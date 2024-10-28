import start.web.Server;
import start.web.Context;
import java.util.Map;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;

class Main {
	
	void start() {
		var server = Server.getInstance();
		
		server.handle("/user-register-login")   .by(Main::askEmail);
		server.handle("/user-register-login")   .via("POST")
												.by(Main::checkEmail);
		
		server.handle("/user-register") .by(Main::showRegisterPage);
		server.handle("/user-profile")  .by(Main::showProfilePage);
		server.handle("/user-logout")   .by(Main::showLogOutPage);
		
		server.handle("/user-login")    .by(Main::showLogInPage);
		server.handle("/user-login")    .via("POST")
										.by(Main::checkPassword);
		
		server.handleError(Main::showError);
	}
	
	static Object askEmail(Context context) {
		return context.forward("/WEB-INF/user-ask-email.jsp");
	}
	
	static Object checkEmail(Context context) {
		String email = context.getParameter("email");
		User user = Storage.getUserByEmail(email);
		HttpSession session = context.getSession(true);
		session.setAttribute("email", email);
		
		if (user == null) {
			return context.redirect("/user-register");
		} else {
			return context.redirect("/user-login");
		}
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
			return context.redirect("/user-register-login");
		}
		
		Boolean loggedIn = (Boolean)session.getAttribute("loggedIn");
		if (loggedIn == null) {
			return context.redirect("/user-register-login");
		}
		
		if (loggedIn == false) {
			return context.redirect("/user-register-login");
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
