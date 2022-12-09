<#import "/common/form.ftlh" as f>

<@layout.main title="Se connecter">
	<h1>Se connecter</h1>
	<@f.form path="user" action="/signin" method="POST">
		<@f.input path="user.username" placeholder="Nom d'utilisateur" />
		<@f.input path="user.password" type="password" placeholder="Mot de passe" />			
		
		<button type="submit">Se connecter</button>
	</@f.form>
</@layout.main>