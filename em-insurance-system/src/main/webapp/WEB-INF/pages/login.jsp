
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
 <head>
     <title><spring:message  code="project.title" /></title>
     <link rel="icon" type="image/x-icon" href="<c:url value='/libs/favicon.ico' /> ">
     <link  href="<c:url value='/libs/bootstrap/dist/css/bootstrap.min.css' /> " rel="stylesheet">
     <!-- Font Awesome -->
     <link href="<c:url value='/libs/font-awesome/css/font-awesome.min.css' /> " rel="stylesheet">
     <!-- NProgress -->
     <link  href="<c:url value='/libs/nprogress/nprogress.css' /> " rel="stylesheet">
     <link href="<c:url value='/libs/pnotify/dist/pnotify.css' /> " rel="stylesheet">
     <link href="<c:url value='/libs/pnotify/dist/pnotify.buttons.css' /> " rel="stylesheet">
     <link href="<c:url value='/libs/pnotify/dist/pnotify.nonblock.css' /> " rel="stylesheet">
     <!-- iCheck -->
     <link href="<c:url value='/libs/iCheck/skins/flat/green.css' /> " rel="stylesheet">
     <link href="<c:url value='/libs/css/login.css' /> " rel="stylesheet">
     <script src="<c:url value='/libs/jquery/dist/jquery.min.js' /> "></script>
     <script  src="<c:url value='/libs/bootstrap/dist/js/bootstrap.min.js' /> "></script>
     <script src="<c:url value="/libs/pnotify/dist/pnotify.js"/>"></script>
     <script src="<c:url value="/libs/pnotify/dist/pnotify.buttons.js"/>"></script>
     <script  src="<c:url value="/libs/pnotify/dist/pnotify.nonblock.js"/>"></script>
     <script src="<c:url value="/libs/jquery-validation/jquery.validate.min.js"/>"></script>
     <script src="<c:url value="/libs/jquery-validation/additional-methods.min.js"/>"></script>
     <script type="text/javascript" src="<c:url value="/js/modules/login/login.js"/>"></script>
 </head>

<body>
<div class="container">

    <div class="col-md-10 col-md-offset-1 main" >
        <div class="col-md-6 left-side" >
            <h3><spring:message code='project.name'/></h3>
            <div class="kv-avatar center-block" style="width:400px">
                <img  src="<c:url value='/logo'/> " style="width:200px">
            </div>
            <br>


        </div><!--col-sm-6-->

        <div class="col-md-6 right-side">
            <h3>Login</h3>

            <!--Form with header-->
            <div class="form">

                <form action="j_spring_security_check" method="post"  novalidate="novalidate" autocomplete="off" id="login_form">
                    <div class="alert alert-danger alert-dismissible fade in" role="alert" id="login-error">
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span>
                        </button>
                        ${fn:replace(SPRING_SECURITY_LAST_EXCEPTION.message, 'Bad credentials', 'Username/Password are incorrect')}
                    </div>
                    <div>
                <div class="form-group">
                    <label for="form2">Username</label>
                    <input type="text" id="form2" class="form-control input-lg" name="j_username">

                </div>

                <div class="form-group">
                    <label for="form4">Password</label>
                    <input type="password" id="form4" class="form-control input-lg" name="j_password">

                </div>

                <div class="text-xs-center">
                    <button type="button" id="btn-forgot-password"  onclick="forgotPassword()" class="btn btn-link btn-danger"><spring:message code="login.forgotPassword" /></button>
                    <button class="btn btn-deep-purple" type="submit"><spring:message code="login.signIn" /></button>
                </div>
                    </div>
                </form>

            </div>
        </div>
    </div>

    <div class="modal fade bs-example-modal-sm" id="myPleaseWait" tabindex="-1"
         role="dialog" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">
                    <span class="glyphicon glyphicon-time">
                    </span>Please Wait
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="progress">
                        <div class="progress-bar progress-bar-info
                    progress-bar-striped active"
                             style="width: 100%">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<script>
    $(function(){

        $(document).ready(function() {
            var url =  $(location).attr('href');
            var error =url.indexOf("j_spring_security_check") >= 0;
            if(error){
                $('#login-error').show();
            }
            else{
                $('#login-error').hide();
            }
        });
        $(document).ajaxStart(function () {
            $("#btn-forgot-password").attr("disabled", true);
        });
        $(document).ajaxComplete(function () {
            $("#btn-forgot-password").attr("disabled", false);
        });

    });

    function forgotPassword(){
            var $form = $('#login_form');
            var data = {};
            $form.serializeArray().map(function(x){data[x.name] = x.value;});
            var url = "resetPassword";
            // console.log(data);
            $('#myPleaseWait').modal({
                backdrop: 'static',
                keyboard: true
            });
            var request = $.post(url, data );
            request.success(function(){
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Success',
                    text: 'Credentials sent to your email address',
                    type: 'success',
                    styling: 'bootstrap3'
                });
                // validator.resetForm();
            });
            request.error(function(jqXHR, textStatus, errorThrown){
                console.log('here....');
                $('#myPleaseWait').modal('hide');
                new PNotify({
                    title: 'Error',
                    text: jqXHR.responseText,
                    type: 'error',
                    styling: 'bootstrap3'
                });
            });
            request.always(function(){
            });
    }
</script>
</html>