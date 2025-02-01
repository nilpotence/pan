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
            <#assign sortOptions=[
                {'name': ['createdAt,DESC'], 'label': 'Date de création'}, 
                {'name': ['estimatedGrade,ASC', 'name,ASC'], 'label': 'Cotation'},
                {'name': ['score.score,DESC','name,ASC'], 'label': 'Score'}
            ]>
			<#list sortOptions as sort>
				<option ${currentSort?seq_contains(sort.name?first)?then('selected','')} value="${sort.name?join('&sort=')}">${sort.label}</option>
			</#list>
		</select>
	</div>
	
	<table>
		<thead>
			<tr>
				<th>Bloc</th>
                <#if currentUser??>
                    <th style="width: 1%; padding-left: 3px; padding-right: 3px;">&#10003;</th>
                    <th style="width: 1%;">score</th>
                </#if>
				<th style="width: 1%;">action</th>
			</tr>
		</thead>
		<tbody>
			<#list boulders.iterator() as b>
				
				<#if currentUser?? && currentUser.role == 'ADMIN' && b.obsoleteCount gt 0>
					<tr style="background: rgba(255, 0, 0, 0.2);">
				<#else>
					<tr>
				</#if>
					<td>
                        <strong>${b.name}</strong> - 
                        <i>${b.estimatedGrade}</i>
                    </td>
                    <#if currentUser??>
                        <td style="padding-left: 3px; padding-right: 3px;"><#if b.ticked>&#10003;</#if></td>
                        <td style="font-size: 0.8em;">${b.score}</td>
                    </#if>
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