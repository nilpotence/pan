<#import '_form.ftlh' as f />

<@layout.main title="Créer un nouveau bloc">
	<h1>Créer un nouveau bloc</h1>
	<@f.editBoulder action="/boulders/new" method="POST" actionLabel="Créer" />
</@layout.main>