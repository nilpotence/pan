<@layout.main title=boulder.name>
	<h1>${boulder.name} - ${boulder.estimatedGrade}</h1>

	<svg 
		viewBox="${bbox.x?floor} ${bbox.y?floor} ${bbox.width?floor} ${bbox.height?floor}"
		style="width: 100%; aspect-ratio: ${bbox.width?floor}/${bbox.height?floor}; user-select: none;"
	>
			
		<defs>
			<g id="hold">
				<circle x=0 y=0 r=1 fill=none stroke-width=0.1 stroke=red></circle>
			</g>
			<circle id="holdClipPath" x=0 y=0 r=3></circle>
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
			width=160
			height=90	
		/>
		<image
			xlink:href="/img/pan_web.jpg"
			x="0"
			y="0"
			width="160"
			height="90"
			clip-path="url(#holdsClipPath)"
		/>
		<#list bbox.holds as h>
			<use class="hold" xlink:href="#hold" x=${h.x?c} y=${h.y?c}></use>
		</#list>
	</svg>	
	<script>
		const svg = document.querySelector('svg')
		
		svg.addEventListener('click', evt => {
			const pt = svg.createSVGPoint()
			pt.x = evt.clientX
			pt.y = evt.clientY
			
			console.log(pt.matrixTransform(svg.getScreenCTM().inverse()))
		})
	</script>
</@layout.main>