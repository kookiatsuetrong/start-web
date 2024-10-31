<!DOCTYPE html>
<html>
	<head>
		<title>Reset Password</title>
		<link rel="stylesheet" href="/normalize.css" />
		<link rel="stylesheet" href="/main.css" />
	</head>
	<body>
		<header></header>
		<main>
			<section class="container">
				<form class="user-form" method="post">
					<h3>Reset Password</h3>
					<input name="email" 
						   placeholder="Your Email" 
						   autofocus 
						   required
						   autocomplete="off"
						   />
					<%
					String message = (String)session
										.getAttribute("message");
					if (message == null) message = "";
					session.removeAttribute("message");
					
					String photoCode = (String)session
										.getAttribute("photo-code");
					if (photoCode == null) photoCode = "";
					session.removeAttribute("photo-code");
					%>
					<span class="duo">
						<img src="data:image/png;base64, <%= photoCode %>" />
						<input name="code" 
							placeholder="4-Digit Code" 
							required 
							autocomplete="off"
							/>
					</span>
					<button>Get Reset Code</button>
					<%= message %>
				</form>
			</section>
		</main>
		
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
		
		<%@include file="/WEB-INF/footer.jsp" %>
	</body>
</html>
