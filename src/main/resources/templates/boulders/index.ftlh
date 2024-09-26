<#import '/common/auth.ftlh' as auth>
<#import '/common/form.ftlh' as f>
<#import '/common/table.ftlh' as t>

<@layout.main title="Les blocs">
	<h1 style="display: flex; justify-content: space-between; align-items: center;">
		Les blocs 
		<a href="/boulders/new" role="button" title="Créer un nouveau bloc">Créer</a>
	</h1>
	
	<div style="display: flex; flex-direction: column;">
		<label for="sortSelect">Trier par : </label>
		<select id="sortSelect">
			<#list [{'name': 'createdAt,DESC', 'label': 'Date de création'}, {'name': 'estimatedGrade,ASC', 'label': 'Cotation'}] as sort>
				<option ${currentSort?seq_contains(sort.name)?then('selected','')} value="${sort.name}">${sort.label}</option>
			</#list>
		</select>
	</div>
	
	<table>
		<thead>
			<tr>
				<th>Bloc</th>
				<th style="width: 1%;">action</th>
			</tr>
		</thead>
		<tbody>
			<#list boulders.iterator() as b>
				<tr <#if currentUser?? && currentUser.role == 'ADMIN' && b.obsoleteCount gt 0>style="background: rgba(255, 0, 0, 0.2);"</#if>>
					<td><strong>${b.name}</strong> - <i>${b.estimatedGrade}</i></td>
					<td>
						<a href="boulders/${b.id}">Voir</a>
					</td>
				</tr>	
			</#list>
			
			
		</tbody>
	</table>
	
	<@t.pagination boulders />
	
	<script type="text/javascript">
		const select = document.querySelector("#sortSelect")
		select.addEventListener('change', evt => window.location = "?sort=" + evt.target.value)
	</script>
</@layout.main>