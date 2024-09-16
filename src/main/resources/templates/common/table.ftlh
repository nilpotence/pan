
<#macro pagination page>
	<div style="padding-left: 10px;display: flex; justify-content: end;">
		<#if page.getNumber() gt 0>
			<a 
				role="button"
				style="padding: 0;"
				class="pan-icon-button"
				href="?${show(page.getPageable().withPage(page.getNumber() - 1))}">
				<span class="gg-arrow-left"></span>							
			</a>
		</#if>
		<#if page.getNumber() < (page.getTotalPages() - 1)>
			<a
				role="button"
				style="padding: 0;"
				class="pan-icon-button"
				href="?${show(page.getPageable().withPage(page.getNumber() + 1))}">
				<span class="gg-arrow-right"></span>							
			</a>
		</#if>
	</div>
</#macro>