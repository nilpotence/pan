<#import "/common/form.ftlh" as f />

<#macro main title="Pan">
<!doctype html>
<html lang="en">
 	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" type="text/css" media="all" href="<@spring.url '/bundle/index.css' />" />
		<link rel="stylesheet" href="/icons/css/trash.css" />
		<link rel="stylesheet" href="/icons/css/pen.css" />
		<link rel="stylesheet" href="/icons/css/arrow-left.css" />
		<link rel="stylesheet" href="/icons/css/arrow-right.css" />
		<script type="application/javascript" src="<@spring.url '/bundle/index.js' />"></script>
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
						Votre score est de ${currentUser.score.score} !
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