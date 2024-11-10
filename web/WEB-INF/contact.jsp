<!DOCTYPE html>
<html>
	<head>
		<title>Contact</title>
		<link rel="stylesheet" href="/normalize.css" />
		<link rel="stylesheet" href="/main.css" />
	</head>
	<body>
		<header></header>
		<main>
			<section class="container">
				<%
				String message = (String)session.getAttribute("message");
				if (message == null) message = "";
				session.removeAttribute("message");

				String photoCode = (String)session.getAttribute("photo-code");
				if (photoCode == null) photoCode = "";
				session.removeAttribute("photo-code");

				String email = (String)session.getAttribute("email");
				if (email == null) email = "";
				session.removeAttribute("email");
				%>
				<form class="user-form" method="post"
					enctype="multipart/form-data">
					<h3>Contact</h3>
					<input name="topic"
						placeholder="Topic"
						required
						autofocus
						autocomplete="off"
						/>
					<textarea name="detail"
						placeholder="Detail"></textarea>
					
					<section class="file-upload">
						<svg class="file-upload-icon-photo"
							width="48" height="48"
							viewBox="0 0 24 24"
							fill="none" stroke="#aaa"
							stroke-width="2"
							stroke-linecap="round"
							stroke-linejoin="round">
							<g transform="translate(2 3)">
								<path d="M20 16a2 2 0 0 1-2
									2H2a2 2 0 0 1-2-2V5c0-1.1.9-2
									2-2h3l2-3h6l2 3h3a2 2 0 0 1
									2 2v11z" />
								<circle cx="10" cy="10" r="4" />
							</g>
						</svg>
						<svg class="file-upload-icon-photo"
							width="48" height="48"
							viewBox="0 0 24 24"
							fill="none" stroke="#aaa"
							stroke-width="2"
							stroke-linecap="round"
							stroke-linejoin="round">
							<rect x="3" y="3" width="18" height="18" rx="2" />
							<circle cx="8.5" cy="8.5" r="1.5" />
							<path d="M20.4 14.5L16 10 4 20" />
						</svg>
						<svg class="file-upload-icon-photo"
							width="48" height="48" 
							viewBox="0 0 24 24" 
							fill="none" stroke="#aaa" 
							stroke-width="2" 
							stroke-linecap="round" 
							stroke-linejoin="round">
							<path d="M14 2H6a2 2 0 0
								0-2 2v16c0 1.1.9 2 2
								2h12a2 2 0 0 0
								2-2V8l-6-6z"/>
							<path d="M14 3v5h5M16 13H8M16
									17H8M10 9H8"/>
						</svg>
						<svg class="file-upload-icon-ready"
							width="48" height="48"
							viewBox="0 0 24 24"
							fill="none" stroke="#aaa"
							stroke-width="2"
							stroke-linecap="round"
							stroke-linejoin="round">
							<polyline points="9 11 12 14 22 4" />
							<path d="M21 12v7a2 2 0 0 1-2
								2H5a2 2 0 0 1-2-2V5a2 2 0
								0 1 2-2h11" />
						</svg>
					</section>

					<input name="email" 
						placeholder="Your Email" 
						type="email"
						required
						value="<%= email %>"
						autocomplete="off"
						/>
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
					<button>Send</button>
					<%= message %>
				</form>
			</section>
				
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

			<style>
				.file-upload {
					padding: 1rem;
					display: block;
					margin-bottom: 1rem;
					background: #f2fff2;
					border: .15rem dashed #bbb;
					transition: border .1s linear;
					height: 3rem;
					text-align: center;
				}
				.file-upload.file-upload-active {
					border: .15rem dashed #666;
					background: #eee;
				}
				.file-upload-icon-photo {
					display: inline;
				}
				.file-upload-active .file-upload-icon-photo {
					stroke: #666;
				}
				.file-upload-icon-ready {
					display: none;
				}
			</style>

			<script>
			setup()

			function setup() {
				var target = document.querySelector(".file-upload")
				target.ondrop  = dropping
				target.onclick = choosing

				target.ondragover = function (event) {
					event.preventDefault()
					event.target.classList.add("file-upload-active")
				}
				target.ondragleave = function (event) {
					event.target.classList.remove("file-upload-active")
				}
				target.ondragend = function (event) {
					event.target.classList.remove("file-upload-active")
				}
			}

			function dropping(event) {
				event.preventDefault()
				for (var f of event.dataTransfer.files) {
					upload(f)
				}
				document.querySelectorAll(".file-upload-icon-photo")
						.forEach( e => e.style.display = "none" )
				document.querySelector(".file-upload-icon-ready")
						.style.display = "inline"
				event.target.classList.remove("file-upload-active")
			}

			function choosing() {
				var chooser = document.createElement("input")
				chooser.type = "file"
				chooser.onchange = event => {
					for (var f of event.target.files) {
						upload(f)
					}
					document.querySelectorAll(".file-upload-icon-photo")
							.forEach( e => e.style.display = "none" )
					document.querySelector(".file-upload-icon-ready")
							.style.display = "inline"
				}
				chooser.click()
			}
			</script>
		</main>
		
		
		<%@include file="/WEB-INF/footer.jsp" %>
	</body>
</html>

