<#import "/common/form.ftlh" as f>

<@layout.main title="Se connecter">
	<h1>Se connecter</h1>
	
	<p>Pas encore de compte ? <a href="/signup">Inscrivez-vous</a> !</p>
	<#if loginBlocked>
		<p class="input-error" role="alert">Trop de tentatives de connexion. Réessayez dans 15 minutes.</p>
	<#elseif loginError>
		<p class="input-error" role="alert">Nom d'utilisateur ou mot de passe incorrect.</p>
	</#if>
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