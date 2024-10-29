<!DOCTYPE html>
<html>
	<head>
		<title>Profile</title>
		<link rel="stylesheet" href="/normalize.css" />
		<link rel="stylesheet" href="/main.css" />
	</head>
	<body>
		<header></header>
		<main>
			<section class="container">
				<%
				// User user = (User)session.getAttribute("user");
				String email = (String)session.getAttribute("email");
					
				%>
				<p>Email: <%= email %></p>
				
				<a href="/user-logout" class="button">Log Out</a>
			</section>
		</main>
		<footer></footer>
	</body>
</html>

