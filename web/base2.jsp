<!doctype html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${lan.getLanSelected()}" />
<fmt:setBundle basename="Resources.string" />
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <link rel="apple-touch-icon" sizes="76x76" href="assets/img/apple-icon.png">
        <link rel="icon" type="image/png" href="assets/img/favicon.png">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

        <title><c:out value="${title}"/></title>
        <c:set value="/blablabla.jsp" scope="session" var="lastPage"/>

        <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport' />

        <!--     Fonts and icons     -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons" />
        <link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700" />
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css" />

        <!-- CSS Files -->
        <link href="assets/css/bootstrap.min.css" rel="stylesheet" />
        <link href="assets/css/material-kit.css" rel="stylesheet"/>
        <link href="assets/css/demo.css" rel="stylesheet" />

        <!--   Core JS Files   -->
        <script src="assets/js/jquery.min.js" type="text/javascript"></script>
        <script src="assets/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="assets/js/material.min.js"></script>

        <!--  Plugin for the Sliders, full documentation here: http://refreshless.com/nouislider/ -->
        <script src="assets/js/nouislider.min.js" type="text/javascript"></script>

        <!--  Plugin for the Datepicker, full documentation here: http://www.eyecon.ro/bootstrap-datepicker/ -->
        <script src="assets/js/bootstrap-datepicker.js" type="text/javascript"></script>

        <!-- Control Center for Material Kit: activating the ripples, parallax effects, scripts from the example pages etc -->
        <script src="assets/js/material-kit.js" type="text/javascript"></script>

        <script type="text/javascript" src="scripts/jquery-1.8.2.min.js"></script>
        <script type="text/javascript" src="scripts/jquery.mockjax.js"></script>
        <script type="text/javascript" src="src/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="autocomplete.txt"></script>
        <script type="text/javascript" src="scripts/demo.js"></script>

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

    </head>

    <body class="index-page">
        <!-- Navbar -->
        <nav class="navbar navbar-primary navbar-transparent navbar-fixed-top navbar-color-on-scroll">
            <div class="container">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navigation-index">
                        <span class="sr-only">Toggle navigation</span>

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
                            <c:choose>
                                <c:when test="${utente == null}">
                                    <a href="<%= request.getContextPath()%>/registration.jsp"><fmt:message key="welcome.visitors"/></a>
                                </c:when>
                                <c:when test="${utente.isAmministratore()}">

                                    <a data-toggle="dropdown"> <img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><fmt:message key="profile"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                    </ul>

                                </c:when>
                                <c:when test="${utente.isRegistrato()}">

                                    <a data-toggle="dropdown"> <img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><fmt:message key="profile"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneAddRistorante"><fmt:message key="add.restaurant"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                    </ul>

                                </c:when>
                                <c:when test="${utente.isRistoratore()}">

                                    <a  data-toggle="dropdown"> <img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><fmt:message key="profile"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/privateRistoratore/ConfigurazioneRistoranti"><fmt:message key="my.restaurant"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneAddRistorante"><fmt:message key="add.restaurant"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                    </ul>

                                </c:when>
                            </c:choose>
                        </li>
                        <li>
                            <form class="navbar-form navbar-left" role="search" action="<%= request.getContextPath()%>/SearchServlet?tipo=Simple" method="POST">
                                <div class="form-group" style="position: relative; height: 40px;">
                                    <input type="text" placeholder=" <fmt:message key="cerca"/>" class="form-control input-sm" name="research" id="autocomplete-ajax" style="position: absolute; z-index: 2;"/>
                                    <input type="text" name="research" id="autocomplete-ajax-x" disabled="disabled" style="color: #CCC; background: transparent; z-index: 1;"/>
                                    <button type="submit" class="btn btn-primary btn-sm" style="position: initial;  border-style: solid; border-width: 1px;"><fmt:message key="submit"/></button>
                                </div>

                            </form>
                        </li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <c:choose>
                            <c:when test="${utente == null}">
                                <li>
                                    <a data-toggle="modal" data-target="#myModal">
                                        <i class="material-icons">account_circle</i> Login
                                    </a>
                                </li>
                                <li>
                                    <a href="<%= request.getContextPath()%>/registration.jsp">
                                        <i class="material-icons">create</i><fmt:message key="register"/>
                                    </a>
                                </li>
                            </c:when>
                            <c:when test="${utente.isRistoratore() || utente.isAmministratore()}">
                                <li>


                                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><fmt:message key="notify"/>
                                        <span class="caret"></span></a>
                                    <ul class="dropdown-menu">
                                        <c:forEach var="notifica" items="${utente.getNotifiche()}">
                                            <li>
                                                <a href="<%=request.getContextPath()%>/private/PrepareNotificheServlet?id_not=<c:out value="${notifica.getId()}"/>">
                                                    <c:out value="${notifica.toString()}"/>
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ul>

                                </li>

                            </c:when>
                        </c:choose>  
                        <li>
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <img src="assets/img/flags/US.png"/>
                                Language
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=en_GB"><img src="img/flags/GB.png"/><fmt:message key="english"/></a></li>
                                <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=it_IT"><img src="img/flags/IT.png"/><fmt:message key="italian"/></a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <!-- End Navbar -->

        <div class="wrapper">
            <div class="header header-filter" style="background-image: url('assets/img/bg2.jpeg');">
                <div class="container">
                    <div class="row">
                        <div class="col-md-8 col-md-offset-2">
                            <!-- Central code here -->
                        </div>
                    </div>
                </div>
            </div>

            <div class="main main-raised">
                <!-- Start Sections -->
                <div class="section section-basic">
                    <div class="container">



                    </div>
                </div>
                <!-- End Sections  -->

            </div>

            <footer class="footer">
                <div class="container">
                    <nav class="pull-left">
                        <ul>
                            <li>
                                <a href="http://www.creative-tim.com/license">
                                    Developed by Unitn-BrazzaGroup
                                </a>
                            </li>
                        </ul>
                    </nav>
                    <div class="copyright pull-right">
                        &copy;2016 Brazza Group Company <i class="material-icons">favorite</i>
                    </div>
                </div>
            </footer>
        </div>

        <!-- Sart Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-body">
                        <div class="card card-signup">
                            <form method="POST" action="<%= request.getContextPath()%>/LoginServlet"> 
                                <div class="header header-primary text-center">
                                    <h4>Login</h4>
                                </div>
                                <div class="content">
                                    <div class="input-group">
                                        <span class="input-group-addon">
                                            <i class="material-icons">email</i>
                                        </span>
                                        <div class="form-group is-empty"><input name="mail" type="text" class="form-control" placeholder="Email..."><span class="material-input"></span></div>
                                    </div>

                                    <div class="input-group">
                                        <span class="input-group-addon">
                                            <i class="material-icons">lock_outline</i>
                                        </span>
                                        <div class="form-group is-empty"><input name="password" type="password" placeholder="Password..." class="form-control"><span class="material-input"></span></div>
                                    </div>
                                </div>
                                <div class="footer text-center">
                                    <input type="submit" class="btn btn-simple btn-primary btn-lg" value="<fmt:message key="submit"/>">
                                </div>
                            </form>
                        </div>                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-info btn-simple" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!--  End Modal -->


    </body>


</html>
