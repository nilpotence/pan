<#import '/common/auth.ftlh' as auth>
<#import '/common/form.ftlh' as f>

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
				<tr>
					<td>${b.name} - ${b.estimatedGrade}</td>
					<td>
						<a href="boulders/${b.id}">Voir</a>
					</td>
				</tr>	
			</#list>
			
			
		</tbody>
	</table>
	<div style="padding-left: 10px;display: flex; justify-content: end;">
		<#if boulders.getNumber() gt 0>
			<a 
				role="button"
				style="padding: 0;"
				class="pan-icon-button"
				href="?${show(boulders.getPageable().withPage(boulders.getNumber() - 1))}">
				<span class="gg-arrow-left"></span>							
			</a>
		</#if>
		<#if boulders.getNumber() < (boulders.getTotalPages() - 1)>
			<a
				role="button"
				style="padding: 0;"
				class="pan-icon-button"
				href="?${show(boulders.getPageable().withPage(boulders.getNumber() + 1))}">
				<span class="gg-arrow-right"></span>							
			</a>
		</#if>
	</div>
	
	<script type="text/javascript">
		const select = document.querySelector("#sortSelect")
		select.addEventListener('change', evt => window.location = "?sort=" + evt.target.value)
	</script>
</@layout.main>