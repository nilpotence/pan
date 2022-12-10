<#import '/common/auth.ftlh' as auth>
<#import '/common/form.ftlh' as f>

<@layout.main title="Les blocs">
	<h1 style="display: flex; justify-content: space-between; align-items: center;">
		Les blocs 
		<a href="/boulders/new" role="button" title="Créer un nouveau bloc">Créer</a>
	</h1>
	
	<table>
		<thead>
			<tr>
				<th>Nom du bloc</th>
				<th>Cotation estimée</th>
				<th>Ouvreur</th>
				<th colspan="3" style="width: 1%;">action</th>
			</tr>
		</thead>
		<tbody>
			<#list boulders as b>
				<tr>
					<td>${b.name}</td>
					<td>${b.estimatedGrade}</td>
					<td>${b.createdBy.username}</td>
					<td>
						<a href="boulders/${b.id}">Voir</a>
					</td>
					<@auth.ifCanUpdate b>
						<td>
							<a href="boulders/${b.id}/edit">Modifier</a>
						</td>
						<td>
							<a 
								href="#"
								class="delete-boulder-action"
								data-boulder-id=${b.id}
								data-boulder-name=${b.name}>
								Supprimer
							</a>
						</td>
					</@auth.ifCanUpdate>
				</tr>	
			</#list>
		</tbody>
	</table>
	<dialog id="confirmDeleteDialog">
		<article>
			<h3>Supprimer le bloc <span class="boulder-name"></span></h3>
			<p>
				Êtes vous sur de vouloir supprimer le bloc <span class="boulder-name"></span> ?
				Attention, Le bloc ne pourra pas être restoré.
			</p>
			<footer>
				<a href="#" id="cancelDeleteAction" role="button" class="secondary">Annuler</a>
				<a href="#" id="confirmDeleteAction" role="button">Confirmer</a>
			</footer>
		</article>
		<form id="deleteForm" action="" method="POST">
			<@f.csrf />
			<input type="hidden" name="_method" value="DELETE" />
		</form>
	</dialog>
	<script type="text/javascript" src="/bundle/boulders/index.js"></script>
</@layout.main>