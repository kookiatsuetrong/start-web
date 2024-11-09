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
						type="email"
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
					<span class="trio">
						<input name="code" 
							placeholder="4-Digit Code"
							type="number"
							required 
							autocomplete="off"
							/>
						
						<svg width="24" height="24" 
							viewBox="0 0 24 24" fill="none" 
							stroke="#888" stroke-width="2" 
							stroke-linecap="round" 
							stroke-linejoin="round">
							<path d="M19 12H6M12 5l-7 7 7 7"/>
						</svg>
						
						<img src="data:image/png;base64, 
							<%= photoCode %>" />
					</span>
					<button>Get Reset Code</button>
					<%= message %>
				</form>
			</section>
		</main>
		
		<style>
			.trio {
				display: grid;
				grid-template-columns: 1fr 1.5rem 4rem;
				column-gap: .5rem;
			}
			.trio svg {
				margin-top: .4rem;
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
