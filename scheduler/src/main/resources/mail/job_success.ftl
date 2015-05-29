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
		margin-left: 80%;
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
<div style="font-size: 14px;">
<div style="font-size: 16px; font-weight: bold; text-align: center;">
	Job Success
</div>
<div class="msg">
	<p style="margin-left: 10px;">Job [ ${job.jobKey.group}.${job.jobKey.name}] runs successfully within the schedule at ${startTime?datetime}.</p>
	<table style="margin-left: 10px; width: 500px; border: 1px dashed #515151; border-spacing: 0; border-collapse: collapse;">
		<tr>
		<th>Start Time</th>
		<th>End Time</th>
		<th>Time Cost</th>
		</tr>
		<tr>
		<td style="text-align: center;">${startTime?datetime}</td>
		<td style="text-align: center;">
		<#if endTime??>
			${endTime?datetime}
		<#else>
			--
		</#if>
		</td>
		<td style="text-align: center;">${timeCost}</td>
		</tr>
	</table>
</div>
<div>
	<p style="font-size: 14px; font-weight: bold; margin-left: 70%;">midas data team</p>
</div>
</div>
</body>