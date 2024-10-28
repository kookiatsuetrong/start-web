<!DOCTYPE html>
<html>
	<head>
		<title>Log In</title>
		<link rel="stylesheet" href="/normalize.css" />
		<link rel="stylesheet" href="/main.css" />
	</head>
	<body>
		<main>
			<section class="container">
				<form method="post">
					<h3>Log In</h3>
					<input name="email" 
						   placeholder="Your Email" 
						   autofocus 
						   autocomplete="off"
						   />
					<input name="password" 
						   type="password"
						   placeholder="Your Password"
						   />
					<button>Log In</button>
				</form>
			</section>
		</main>
		<footer></footer>
	</body>
	<style>
		form {
			max-width: 320px;
			margin: 0 auto;
		}
		form > h3 {
			margin-bottom: 1rem;
		}
		form > input {
			margin-bottom: .5rem;
		}
	</style>
</html>
