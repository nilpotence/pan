<#import '/common/auth.ftlh' as auth />
<#import '/common/form.ftlh' as f />

<@layout.main title=boulder.name>
	<h1 style="display: flex; justify-content: space-between; align-items: center;">
		${boulder.name} - ${boulder.estimatedGrade} 
	
		<div style="display: flex;">
			<@auth.ifCanUpdate boulder>
				<a 
					href="#"
					role="button"
					class="delete-boulder-action pan-icon-button secondary"
					title="Supprimer">
					<span class="gg-trash"></span>	
				</a>
				<a 
					href="/boulders/${boulder.id}/edit"
					role="button"
					title="Modifier"
					class="pan-icon-button">
					<span class="gg-pen"></span>
				</a>
			</@auth.ifCanUpdate>
		</div>
	</h1>

	<svg 
		viewBox="${bbox.x?floor} ${bbox.y?floor} ${bbox.width?floor} ${bbox.height?floor}"
		style="
			width: 100%;
			aspect-ratio: ${bbox.width?floor}/${bbox.height?floor};
			user-select: none;
			border: 1px solid var(--form-element-border-color);
			border-radius: var(--border-radius);"
	>
			
		<defs>
			<g id="hold">
				<circle x=0 y=0 r=10 fill=none stroke-width=1 stroke=red></circle>
			</g>
			<circle id="holdClipPath" x=0 y=0 r=30></circle>
		</defs>

		<clipPath id="holdsClipPath">
			<#list bbox.holds as h>
				<use  xlink:href="#holdClipPath" x=${h.x?c} y=${h.y?c}></use>
			</#list>
		</clipPath>
		<image
			xlink:href="/img/pan_web_dark.jpg"
			x=0
			y=0
			width=1600
			height=900	
		/>
		<image
			xlink:href="/img/pan_web.jpg"
			x="0"
			y="0"
			width="1600"
			height="900"
			clip-path="url(#holdsClipPath)"
		/>
		<#list bbox.holds as h>
			<use class="hold" xlink:href="#hold" x=${h.x?c} y=${h.y?c}></use>
		</#list>
	</svg>	
	<dialog id="confirmDeleteDialog">
		<article>
			<h3>Supprimer le bloc ${boulder.name}</h3>
			<p>
				Êtes vous sur de vouloir supprimer le bloc ${boulder.name} ?
				Attention, Le bloc ne pourra pas être restoré.
			</p>
			<footer>
				<a href="#" id="cancelDeleteAction" role="button" class="secondary">Annuler</a>
				<a href="#" id="confirmDeleteAction" role="button">Confirmer</a>
			</footer>
		</article>
		<form id="deleteForm" action="/boulders/${boulder.id}" method="POST">
			<@f.csrf />
			<input type="hidden" name="_method" value="DELETE" />
		</form>
	</dialog>
	<script type="text/javascript" src="/bundle/boulders/show.js"></script>
</@layout.main>