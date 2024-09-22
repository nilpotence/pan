<#import '/common/auth.ftlh' as auth>
<#import '/common/form.ftlh' as f>
<#import '/common/table.ftlh' as t>

<@layout.main title="Le classement">
	<h1>
		Le classement
	</h1>
	
	<table>
		<thead>
			<tr>
				<th>Grimpeur.euse</th>
				<th>score</th>
				<th>rang</th>
			</tr>
		</thead>
		<tbody>
			<#list scores.iterator() as score>
				<tr>
					<td>${score.user.username}</td>
					<td>${score.score}</td>
					<td>${(rankOffset + score?index + 1)}</td>
				</tr>
			</#list>
		</tbody>
	</table>
	<@t.pagination scores />
		
</@layout.main>
	