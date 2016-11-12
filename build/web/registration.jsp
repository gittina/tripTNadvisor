<%-- 
    Document   : registration
    Created on : 23-set-2016, 10.52.55
    Author     : Francesco
--%>
<%@page import="DataBase.Utente"%>
<%@page import="javax.websocket.Session"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><c:out value="${title}"/></title>
        <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />

        <!--     Fonts and icons     -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons" />
        <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" />

        <!-- CSS Files -->
        <link href="assets/css/bootstrap.min.css" rel="stylesheet" />
        <link href="assets/css/material-kit.css" rel="stylesheet"/>
        <link href="assets/css/demo.css" rel="stylesheet" />
    </head>
    <body class="signup-page">
        <!-- Navbar -->
        <nav class="navbar navbar-transparent navbar-fixed-top navbar-color-on-scroll">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navigation-index">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a href="<%= request.getContextPath()%>/HomeServlet">
                        <div class="logo-container">
                            <div class="logo">
                                <img src="<%= request.getContextPath()%>/assets/img/logo.png" alt="TNadvisor Logo">
                            </div>
                            <div class="brand">
                                Trip TN Advisor
                            </div>


                        </div>
                    </a>
                </div>

                <div class="collapse navbar-collapse" id="navigation-index">
                    <ul class="nav navbar-nav navbar-left">
                        <li>
                            <form class="navbar-form navbar-left" role="search" action="SearchServlet" method="POST">
                                <div class="form-group">
                                    <input type="text" value="" placeholder="Cerca" class="form-control" name="research"/>
                                    <button type="submit" class="btn btn-primary btn-sm">Submit</button>
                                </div>
                            </form>
                        </li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <img src="assets/img/flags/US.png"/>
                                Language
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="#"><img src="assets/img/flags/GB.png"/> English(UK)</a></li>
                                <li><a href="#"><img src="assets/img/flags/IT.png"/> Italiano</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <!-- End Navbar -->
        <div class="wrapper">
            <div class="header header-filter" style="background-image: url('<%=request.getContextPath()%>/assets/img/city.jpg'); background-size: cover; background-position: top center;">
                <div class="container">
                    <div class="row">
                        <div class="col-md-8 col-md-offset-2 col-sm-8 col-sm-offset-1">
                            <div class="card card-signup">

                                <form method="POST" action="<%= request.getContextPath()%>/RegistrationServlet">
                                    <div class="header header-primary text-center">
                                        <h4>Sign In</h4>
                                    </div>
                                    <div class="content"> 
                                        <span class="label label-danger"><c:out value="${problemMessage}"/></span>
                                        <div class="input-group">
                                            <span class="input-group-addon">
                                                <i class="material-icons">face</i>
                                            </span>
                                            <div class="form-group label-floating is-empty">
                                                <label class="control-label">Name</label>
                                                <input type="text" name="name" class="form-control">                                                
                                                <span class="label label-danger"><c:out value="${nomeMessage}"/></span>
                                            </div>
                                            <span class="input-group-addon">
                                                <i class="material-icons">face</i>
                                            </span>
                                            <div class="form-group label-floating is-empty">
                                                <label class="control-label">Surname</label>
                                                <input type="text" name="surname" class="form-control">                                             
                                                <span class="label label-danger"><c:out value="${cognomeMessage}"/></span>
                                            </div>
                                        </div>
                                        <div class="input-group">
                                            <span class="input-group-addon">
                                                <i class="material-icons">email</i>
                                            </span>
                                            <div class="form-group label-floating is-empty">
                                                <label class="control-label">Email</label>
                                                <input type="email" name="mail1" class="form-control">
                                                <span class="label label-danger"><c:out value="${doppioneMessage}"/></span>
                                            </div>
                                            <span class="input-group-addon">
                                                <i class="material-icons">email</i>
                                            </span>
                                            <div class="form-group label-floating is-empty">
                                                <label class="control-label">Confirm Email</label>
                                                <input type="email" name="mail2" class="form-control">
                                                <span class="label label-danger"><c:out value="${mailmessage}"/></span>
                                            </div>
                                        </div>

                                        <div class="input-group">
                                            <span class="input-group-addon">
                                                <i class="material-icons">lock_outline</i>
                                            </span>
                                            <div class="form-group label-floating is-empty">
                                                <label class="control-label">Password</label>
                                                <input type="password" name="pass1" class="form-control">
                                                <span class="label label-danger"><c:out value="${passMessage}"/></span>
                                            </div>
                                            <span class="input-group-addon">
                                                <i class="material-icons">lock_outline</i>
                                            </span>
                                            <div class="form-group label-floating is-empty">
                                                <label class="control-label">Repeat Password</label>
                                                <input type="password" name="pass2" class="form-control">
                                            </div>
                                        </div>
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="check">
                                                I accept the Terms of Use
                                            </label>
                                            <span class="label label-danger"><c:out value="${checkMessage}"/></span>
                                        </div>
                                    </div>
                                    <div class="footer text-center"> 
                                        <input type="submit" class="btn btn-simple btn-primary btn-lg" value="Submit">
                                        <input type="submit" class="btn btn-simple btn-primary btn-lg" value="Annulla" onclick="history.go(-1);">
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

            </div>

        </div>
    </body>
    <script src="assets/js/jquery.min.js" type="text/javascript"></script>
    <script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="assets/js/material.min.js"></script>

    <!--  Plugin for the Sliders, full documentation here: http://refreshless.com/nouislider/ -->
    <script src="assets/js/nouislider.min.js" type="text/javascript"></script>

    <!--  Plugin for the Datepicker, full documentation here: http://www.eyecon.ro/bootstrap-datepicker/ -->
    <script src="assets/js/bootstrap-datepicker.js" type="text/javascript"></script>

    <!-- Control Center for Material Kit: activating the ripples, parallax effects, scripts from the example pages etc -->
    <script src="assets/js/material-kit.js" type="text/javascript"></script>

    <script type="text/javascript">

                                            $().ready(function () {
                                                // the body of this function is in assets/material-kit.js
                                                materialKit.initSliders();
                                                window_width = $(window).width();

                                                if (window_width >= 992) {
                                                    big_image = $('.wrapper > .header');

                                                    $(window).on('scroll', materialKitDemo.checkScrollForParallax);
                                                }

                                            });
    </script>   
</html>
