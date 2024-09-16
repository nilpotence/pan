<#import '/common/auth.ftlh' as auth />
<#import '/common/form.ftlh' as f />

<@layout.main title=boulder.name>
	<h1 style="display: flex; justify-content: space-between; align-items: center;">
		${boulder.name} - ${boulder.estimatedGrade} <#if tick?? && tick.appUser??>&#10003;</#if>
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
	
	<#if tick??>
		<#if tick.appUser??>
			<div>
				<p>
					enchainé le 
					<strong>${tick.createdAt.format('dd/MM/YYYY')}</strong>
					<#if tick.comment?has_content>
					 	: <span style="font-style: italic;">&laquo;${tick.comment}&raquo;</span>
					</#if>	
					<#if tick.estimatedGrade?has_content>
						(${tick.estimatedGrade})
					</#if>
					 - <a href="#" class="edit-tick-action">modifier...</a>
				</p>
			</div>
		<#else>
			<p><a href="#" class="edit-tick-action">j'ai enchainé...</a></p>
		</#if>
	</#if>
	
	<svg 
		viewBox="${bbox.x?floor?c} ${bbox.y?floor?c} ${bbox.width?floor?c} ${bbox.height?floor?c}"
		style="
			width: 100%;
			aspect-ratio: ${bbox.width?floor?c}/${bbox.height?floor?c};
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
			xlink:href="${boulder.darkCustomPhoto}"
			x=0
			y=0
			width=${boulder.customPhotoWidth?c}
			height=${boulder.customPhotoHeight?c}	
		/>
		<image
			xlink:href="${boulder.customPhoto}"
			x="0"
			y="0"
			width="${boulder.customPhotoWidth?c}"
			height="${boulder.customPhotoHeight?c}"
			clip-path="url(#holdsClipPath)"
		/>
		<#list bbox.holds as h>
			<use class="hold" xlink:href="#hold" x=${h.x?c} y=${h.y?c}></use>
		</#list>
	</svg>	
	
	<div id="ticklist" style="margin-top: 40px;">
		<h3>&#128170; répetitions &#128170;</h3>
		
			
		<table>
			<thead>
				<tr>
					<th>grimpeur.euse</th>
					<th>commentaire</th>
					<th>date</th>
				</tr>
			</thead>
			<tbody>
				<#list ticks.iterator() as t>
					<tr>
						<td>${t.appUser.username}</td>
						<td>${t.comment}</td>
						<td>${t.createdAt.format('dd/MM/YYYY')}</td>
					</tr>	
				</#list>	
			</tbody>
		</table>
	</div>
	
	<dialog id="tickDialog">
		<article>
			<h3>Détail de l'enchainement</h3>
			<@f.form path="tick" action="/boulders/${boulder.id}/tick" method="POST">
				<div class="pan-input">
					<label>
						<input name="ticked" type="checkbox" <#if tick?? && tick.appUser??>checked</#if> />
						J'ai enchainé
					</label>
				</div>
				<@f.input path="tick.comment" placeholder="commentaire" /><#assign grades = [] />
				<#list 3..8 as gradeNum>
					<#list ['a', 'b', 'c'] as gradeLetter>
						<#list ['', '+'] as gradeSign>
							<#assign grades = grades + [gradeNum + gradeLetter + gradeSign] />
						</#list>
					</#list>
				</#list>
				<@f.select 
					path="tick.estimatedGrade"
					options=grades
					placeholder="Cotation estimée" 
					label="" />
			</@f.form>
			<footer>
				<a href="#" id="cancelTickAction" role="button" class="secondary">Annuler</a>
				<a href="#" id="confirmTickAction" role="button">Confirmer</a>
			</footer>
		</article>
	</dialog>
	
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