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


        <script type="text/javascript">
            function getLocationConstant() {
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(onGeoSuccess, onGeoError);
                    document.getElementById("temp").style.display = 'block';
                } else {
                    alert("Your browser or device doesn't support Geolocation");
                }
            }

            // If we have a successful location update
            function onGeoSuccess(event) {
                document.getElementById("Latitude").value = event.coords.latitude;
                document.getElementById("Longitude").value = event.coords.longitude;
                document.getElementById("temp").style.display = 'none';
                document.getElementById("received").style.display = 'block';
                document.getElementById("getLoc").style.display = 'none';
            }

            // If something has gone wrong with the geolocation request
            function onGeoError(event) {
                alert("Error code " + event.code + ". " + event.message);
            }
        </script>

        <script type="text/javascript" src="/scripts/jquery-1.8.2.min.js"></script>
        <script type="text/javascript" src="/scripts/jquery.mockjax.js"></script>
        <script type="text/javascript" src="/src/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="/autocomplete.txt"></script>
        <script type="text/javascript" src="/scripts/demo.js"></script>

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
                                <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=en_GB"><img src="assets/img/flags/GB.png"/><fmt:message key="english"/></a></li>
                                <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=it_IT"><img src="assets/img/flags/IT.png"/><fmt:message key="italian"/></a></li>
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
                            <div class="brand" style=" margin-top: 150px;">
                                <h2>
                                    <fmt:message key="index.spot"/>
                                </h2>

                                <form role="search" method="POST" action="<%= request.getContextPath()%>/SearchServlet?tipo=Advanced_1">
                                    <div class="row">

                                        <div class="form-group">
                                            <div class="form-group">
                                                <input type="text" placeholder="<fmt:message key="cerca"/>" class="form-control" name="research" style="color: white"/>
                                            </div>
                                            <div class="radio">
                                                <label><input type="radio" name="spec" value="nome" checked="checked"><fmt:message key="name"/></label>
                                                <label><input type="radio" name="spec" value="addr"><fmt:message key="address"/></label>
                                                <label><input type="radio" name="spec" value="zona"><fmt:message key="geographic.zone"/></label>
                                                <label><input type="radio" name="spec" value="spec"><fmt:message key="specialty"/></label>
                                            </div>
                                            <button type="submit" class="btn btn-primary"><fmt:message key="search"/></button>
                                        </div>

                                    </div>
                                    <div class="row" style="color: white;">
                                        <button class="btn btn-primary" id="getLoc" onclick="getLocationConstant()">Get Location</button>

                                        <div class="form-group">
                                            <div id="divSample" class="hideClass">
                                                <input hidden type="text" id="Latitude" name="Latitude" value="">
                                                <input hidden type="text" id="Longitude" name="Longitude" value="">
                                            </div>
                                            <div id="temp" style="display:none"><h6><fmt:message key="receiving"/>...</h6></div>
                                            <div id="received" style="display:none">Received <i class="material-icons">done</i></div>
                                        </div>

                                    </div>
                                    <div class="row">

                                        <div class="form-group">
                                            <h6 class=""><fmt:message key="search.type"/></h6>
                                            <div class="checkbox">
                                                <label><input type="checkbox" name="spec" value="Ristorante"><fmt:message key="restaurant"/> </label>
                                                <label><input type="checkbox" name="spec" value="Pizzeria"><fmt:message key="pizzeria"/> </label>
                                                <label><input type="checkbox" name="spec" value="Trattoria"><fmt:message key="tavern"/> </label>
                                                <label><input type="checkbox" name="spec" value="Polleria"><fmt:message key="polleria"/> </label>
                                                <label><input type="checkbox" name="spec" value="Chinese"><fmt:message key="chinese"/> </label>
                                                <label><input type="checkbox" name="spec" value="Japanese"><fmt:message key="japanese"/> </label>
                                            </div>
                                        </div>

                                    </div>

                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="main main-raised">
                <!-- Start Sections -->
                <div class="section section-basic">
                    <div class="container">
                        <div class="row">
                            <div class="col-md-3">
                                <h3>
                                    <fmt:message key="most.voted"/>
                                </h3>
                                <c:forEach var="ristorante" items="${mostVoted}">
                                    <div class="row">
                                        <h4>
                                            <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${ristorante.getId()}"/>"><c:out value="${ristorante.getName()}"/></a>
                                        </h4>
                                        <div>
                                            <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${ristorante.getId()}"/>"><img src="<%= request.getContextPath()%><c:out value="${ristorante.getFoto().get(0).getFotopath()}"/>" style="max-height: 100%; max-width: 100%"/></a>
                                                <fmt:message key="users.vote"/> 
                                                <c:if test="${ristorante.getVoto() == 0.0}">
                                                    <fmt:message key="no.vote"/>
                                                </c:if>
                                                <c:if test="${ristorante.getVoto() > 0}">
                                                    <c:out value="${ristorante.getVoto()}"/>
                                                </c:if><br><br>
                                            <fmt:message key="cooking.type"/> <c:out value="${ristorante.getCucina()}"/> <br>
                                            <fmt:message key="economy.zone"/> <c:out value="${ristorante.getFascia()}"/> <br>
                                            <fmt:message key="web.site"/> <a href="<c:out value="${ristorante.getLinksito()}"/>"><c:out value="${ristorante.getName()}"/></a><br>           
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="col-md-1"></div>
                            <div class="col-md-3">
                                <h3>
                                    <fmt:message key="most.seen"/>
                                </h3>
                                <c:forEach var="ristorante" items="${mostSeen}">
                                    <div class="row">
                                        <h4>
                                            <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${ristorante.getId()}"/>"><c:out value="${ristorante.getName()}"/></a>
                                        </h4>
                                        <div>
                                            <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${ristorante.getId()}"/>"><img src="<%= request.getContextPath()%><c:out value="${ristorante.getFoto().get(0).getFotopath()}"/>" style="max-height: 100%; max-width: 100%"/></a>
                                                <fmt:message key="users.vote"/> 
                                                <c:if test="${ristorante.getVoto() == 0.0}">
                                                    <fmt:message key="no.vote"/>
                                                </c:if>
                                                <c:if test="${ristorante.getVoto() > 0}">
                                                    <c:out value="${ristorante.getVoto()}"/>
                                                </c:if><br><br>
                                            <fmt:message key="cooking.type"/> <c:out value="${ristorante.getCucina()}"/> <br>
                                            <fmt:message key="economy.zone"/> <c:out value="${ristorante.getFascia()}"/> <br>
                                            <fmt:message key="web.site"/> <a href="<c:out value="${ristorante.getLinksito()}"/>"><c:out value="${ristorante.getName()}"/></a><br>           
                                        </div>
                                    </div>
                                    <div class="row"></div>
                                </c:forEach>
                            </div>
                            <div class="col-md-1"></div>
                            <div class="col-md-3">
                                <h3>
                                    <fmt:message key="last.rec"/>
                                </h3>
                                <c:forEach var="rec" items="${lastRec}">
                                    <div class="row">
                                        <h4>
                                            <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${rec.getRistorante().getId()}"/>"><c:out value="${rec.getTitolo()}"/><br></a>
                                        </h4>
                                        <h6>
                                            <fmt:message key="by"/> <c:out value="${rec.getUtente().getNomeCognome()}"/>(<c:out value="${rec.getUtente().getReputazione()}"/>), <c:out value="${rec.getData()}"/><br>
                                        </h6>

                                        <div>
                                            <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${rec.getRistorante().getId()}"/>"><img src="<%= request.getContextPath()%><c:out value="${rec.getFotoPath()}"/>" style="max-height: 100%; max-width: 100%"/></a>
                                            <fmt:message key="val.media"/>: <c:out value="${rec.getMediaVoti()}"/><br>
                                            <p><c:out value="${rec.getTesto()}"/></p>
                                            <fmt:message key="answer"/>:<p><c:out value="${rec.getCommento()}"/></p>         
                                        </div>
                                    </div>
                                    <div class="row"></div>
                                </c:forEach>
                            </div>
                        </div>


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
                        </div>                    
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-info btn-simple" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <!--  End Modal -->
    </body>

</html>
