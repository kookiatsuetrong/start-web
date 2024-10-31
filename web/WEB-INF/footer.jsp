
<footer>
	<section class="container">
		<p>
			<a href="/">Home</a>
			<a href="/">About Us</a>
			<a href="/">Contact</a>
		</p>

		<p>
			<a href="/user-check-email">Create Account</a>
			<a href="/user-check-email">Log In</a>
			<a href="/reset-password">Reset Password</a>
		</p>

		<p>
			The quick brown fox jumps over a lazy dog.
			The quick brown fox jumps over a lazy dog.
		</p>
	</section>
</footer>

<style>
	footer {
		
	}
	footer .container {
		display: grid;
	}
	@media (min-width: 640px) {
		footer .container {
			grid-template-columns: 1fr 1fr 1.5fr;
		}
	}
	footer a {
		margin: 0;
		display: block;
	}
	footer p {
		line-height: 2rem;
	}
</style>
