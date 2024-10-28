<!DOCTYPE html>
<html>
	<head>
		<title>Log In</title>
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
					<h3>Log In</h3>
					<input name="email" 
						   placeholder="Your Email" 
						   autocomplete="off"
						   readonly
						   value="<%= email %>"
						   />
					<input name="password" 
						   type="password"
						   placeholder="Your Password"
						   autofocus
						   />
					<button>Log In</button>
				</form>
			</section>
		</main>
		<footer></footer>
	</body>
</html>
