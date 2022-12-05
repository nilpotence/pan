<@layout.main title="Les blocs">
	<h1>Les blocs</h1>
	
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