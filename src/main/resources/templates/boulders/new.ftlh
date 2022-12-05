<@layout.main title="Créer un nouveau bloc">
	<h1>Créer un nouveau bloc</h1>
	<form action="/boulders/new" method="POST">
		<label for="boulderName">
			<input type="text" id="boulderName" name="name" placeholder="Nom du bloc" required />
		</label>
		<select name="estimatedGrade">
			<option value="" disabled selected>Cotation estimée</option>
			<#list 3..8 as gradeNum>
				<#list ['a', 'b', 'c'] as gradeLetter>
					<#list ['', '+'] as gradeSign>
						<option value="${gradeNum}${gradeLetter}${gradeSign}">${gradeNum}${gradeLetter}${gradeSign}</option>
					</#list>
				</#list>
			</#list>
		</select>
		<svg 
			viewBox="0 0 160 90"
			style="width: 100%; aspect-ratio: 16/9; user-select: none;">
			<defs>
				<g id="hold">
					<circle x=0 y=0 r=1 fill=none stroke-width=0.1 stroke=red></circle>
					<circle x=0 y=0 r=3 fill="rgba(255,255,255,0)"></circle>
				</g>
				<circle id="holdClipPath" x=0 y=0 r=3></circle>
			</defs>
			<g id="scene">
				<clipPath id="holdsClipPath">
				</clipPath>
				<image
					xlink:href="/img/pan_web_dark.jpg"
					x="0"
					y="0"
					width="160"
					height="90"
				/>
				<image
					xlink:href="/img/pan_web.jpg"
					x="0"
					y="0"
					width="160"
					height="90"
					clip-path="url(#holdsClipPath)"
				/>
			</g>
		</svg>
		<input type="hidden" id="holdsInput" name="holds" />
		<button type="submit">Créer</button>
	</form>
	<script type="text/javascript" src="/bundle/boulders/new.js"></script>
</@layout.main>