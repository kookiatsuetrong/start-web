<!DOCTYPE html>
<html>
	<head>
		<title>Home</title>
		<link rel="stylesheet" href="/normalize.css" />
		<link rel="stylesheet" href="/main.css" />
	</head>
	<body>
		<header></header>
		<main>
			<section class="container">
			<%
				out.println("This is a JSP page");
			%>
			<br/>
			<br/>
			<a class="button" href="/user-check-email">Register</a>
			<a class="button" href="/user-check-email">Log In</a>
			</section>
		</main>
		<footer></footer>
	</body>
</html>
