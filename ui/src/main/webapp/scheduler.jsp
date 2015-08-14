<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Scheduler</title>
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-theme.css" rel="stylesheet">
<link href="css/bootstrap-treeview.min.css" rel="stylesheet">
<link href="css/jquery.dataTables.css" rel="stylesheet">
<link href="css/scheduler-customized.css" rel="stylesheet">
<script src="js/jquery/jquery.min.js"></script>
<script src="js/jquery/jquery.widget.min.js"></script>
<script src="js/jquery/jquery.dataTables.min.js"></script>
<script src="js/bootstrap/bootstrap-treeview.min.js"></script>
<script src="js/jquery/jquery.form-validator.min.js"></script>
<script src="js/bootstrap/bootstrap.js"></script>
<script src="js/lib/base.js"></script>
<script src="js/lib/date.js"></script>
<script src="js/lib/editbar.js"></script>
<script src="js/lib/mapping.js"></script>
<script src="js/scheduler.js"></script>
</head>
<body>
	<div class="container">
		<nav class="navbar navbar-inverse">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#bs-example-navbar-collapse-9"
						aria-expanded="false">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="#">Brand</a>
				</div>

				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-9">
					<ul class="nav navbar-nav">
						<li class="active"><a href="/">首页</a></li>
						<li><a href="">调度审计</a></li>
						<li><a href="#">说明</a></li>
					</ul>

                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="#"><s:property value="#session['username']"/></a></li>
                    </ul>
				</div>
				<!-- /.navbar-collapse -->
			</div>
			<!-- /.container-fluid -->
		</nav>

		<div class="panel panel-primary">
			<div class="panel-heading">工作区</div>
			<div class="panel-content">
				<ul id="tabs" class="nav nav-tabs" role="tablist">
					<li role="presentation" class="active"><a href="#private"
						id="private-tab" role="tab" data-toggle="tab"
						aria-controls="private" aria-expanded="true">私有</a></li>
					<li role="presentation" class=""><a href="#public" role="tab"
						id="public-tab" data-toggle="tab" aria-controls="profile"
						aria-expanded="false">公共</a></li>
					<li role="presentation" class=""><a href="#notification" role="tab"
					id="public-tab" data-toggle="tab" aria-controls="profile"
					aria-expanded="false">通知</a></li>
				</ul>
				<div id="tabContent" class="tab-content">
					<div role="tabpanel" class="tab-pane fade active in" id="private"
						aria-labelledby="private-tab">
						<div class="panel panel-info">
							<div class="panel-heading">创建Job</div>
							<div class="panel-content">
								<form id="jobForm" class="form-horizontal" action="/proxy/scheduler/rest/createJob" method="post" onsubmit="return false;">
									<div class="form-group">
										<label for="jobName" class="col-sm-2 control-label">名称
											<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
										</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="jobName" name="jobName" data-mapping="name"
												data-validation="required" data-validation-error-msg="必填项"
												placeholder="">
										</div>
									</div>
									<div class="form-group">
										<label for="jobGroup" class="col-sm-2 control-label">组
											<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
										</label>
										<div class="col-sm-6">
											<select class="form-control" id="jobGroup" name="jobGroup" data-mapping="group"
												data-validation="required" data-validation-error-msg="必填项">
												<option value="">----</option>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label for="jobSchedule" class="col-sm-2 control-label">调度
											<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
										</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="jobSchedule" name="jobSchedule" data-mapping="schedule"
												data-validation="required" data-validation-error-msg="必填项"
												placeholder="">
										</div>
									</div>
									<div class="form-group">
										<label for="jobDescription" class="col-sm-2 control-label">描述
											<span class="glyphicon" aria-hidden="true"> </span>
										</label>
										<div class="col-sm-6">
											<textarea class="form-control" id="jobDescription" name="jobDescription" data-mapping="description"
												placeholder=""></textarea>
										</div>
									</div>
									<div class="form-group">
										<label class="col-sm-2 control-label">类型 <span
											class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
										</label>
										<div class="col-sm-6">
											<label class="col-md-5 radio-inline"> 
												<input
												type="radio" name="jobType" data-mapping="type" checked value="SHELL_JOB"> 启动脚本 
												<input
												type="text" class="form-control col-md-2" id="entryFile" name="entryFile" data-mapping="mainEntry"
												data-validation="radio_pane" placeholder="">
											</label> <label class="col-md-5 radio-inline"> 
												<input
												type="radio" name="jobType" data-mapping="type" value="JAR_JOB"> Jar 
												<input type="file"
												class="form-control col-md-2 file" id="jarFile" name="jarFile" data-mapping="mainEntry"
												data-validation="radio_pane" disabled="disabled" placeholder="">
											</label>
										</div>
									</div>
									<div class="form-group">
										<label for="sla" class="col-sm-2 control-label">SLA<span
											class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
										</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="sla" name="sla"
												data-validation="number" data-validation-error-msg="必填项(数字)"
												placeholder="">
										</div>
									</div>
									<div class="form-group">
										<label for="sle" class="col-sm-2 control-label">SLE <span
											class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
										</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="sle" name="sle"
												data-validation="number" data-validation-error-msg="必填项(数字)"
												placeholder="">
										</div>
									</div>
									<div class="form-group">
										<label for="mail" class="col-sm-2 control-label">Email <span
											class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
										</label>
										<div class="col-sm-6">
											<input type="text" class="form-control" id="mail" name="mail"
												data-validation="email" data-validation-error-msg="必填项"
												placeholder="">
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-2">
											<label class="col-sm-6 checkbox-inline"> <input
												type="checkbox" name="notified" checked value="true"> Job执行完成后，通知我状态。
											</label> <label class="checkbox-inline"> <input
												type="checkbox" name="disabled" value="true"> 创建但不启动调度
											</label>
										</div>
									</div>
									<div class="form-group fade">
										<input type="text" class="form-control" id="owner" name="owner" value="tenglinxiao"/>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-5 col-sm-10">
											<button type="submit" class="btn btn-default">创建</button>
										</div>
									</div>
								</form>
							</div>
						</div>
						<div class="panel panel-info">
							<div class="panel-heading">管理Job</div>
							<div class="panel-content">
								<form class="form-horizontal" style="display: none;">
									<div class="form-group">
										<label for="jobName" class="col-sm-2 control-label">名称</label>
										<div class="col-sm-9">
											<input type="email" class="form-control" id="filterJobName"
												placeholder="">
										</div>
									</div>
									<div class="form-group">
										<label for="filterJobType" class="col-sm-2 control-label">类型</label>
										<div class="col-sm-9" id="filterJobType">
											<label class="radio-inline"> <input type="radio"
												name="filterJobType" checked> 全部
											</label> <label class="radio-inline"> <input type="radio"
												name="filterJobType"> Shell
											</label> <label class="radio-inline"> <input type="radio"
												name="filterJobType"> Jar
											</label>
										</div>
									</div>
									<div class="form-group">
										<div class="col-sm-offset-5 col-sm-10">
											<button type="submit" class="btn btn-default">GO!</button>
										</div>
									</div>
								</form>
								<div class="container-fluid">
									<table id="privateJobs" class="table table-striped table-hover">
										<thead>
											<tr>
												<th>NO.</th>
												<th>名称</th>
												<th>组</th>
												<th>调度</th>
												<th>创建日期</th>
												<th>最后修改日期</th>
												<th>操作</th>
											</tr>
										</thead>
									</table>
								</div>
							</div>
						</div>

						<!-- Shell file explorer modal -->
						<div class="modal fade" id="explorer" tabindex="-1" role="dialog">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">选择Shell</h4>
									</div>
									<div class="modal-body">
										<div id="tree"></div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">取消</button>
										<button type="button" class="btn btn-primary">确定</button>
									</div>
								</div>
							</div>
						</div>
						
						<!-- Edit modal -->
						<div class="modal fade" id="editModal" tabindex="-1" role="dialog">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">编辑</h4>
									</div>
									<div class="modal-body">
										<form>
											<div class="form-group">
												<label for="sla" class="col-sm-2 control-label">名称</label>
												<div class="col-sm-9">
													<span class="form-control" id="editJobName"></span>
												</div>
											</div>
											<div class="form-group">
												<label for="sla" class="col-sm-2 control-label">组</label>
												<div class="col-sm-9">
													<span class="form-control" id="editJobGroup"></span>
												</div>
											</div>
											<div class="form-group">
												<label for="sla" class="col-sm-2 control-label">调度</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" id="sla"
														placeholder="">
												</div>
											</div>
											<div class="form-group">
												<label for="sla" class="col-sm-2 control-label">SLA</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" id="sla"
														placeholder="">
												</div>
											</div>
											<div class="form-group">
												<label for="sle" class="col-sm-2 control-label">SLE</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" id="sle"
														placeholder="">
												</div>
											</div>
											<div class="form-group">
												<div class="col-sm-offset-2">
													<label class="col-sm-6 checkbox-inline"> <input
														type="checkbox"> Job执行完成后，通知我状态。
													</label> <label class="checkbox-inline"> <input
														type="checkbox"> 启动调度
													</label>
												</div>
											</div>
										</form>
										<div class="alert alert-warning">
											启动调度后，后台将为该Job分配Slot,立即开始调度。</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">取消</button>
										<button type="button" class="btn btn-primary">确定</button>
									</div>
								</div>
							</div>
						</div>

						<!-- Remove modal -->
						<div class="modal fade" id="removeModal" tabindex="-1"
							role="dialog">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">确认</h4>
									</div>
									<div class="modal-body ">
										<div class="alert alert-danger">
											确定删除Job<span id="targetJob"></span>?
										</div>
										<div class="alert alert-warning">Note:
											确定被删除的job会被立即从调度器卸载，并且不可恢复！</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">取消</button>
										<button type="button" class="btn btn-primary">确定</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div role="tabpanel" class="tab-pane fade" id="public"
						aria-labelledby="public-tab">
						<div class="">
							<table id="publicJobs" class="table table-striped table-hover">
								<thead>
									<tr>
										<th>NO.</th>
										<th>名称</th>
										<th>组</th>
										<th>调度</th>
										<th>创建日期</th>
										<th>最后修改日期</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<div role="tabpanel" class="tab-pane fade" id="notification" 
						aria-labelledby="public-tab">
						<div class="panel panel-info">
							<div class="panel-heading">添加联系人</div>
							<div class="panel-content">
								<form id="jobForm" class="form-horizontal" action="/proxy/scheduler/rest/createJob" method="post" onsubmit="return false;">
										<div class="form-group">
											<label for="jobName" class="col-sm-2 control-label">名称
												<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
											</label>
											<div class="col-sm-6">
												<input type="text" class="form-control" id="jobName" name="jobName" data-mapping="name"
													data-validation="required" data-validation-error-msg="必填项"
													placeholder="">
											</div>
										</div>
										<div class="form-group">
											<label for="jobGroup" class="col-sm-2 control-label">组
												<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
											</label>
											<div class="col-sm-6">
												<select class="form-control" id="jobGroup" name="jobGroup" data-mapping="group"
													data-validation="" data-validation-error-msg="必填项">
													<option value="">----</option>
												</select>
											</div>
										</div>
										<div class="form-group">
											<label for="jobSchedule" class="col-sm-2 control-label">姓名
												<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
											</label>
											<div class="col-sm-6">
												<input type="text" class="form-control" id="jobSchedule" name="jobSchedule" data-mapping="schedule"
													data-validation="required" data-validation-error-msg="必填项"
													placeholder="">
											</div>
										</div>
										<div class="form-group">
											<label for="jobSchedule" class="col-sm-2 control-label">Email
												<span class="glyphicon glyphicon-asterisk" aria-hidden="true"></span>
											</label>
											<div class="col-sm-6">
												<input type="text" class="form-control" id="jobSchedule" name="jobSchedule" data-mapping="schedule"
													data-validation="required" data-validation-error-msg="必填项"
													placeholder="">
											</div>
										</div>
										<div class="form-group">
											<div class="col-sm-offset-5 col-sm-10">
												<button type="submit" class="btn btn-default">添加</button>
											</div>
										</div>
								</form>
							</div>
						</div>
						<div class="">
							<table id="publicJobs" class="table table-striped table-hover">
								<thead>
									<tr>
										<th>联系人</th>
										<th>邮件</th>
										<th>创建日期</th>
										<th>操作</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
				</div>
				
				<!-- Modal for ops return. -->
				<div class="modal fade" id="result" tabindex="-1"
							role="dialog">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h4 class="modal-title">结果</h4>
									</div>
									<div class="modal-body">
										<div class="alert alert-success">
											操作成功!
										</div>
										<div class="alert alert-danger hide">
											操作失败: <span></span>
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">确定</button>
									</div>
								</div>
							</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>