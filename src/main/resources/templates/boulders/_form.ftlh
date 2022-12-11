<#import "/common/form.ftlh" as f>

<#macro editBoulder action method actionLabel>
	<@f.form path="boulder" action=action method=method>
		<@f.input path="boulder.name" label="" type="text" placeholder="Nom du bloc" /> 
		<#assign grades = [] />
		<#list 3..8 as gradeNum>
			<#list ['a', 'b', 'c'] as gradeLetter>
				<#list ['', '+'] as gradeSign>
					<#assign grades = grades + [gradeNum + gradeLetter + gradeSign] />
				</#list>
			</#list>
		</#list>
		<@f.select path="boulder.estimatedGrade" options=grades placeholder="Cotation estimée" label="" required="true" />
		<@f.inputWrapper path="boulder.holds">
		<svg 
			<@f.id />
			<@f.ariaInvalid />
			viewBox="0 0 160 90"
			style="width: 100%; aspect-ratio: 16/9; user-select: none; background-image: url('/img/pan_web_dark.jpg'); background-size: cover;"
			>
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
					xlink:href="/img/pan_web.jpg"
					x="0"
					y="0"
					width="160"
					height="90"
					clip-path="url(#holdsClipPath)"
				/>
			</g>
		</svg>
		<@f.hiddenInput path="boulder.holds" id="holdsInput" />
		</@f.inputWrapper>
		<button type="submit">${actionLabel}</button>
	</@f.form>
	<script type="text/javascript" src="/bundle/boulders/new.js"></script>
</#macro>