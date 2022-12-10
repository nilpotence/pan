<#import '_form.ftlh' as f>

<@layout.main title="Editer le bloc ${boulder.name}">
	<h1>Modifier le bloc ${boulder.name}</h1>
	<@f.editBoulder action="/boulders/${boulder.id}/edit" method="PUT" actionLabel="Modifier" />
</@layout.main>