<#macro main title="Pan">
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" media="all" href="<@spring.url '/bundle/index.css' />" />
	<script type="application/javascript" src="<@spring.url '/bundle/index.js' />"></script>
    <title>${title}</title>
  </head>
  <body>
    <main class="container">
		  <nav>
			  <ul>
				  <li><strong>Pan</strong></li>
			  </ul>
			  <ul>
				  <li><a href="<@spring.url '/boulders' />">Les blocs</a></li>
				  <li><a href="<@spring.url '/boulders/new' />">Créer</a></li>
			  </ul>
		  </nav>
    	<#nested />
    </main>
  </body>
</html>
</#macro>