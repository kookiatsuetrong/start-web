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
						type="email"
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
					<span class="trio">
						<input name="code" 
							placeholder="4-Digit Code" 
							required
							type="number"
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

					<button>Continue</button>
					<%
					out.print(message); 
					session.removeAttribute("message");
					%>
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

