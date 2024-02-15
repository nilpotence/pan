<#import '/common/auth.ftlh' as auth>

<@layout.main title="Les pans">
	<h1 style="display: flex; justify-content: space-between; align-items: center;">
		Les pans
		<a href="/walls/new" role="button" title="Créer un nouveau pan">Créer</a>
	</h1>

	<table>
		<thead>
			<tr>
				<th>Nom</th>
				<th style="width: 1%;">action</th>
			</tr>
		</thead>
		<tbody>
			<#list walls as w>
				<tr>
					<td>${w.name}</td>
					<td>
						<a href="walls/${w.id}">Voir</a>
					</td>
				</tr>	
			</#list>
		</tbody>
	</table>

</@layout.main>