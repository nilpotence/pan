<#import '/common/auth.ftlh' as auth>
<#import '/common/form.ftlh' as f>

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
	<div style="padding-left: 10px;display: flex; justify-content: end;">
		<#if photos.getNumber() gt 0>
			<a 
				role="button"
				style="padding: 0;"
				class="pan-icon-button"
				href="?${show(photos.getPageable().withPage(photos.getNumber() - 1))}">
				<span class="gg-arrow-left"></span>							
			</a>
		</#if>
		<#if photos.getNumber() < (photos.getTotalPages() - 1)>
			<a
				role="button"
				style="padding: 0;"
				class="pan-icon-button"
				href="?${show(photos.getPageable().withPage(photos.getNumber() + 1))}">
				<span class="gg-arrow-right"></span>							
			</a>
		</#if>
	</div>
</@layout.main>