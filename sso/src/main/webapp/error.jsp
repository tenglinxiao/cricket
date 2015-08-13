<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <script src="js/jquery/jquery.min.js"></script>
</head>
<body>
<div style="display: table; position: absolute; height: 100%; width: 100%;">
    <div style="display: table-cell; vertical-align: middle;">
        <div class="panel panel-danger" style="width:40%; margin: auto auto;">
            <div class="panel-heading">${pageContext.errorData.throwable.message}</div>
            <div class="panel-body">
                <div class="alert alert-success" style="margin:0px">
                    Refer to the following link style to complete sso:
                    <span class="label label-danger">http://{domain}/sso?target={target_page}</span>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
