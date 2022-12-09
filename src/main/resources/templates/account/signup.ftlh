<#import "/common/form.ftlh" as f>

<@layout.main title="Se connecter">
	<h1>Créer un compte</h1>
	<@f.form path="user" action="/signup" method="POST">
		<@f.input path="user.username" placeholder="Nom d'utilisateur" />
		<@f.input path="user.password" type="password" placeholder="Mot de passe" />			
		<@f.input path="user.passwordConfirmation" type="password" placeholder="Confirmer le mot de passe" />			
		
		<button type="submit">Créer un compte</button>
	</@f.form>
</@layout.main>