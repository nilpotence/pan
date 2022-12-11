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
		<canvas
			style="background-image: url('/img/pan_web.jpg'); background-size: cover; width: 100%; aspect-ratio: 16/9; user-select: none;"
			width=160
			height=90>
		</canvas>
		<@f.hiddenInput path="boulder.holds" id="holdsInput" />
		</@f.inputWrapper>
		<button type="submit">${actionLabel}</button>
	</@f.form>
	<script type="text/javascript" src="/bundle/boulders/new.js"></script>
</#macro>