<style>
	body {
		font-size: 14px;
	}
	.title {
		font-size: 18px;
		font-weight: bold;
		text-align: center;
	}
	.msg {
	}
	.signature {
		font-size: 14px;
		font-weight: bold;
		padding-left: 80%;
	}
	table {
		width: 500px;	
		border: 1px dashed #515151;
		border-spacing: 0;
		border-collapse: collapse;
		margin-top: 10px;
	}
	th, td {
		text-align: center;
		border: 1px dashed #515151;
	}
</style>
<body>
<div>
<p class="title">
	Job Success
</p>
<p class="msg">
	Job [${job.jobKey.name}, ${job.jobKey.group}] runs successfully within the schedule at ${startTime?datetime}.
	<table>
		<tr>
		<th>Start Time</th>
		<th>End Time</th>
		<th>Time Cost</th>
		</tr>
		<tr>
		<td>${startTime?datetime}</td>
		<td>
		<#if endTime??>
			${endTime?datetime}
		<#else>
			--
		</#if>
		</td>
		<td>${timeCost}</td>
		</tr>
	</table>
</p>
<p class="signature">
	midas data team
</p>
</div>
</body>