import start.web.Server;
import start.web.Context;
import java.util.Map;
import jakarta.json.JsonObject;

class Main {
	
	void start() {
		var server = Server.getInstance();
		server.handle("/register")  .by(Main::showRegisterPage);
		server.handle("/profile")   .by(Main::showProfilePage);
		server.handle("/logout")    .by(Main::showLogOutPage);
		
		server.handle("/login")     .by(Main::showLogInPage);
		server.handle("/login")     .via("POST")
									.by(Main::checkPassword);
		
		server.handleError(Main::showError);
	}
	
	static Object showRegisterPage(Context context) {
		return context.forward("/WEB-INF/register.jsp");
	}
	
	static Object showError(Context context) {
		return context.forward("/WEB-INF/error.jsp");
	}
	
	static Object showLogInPage(Context context) {
		return context.forward("/WEB-INF/login.jsp");
	}
	
	static Object checkPassword(Context context) {
		var email    = context.getParameter("email");
		var password = context.getParameter("password");
		
		if ("user".equals(email) && 
			"password".equals(password)) {
			var session = context.getSession(true);
			session.setAttribute("email", email);
			return context.redirect("/profile");
		} else {
			return context.redirect("/login");
		}
	}
	
	static Object showProfilePage(Context context) {
		var session = context.getSession(false);
		if (session == null) {
			return context.redirect("/login");
		}
		var email = (String)session.getAttribute("email");
		if (email == null) {
			return context.redirect("/login");
		}
		
		return context.forward("/WEB-INF/profile.jsp");
	}
	
	static Object showLogOutPage(Context context) {
		var session = context.getSession(false);
		if (session != null) {
			session.removeAttribute("email");
		}
		return context.forward("/WEB-INF/logout.jsp");
	}
}
