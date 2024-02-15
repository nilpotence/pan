<#import "/common/form.ftlh" as f>

<#macro editPan action method actionLabel>
	<@f.form path="wall" action=action method=method enctype="multipart/form-data">
		<@f.input path="wall.name" type="text" placeholder="Nom du mur" />
	
		<@f.fileInput path="wall.id" label="Photo du mur" name="image" capture="user" accept="image/*" />
		<button type="submit">${actionLabel}</button>
	</@f.form>
</#macro>