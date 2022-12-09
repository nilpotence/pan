<#macro ifCanUpdate entity>
	<#if currentUser?? && (currentUser.role == 'ADMIN' || currentUser.id == entity.createdBy.id)>
		<#nested />
	</#if>
</#macro>