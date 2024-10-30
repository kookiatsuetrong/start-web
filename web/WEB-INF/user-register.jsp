<!DOCTYPE html>
<html>
	<head>
		<title>Register</title>
		<link rel="stylesheet" href="/normalize.css" />
		<link rel="stylesheet" href="/main.css" />
	</head>
	<body>
		<header></header>
		<main>
			<%
				String code      = (String)session.getAttribute("code");
				String email     = (String)session.getAttribute("email");
				String password  = (String)session.getAttribute("password");
				String firstName = (String)session.getAttribute("first-name");
				String lastName  = (String)session.getAttribute("last-name");

				if (code      == null) code      = "";
				if (email     == null) email     = "";
				if (password  == null) password  = "";
				if (firstName == null) firstName = "";
				if (lastName  == null) lastName  = "";
				session.removeAttribute("code");
				session.removeAttribute("email");
				session.removeAttribute("password");
				session.removeAttribute("first-name");
				session.removeAttribute("last-name");
				
				String message = (String)session.getAttribute("message");
				if (message == null) message = "";
				session.removeAttribute("message");
				
				String codeType = "text";
				if (start.web.EmailSender.emailEnabled == false) {
					codeType = "hidden";
				}
			%>
			<section class="container">
				<form class="user-form" method="post">
					<h3>Create New Account</h3>
					<% if (start.web.EmailSender.emailEnabled) { %>
					<p>
						The activation code has been
						sent to the given email.
					</p>
					<% }                                        %>
					
					<input name="activation-code"
						placeholder="Activation Code"
						autofocus
						autocomplete="off"
						required
						value="<%= code %>"
						type="<%= codeType %>"
						/>
					
					<input name="email"
						placeholder="Your Email" 
						autocomplete="off"
						readonly
						value="<%= email %>"
						/>
					
					<input name="password" 
						type="password"
						placeholder="Your Password"
						autocomplete="off"
						required
						value="<%= password %>"
						/>
					
					<input name="first-name"
						placeholder="First Name"
						autocomplete="off"
						required
						value="<%= firstName %>"
						/>
					
					<input name="last-name"
						placeholder="Last Name"
						autocomplete="off"
						required
						value="<%= lastName %>"
						/>
					
					<button>Create Account</button>
					<%= message %>
				</form>
			</section>
		</main>
		<footer></footer>
	</body>
</html>
