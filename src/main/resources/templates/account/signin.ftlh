<#import "/common/form.ftlh" as f>

<@layout.main title="Se connecter">
	<h1>Se connecter</h1>
	
	<p>Pas encore de compte ? <a href="/signup">Inscrivez-vous</a> !</p>
	<@f.form path="user" action="/signin" method="POST">
		<@f.input path="user.username" placeholder="Nom d'utilisateur" />
		<@f.input path="user.password" type="password" placeholder="Mot de passe" />			
		
		<label style="margin-bottom: var(--spacing);">
			<input type="checkbox" name="remember-me" />
			Se souvenir de moi
		</label>

		<button type="submit">Se connecter</button>
	</@f.form>
</@layout.main>