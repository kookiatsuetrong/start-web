<!DOCTYPE html>
<html>
	<head>
		<title>Register or Log In</title>
		<link rel="stylesheet" href="/normalize.css" />
		<link rel="stylesheet" href="/main.css" />
	</head>
	<body>
		<header></header>
		<main>
			<section class="container">
				<form class="user-form" method="post">
					<h3>Register or Log In</h3>
					<input name="email" 
						   placeholder="Your Email" 
						   autofocus 
						   required
						   autocomplete="off"
						   />
					<%
					String message   = (String)session
										.getAttribute("message");
					message = message == null ? "" : message;
					String photoCode = (String)session
										.getAttribute("photo-code");
					photoCode = photoCode == null ? "" : photoCode;
					%>
					<span class="duo">
						<img src="data:image/png;base64, <%= photoCode %>" />
						<input name="code" 
							placeholder="4-Digit Code" 
							required 
							autocomplete="off"
							/>
					</span>
					<button>Register / Log In</button>
					<%
					out.print(message); 
					session.removeAttribute("message");
					%>
				</form>
			</section>
		</main>
		
		<footer></footer>
		<style>
			.duo {
				display: grid;
				grid-template-columns: 4rem 1fr;
				column-gap: .5rem;
			}
			img {
				background: #bbb;
				padding: .65rem;
				margin-top: .005rem;
				border-radius: .35rem;
			}
		</style>
	</body>
</html>

