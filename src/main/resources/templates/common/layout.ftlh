<#import "/common/form.ftlh" as f />

<#macro main title="Pan">
<!doctype html>
<html lang="en">
 	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<#-- No favicon: without this the browser asks for /favicon.ico on every page, which redirects to /signin (2 requests + a page render). -->
		<link rel="icon" href="data:,">
		<link rel="stylesheet" type="text/css" media="all" href="<@spring.url '/bundle/index.css' />" />
		<script defer src="<@spring.url '/bundle/index.js' />"></script>
		<title>${title}</title>
	</head>
	<body>
		<main class="container">
			<nav>
				<ul>
					<li><a href="<@spring.url '/boulders' />">blocs</a></li>
					<#if currentUser??>
						<li><a href="<@spring.url '/scores' />">classement</a></li>
					</#if>
				</ul>
				<ul>
					<#if currentUser??>
						<li><a href="#" id="userDialogOpenBtn" class="outline secondary">${currentUser.username}</a></li>
					<#else>
						<li><a href="/signin">Se connecter</a></li>
					</#if>
				</ul>
			</nav>
			<#nested />
		</main>
		<#if currentUser??>
			<dialog id="userDialog">
				<article>
					<h3>${currentUser.username}</h3>
					<p>
						Votre score est de ${currentUser.score.score}, 
						Vous êtes <#if currentUser.score.rank == 1>1er.ère<#else>${currentUser.score.rank}ème</#if> !
					</p>
					<footer>
						<a href="#" id="userDialogBackBtn" role="button" class="secondary">Retour</a>
						<a href="#" role="button" id="logoutBtn">Se deconnecter</a>
						<form style="display: none;" id="logoutForm" action="/signout" method="POST">
							<@f.csrf />
							<button type="submit">se deconnecter</button>
						</form>
					</footer>
				</article>
			</dialog>
		</#if>
	</body>
</html>
</#macro>