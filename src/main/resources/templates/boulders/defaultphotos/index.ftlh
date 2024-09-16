<#import '/common/auth.ftlh' as auth>
<#import '/common/form.ftlh' as f>
<#import '/common/table.ftlh' as t>

<@layout.main title="Photos par defaut">
	<h1 style="display: flex; justify-content: space-between; align-items: center;">
		Photos par defaut
		<a href="/default-photos/new" role="button" title="Créer">Créer</a>
	</h1>
	
	<table>
		<thead>
			<tr>
				<th>Photos</th>
				<th style="width: 1%;">action</th>
			</tr>
		</thead>
		<tbody>
			<#list photos.iterator() as p>
				<tr>
					<td>le ${p.createdAt} (par ${p.createdBy.username})</td>
					<td>
						<a href="/default-photos/${p.id}">Voir</a>
					</td>
				</tr>	
			</#list>
			
			
		</tbody>
	</table>
	
	<@t.pagination photos />

</@layout.main>