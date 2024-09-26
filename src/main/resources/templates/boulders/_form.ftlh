<#import "/common/form.ftlh" as f>

<#macro editBoulder action method actionLabel>
	<@f.form path="boulder" action=action method=method>
		<@f.input path="boulder.name" label="" type="text" placeholder="Nom du bloc" /> 
		<@f.input path="boulder.comment" label="" type="text" placeholder="Commentaire" /> 
		<#assign grades = [] />
		<#list 3..8 as gradeNum>
			<#list ['a', 'b', 'c'] as gradeLetter>
				<#list ['', '+'] as gradeSign>
					<#assign grades = grades + [gradeNum + gradeLetter + gradeSign] />
				</#list>
			</#list>
		</#list>
		<@f.select path="boulder.estimatedGrade" options=grades placeholder="Cotation estimée" label="" required="true" />
		<button id="customPhotoBtn" type="button">Prendre une photo personnalisée</button>
		<input style="display: none;" id="customPhotoFileInput" type="file" accept="image/*" capture="camera" />
		<@f.inputWrapper path="boulder.customPhoto">
			<@f.hiddenInput path="boulder.customPhoto" id="customPhotoInput" />
		</@f.inputWrapper>
		<@f.inputWrapper path="boulder.holds">
			<canvas
				id="boulderCanvas"
				style="background-image: url('${defaultPhoto.data}'); background-size: cover; width: 100%; user-select: none;"
				width="${defaultPhoto.width?c}"
				height="${defaultPhoto.height?c}">
			</canvas>
			<@f.hiddenInput path="boulder.holds" id="holdsInput" />
		</@f.inputWrapper>
		<button id="boulderSubmitBtn" type="submit">${actionLabel}</button>
	</@f.form>
	<script type="text/javascript" src="/bundle/boulders/new.js"></script>
</#macro>