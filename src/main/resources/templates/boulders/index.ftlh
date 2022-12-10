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
				<th>Bloc</th>
				<th style="width: 1%;">action</th>
			</tr>
		</thead>
		<tbody>
			<#list boulders as b>
				<tr>
					<td>${b.name} - ${b.estimatedGrade}</td>
					<td>
						<a href="boulders/${b.id}">Voir</a>
					</td>
				</tr>	
			</#list>
		</tbody>
	</table>
</@layout.main>