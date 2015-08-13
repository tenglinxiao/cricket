<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>Login</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="css/login.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="js/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="js/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/jquery/jquery.form-validator.min.js"></script>
    <script>
        $(function() {
            $.validate({
                form: 'form',
                onElementValidate: function(valid, $el, $form, errorMsg) {
                    if (valid) {
                        $el.next('.glyphicon-ok').removeClass('hide').next('.glyphicon-remove').addClass('hide')
                                .parents('.input-group').removeClass('has-error').next('.input-group').addClass('hide');
                    } else {
                        $el.next('.glyphicon-ok').addClass('hide').next('.glyphicon-remove').removeClass("hide")
                                .parents('.input-group').addClass('has-error').next('.input-group').removeClass('hide');
                    }
                }
            });

            setTimeout(function(){
                $('#validation_error').fadeOut('slow');
            }, 3000);
        });

    </script>
</head>
<body>
<div class="container">
    <div id="login-panel">
        <div class="text-center">
            <img id="login-img" src="images/login.jpg"/>
        </div>
        <div class="form">
            <s:if test="hasActionErrors()">
                <div id="validation_error" class="alert alert-danger alert-dismissible fade in" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>
                    <s:actionerror/>
                </div>
            </s:if>
            <form class="form-signin" action="authenticate" method="post" data-toggle="validator" role="form">
                <div class="input-group">
                    <span class="input-group-addon"><i class="fa fa-user fa-fw"></i></span>
                    <input type="text" class="form-control" name="username" placeholder="Username" data-validation="length"
                           data-validation-length="6-20" data-validation-error-msg-container=".username-error"
                           data-validation-error-msg="Username length MUST between 6 - 20!" aria-describedby="basic-addon1">
                    <span class="glyphicon glyphicon-ok form-control-feedback hide"></span>
                    <span class="glyphicon glyphicon-remove form-control-feedback hide"></span>
                </div>
                <div class="input-group username-error hide"></div>
                <div class="input-group">
                    <span class="input-group-addon"><i class="fa fa-lock fa-fw"></i></span>
                    <input type="password" class="form-control" name="password" placeholder="Password" data-validation="length"
                           data-validation-length="6-20" data-validation-error-msg-container=".password-error"
                           data-validation-error-msg="Password length MUST between 6 - 20!" aria-describedby="basic-addon2">
                    <span class="glyphicon glyphicon-ok form-control-feedback hide"></span>
                    <span class="glyphicon glyphicon-remove form-control-feedback hide"></span>
                </div>
                <div class="input-group password-error hide"></div>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="remember-me"> Remember me
                    </label>
                </div>
                <button class="btn btn-primary btn-block" type="submit">Sign in</button>
            </form>
        </div>
        <div class="row" style="margin-top: 5px;">
            <div class="col-md-6">
                <div class="pull-left">
                    <a href="#">Forget Password?</a>
                </div>
            </div>
            <div class="col-md-6">
                <div class="pull-right">
                    <a href="#">Register</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>