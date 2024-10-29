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
				String email = (String)session.getAttribute("email");
				if (email == null) {
					email = "";
				}
			%>
			<section class="container">
				<form class="user-form" method="post">
					<h3>Create New Account</h3>
					<p>
						The activation code has been
						sent to the given email.
					</p>
					
					<input name="activation-code"
						placeholder="Activation Code"
						autofocus
						required
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
						required
						/>
					
					<input name="first-name"
						placeholder="First Name"
						required
						/>
					
					<input name="last-name"
						placeholder="Last Name"
						required
						/>
					
					<button>Create Account</button>
				</form>
			</section>
		</main>
		<footer></footer>
	</body>
</html>
