<#import '_form.ftlh' as f>

<@layout.main title="Créer un pan">
	<h1>Créer un pan</h1>
	<@f.editPan action="/walls/new" method="POST" actionLabel="Créer" />
</@layout.main>