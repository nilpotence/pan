<#import "/common/form.ftlh" as f>

<@layout.main title="Créer un nouveau bloc">
	<h1>Nouvelle photo par defaut</h1>
	
	<@f.form path="photo" action="/default-photos/new" method="POST">
		<input id="imageFileInput" type="file" accept="image/*" capture="camera" />

		<input type="hidden" id="imageData" name="data" value="" />
		
		<canvas></canvas>

		<button id="photoSubmitBtn" type="submit">Ajouter</button>
	</@f.form>
	
	<script src="<@spring.url '/bundle/boulders/defaultphotos/new.js' />"></script>
</@layout.main>