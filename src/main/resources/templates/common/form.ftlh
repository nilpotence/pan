<#macro extraAttrs extras>
	<#if extras?is_hash>
		<#list extras as attrName, attrValue>${attrName}="${attrValue}"</#list> 
	</#if>
</#macro>
    	
<#macro id prefix="">
	<#if spring.status.expression??>
		<#assign stringId=prefix + spring.status.expression?replace('[','')?replace(']','') />
	<#else>
		<#assign stringId=prefix + spring.status.path />
	</#if>
	id=${stringId}
</#macro>

<#macro ariaInvalid>
	${(spring.status.errorMessages?size > 0)?then('aria-invalid=true aria-describedby=error_${stringId}', '')}
</#macro>

<#macro checkSelected value>
    <#if spring.stringStatusValue?is_number && spring.stringStatusValue == value?number>selected</#if>
    <#if spring.stringStatusValue?is_string && spring.stringStatusValue == value>selected</#if>
</#macro>

<#macro errorMessages extras...>
	<#if spring.status.errorMessages?size gt 0>
		<div class="pan-input--errors" <@id "error_" /> <@extraAttrs extras />>
			<#list spring.status.errorMessages as message>
				<span class="input-error">${message?no_esc}</span>
			</#list>
		</div>
	</#if>
</#macro>

<#macro inputWrapper path label="">
	<div class="pan-input">
		<@spring.bind path />
		<label>
			${label}
			<#nested />
			<@errorMessages />
		</label>
	</div>
</#macro>

<#macro csrf>
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</#macro>

<#macro form path method="POST" extras...>
	<@spring.bind path />
	<form <@id /> <@ariaInvalid /> method="${(method == 'GET')?then('GET','POST')}" <@extraAttrs extras />>
		<@csrf />
		 
		 <#if method != 'GET' && method != 'POST'>
		 	<input type="hidden" name="_method" value=${method}
		 </#if>

		 <div class="form-errors">
		 	<@errorMessages />
		 </div>
		 <#nested />
	</form>
</#macro>

<#macro input path label="" extras...>
	<@inputWrapper path label>
		<input 
			<@id />
			name="${spring.status.expression}"
			value="${spring.stringStatusValue}"
			<@ariaInvalid />
			<@extraAttrs extras />
		/>
	</@inputWrapper>
</#macro>

<#macro select path options placeholder="" label="" extras...>
	<@inputWrapper path label>
		<select
			<@id />
			name="${spring.status.expression}"
			<@ariaInvalid />
			<@extraAttrs extras />>
			
			<#if placeholder?has_content>
				<option value="dummy" disabled <@checkSelected "" />>${placeholder}</option>
			</#if>
			
			<#if options?is_hash>
				<#list options?keys as value>
					<option value="${value}"<@checkSelected value/>>${options[value]}</option>
				</#list>
			<#else> 
				<#list options as value>
					<option value="${value}"<@checkSelected value/>>${value}</option>
				</#list>
			</#if>
		</select>
	</@inputWrapper>
</#macro>

<#macro hiddenInput id path="" extras...>
	<#if path?has_content>
		<@spring.bind path />
	</#if>
	
	<input
		type="hidden"
		id="${id}"
		name="${spring.status.expression}"
		value="${spring.stringStatusValue}"
		<@extraAttrs extras /> />
		
</#macro>


<#macro fileInput path="" label="" extras...>
	<@inputWrapper path label>
		<input
			type="file"
			<@extraAttrs extras /> />
	</@inputWrapper>
</#macro>