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
			<%
				String email = (String)session.getAttribute("email");	
				if (email == null) email = "";
				
				String message = (String)session.getAttribute("message");
				if (message == null) message = "";
				session.removeAttribute("message");
			%>
			<section class="container">
				<form class="user-form" method="post">
					<h3>Reset Password</h3>
					
					<input name="email"
						placeholder="Your Email" 
						type="email"
						autocomplete="off"
						readonly
						value="<%= email %>"
						/>
					
					<p>
						The reset code has been
						sent to the above email.
					</p>
					
					<input name="activation-code"
						placeholder="Reset Code"
						type="number"
						autofocus
						autocomplete="off"
						required
						/>
					
					<input name="password" 
						type="password"
						placeholder="New Password"
						autocomplete="off"
						required
						/>
					
					<input name="confirm"
						type="password"
						placeholder="Confirm Password"
						autocomplete="off"
						required
						/>
					
					<button>Change Password</button>
					<%= message %>
				</form>
			</section>
		</main>
		
		<%@include file="/WEB-INF/footer.jsp" %>
	</body>
</html>



