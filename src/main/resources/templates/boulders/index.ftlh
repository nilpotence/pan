<@layout.main title="Les blocs">
	<h1 style="display: flex; justify-content: space-between; align-items: center;">
		Les blocs 
		<a href="/boulders/new" role="button">Créer</a>
	</h1>
	
	<table>
		<thead>
			<tr>
				<th>Nom du bloc</th>
				<th>Cotation estimée</th>
				<th>action</th>
			</tr>
		</thead>
		<tbody>
			<#list boulders as b>
				<tr>
					<td>${b.name}</td>
					<td>${b.estimatedGrade}</td>
					<td><a href="boulders/${b.id}">Voir</a></td>
				</tr>	
			</#list>
		</tbody>
	</table>
</@layout.main>