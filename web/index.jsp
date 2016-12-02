<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${lan.getLanSelected()}" />
<fmt:setBundle basename="Resources.string" />

<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title><c:out value="${title}"/></title>
        <c:set value="/index.jsp" scope="session" var="lastPage"/>

        <!-- Bootstrap Core CSS -->
        <link href="<%= request.getContextPath()%>/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <!-- Theme CSS -->
        <link href="<%= request.getContextPath()%>/css/freelancer.min.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="<%= request.getContextPath()%>/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet" type="text/css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script type="text/javascript" src="<%= request.getContextPath()%>/scripts/jquery-1.8.2.min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/scripts/jquery.mockjax.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/src/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/autocomplete.txt"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/scripts/demo.js"></script>
    </head>

    <body id="page-top" class="index">

        <!-- Navigation -->
        <nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom">
            <div class="container">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header page-scroll">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span> Menu <i class="fa fa-bars"></i>
                    </button>
                    <a class="navbar-brand" href="<%= request.getContextPath()%>/HomeServlet">TRIPTNADVISOR</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

                    <ul class="nav navbar-nav navbar-left">
                        <c:choose>
                            <c:when test="${utente == null}">
                                <li>
                                    <a href="<%= request.getContextPath()%>/registration.jsp"><fmt:message key="welcome.visitors"/></a>
                                </li>
                            </c:when>
                            <c:when test="${utente.isAmministratore()}">
                                <li>
                                    <button class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                        <span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><fmt:message key="profile"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:when test="${utente.isRegistrato()}">
                                <li>
                                    <button class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                        <span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><fmt:message key="profile"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneAddRistorante"><fmt:message key="add.restaurant"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                    </ul>
                                </li>
                            </c:when>
                            <c:when test="${utente.isRistoratore()}">
                                <li>
                                    <button class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                        <span class="caret"></span></button>            
                                    <ul class="dropdown-menu">
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><font class="dropdown-line"><fmt:message key="profile"/></font></a></li>
                                        <li><a href="<%= request.getContextPath()%>/privateRistoratore/ConfigurazioneRistoranti"><fmt:message key="my.restaurant"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneAddRistorante"><fmt:message key="add.restaurant"/></a></li>
                                        <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                    </ul>
                                </li>
                            </c:when>
                        </c:choose>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <c:choose>
                            <c:when test="${utente == null}">
                                <li>
                                    <a href="<%= request.getContextPath()%>/login.jsp">Login</a>
                                </li>
                                <li>
                                    <a href="<%= request.getContextPath()%>/registration.jsp">
                                        <fmt:message key="register"/>
                                    </a>
                                </li>
                            </c:when>
                            <c:when test="${utente.isRistoratore() || utente.isAmministratore()}">
                                <li>
                                    <button class="btn btn-primary dropdown-toggle" data-toggle="dropdown"><fmt:message key="notify"/>
                                        <span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <c:choose>
                                            <c:when test="${utente.getNotifiche().size()>0}">
                                                <c:forEach var="notifica" items="${utente.getNotifiche()}">
                                                    <li>
                                                        <a href="<%=request.getContextPath()%>/private/PrepareNotificheServlet?id_not=<c:out value="${notifica.getId()}"/>">
                                                            <c:out value="${notifica.toString()}"/>
                                                        </a>
                                                    </li>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <li>
                                                    <a href="<%=request.getContextPath()%>/private/PrepareNotificheServlet?">
                                                        Nessuna notifica
                                                    </a>
                                                </li>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </li>
                            </c:when>  
                        </c:choose>
                        <li>
                            <button class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
                                <img src="<%= request.getContextPath()%><fmt:message key="bandiera"/>" alt="- "/>
                                <fmt:message key="language"/>
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=en_GB"><img src="<%= request.getContextPath()%>/img/flags/GB.png" alt="- "/><fmt:message key="english"/></a></li>
                                <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=it_IT"><img src="<%= request.getContextPath()%>/img/flags/IT.png" alt="- "/><fmt:message key="italian"/></a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
                <!-- /.navbar-collapse -->
            </div>
            <!-- /.container-fluid -->
        </nav>




        <!-- Header -->
        <header>
            <div class="container">
                <div class="row">
                    <img class="img-responsive" src="<%= request.getContextPath()%>/img/profile.jpeg" alt="" height="200" width="400">
                    <div class="intro-text">
                        <span class="name"><fmt:message key="index.spot"/></span>
                    </div>
                    <hr class="star-light">
                    <div class="col-md-2"></div>
                    <div class="col-md-8">
                        <form role="search" method="POST" action="<%= request.getContextPath()%>/SearchServlet?tipo=Advanced_1">
                            <div class="row">
                                <div class="form-group">
                                    <div class="form-group">
                                        <div style="position: relative; height: 80px;">
                                            <input type="text" placeholder=" <fmt:message key="cerca"/>" class="form-control" name="research" id="autocomplete-ajax" style="position: absolute; z-index: 2;"/>
                                            <input type="text" name="research" id="autocomplete-ajax-x" disabled="disabled" style="color: #CCC; background: transparent; z-index: 1;"/>
                                        </div>
                                    </div>
                                    <div class="radio">
                                        <h5>
                                            <label><input type="radio" name="spec" value="nome" checked="checked"><fmt:message key="name"/> | </label>
                                            <label><input type="radio" name="spec" value="addr"><fmt:message key="address"/> | </label>
                                            <label><input type="radio" name="spec" value="zona"><fmt:message key="geographic.zone"/> | </label>
                                            <label><input type="radio" name="spec" value="spec"><fmt:message key="specialty"/></label>
                                        </h5>
                                    </div>
                                    <button type="submit" class="btn btn-primary"><fmt:message key="search"/></button>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <div id="divSample" class="hideClass">
                                        <input hidden type="text" id="Latitude" name="Latitude"/>
                                        <input hidden type="text" id="Longitude" name="Longitude"/>
                                    </div>
                                    <div id="temp" style="display:none"><h5><fmt:message key="receiving"/></h5></div>
                                    <div id="received" style="display:none"><h5><fmt:message key="received"/></h5></div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <h5 class=""><fmt:message key="search.type"/></h5>
                                    <div class="checkbox">
                                        <h5>
                                            <label><input type="checkbox" name="spec" value="Ristorante"><fmt:message key="restaurant"/> | </label>
                                            <label><input type="checkbox" name="spec" value="Pizzeria"><fmt:message key="pizzeria"/> | </label>
                                            <label><input type="checkbox" name="spec" value="Trattoria"><fmt:message key="tavern"/> | </label>
                                            <label><input type="checkbox" name="spec" value="Polleria"><fmt:message key="polleria"/> | </label>
                                            <label><input type="checkbox" name="spec" value="Chinese"><fmt:message key="chinese"/> | </label>
                                            <label><input type="checkbox" name="spec" value="Japanese"><fmt:message key="japanese"/></label>
                                        </h5>
                                    </div>
                                </div>
                            </div>
                        </form>
                        <button class="btn btn-primary" id="getLoc" onclick="getLocationConstant()">
                            <fmt:message key="getLoc"/>
                        </button>
                    </div>
                    <div class="col-md-2"></div>
                </div>
            </div>
        </header>

        <!-- Ristoranti pi� votati -->
        <section>
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <h2><fmt:message key="most.voted"/></h2>
                        <hr class="star-primary">
                    </div>
                </div>

                <div class="row">
                    <c:forEach var="ristorante" items="${mostVoted}">
                        <div class="col-md-4">
                            <h5>
                                <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${ristorante.getId()}"/>" class="portfolio-link" data-toggle="modal">
                                    <c:out value="${ristorante.getName()}"/><br>
                                    <fmt:message key="users.vote"/>
                                    <c:if test="${ristorante.getVoto() == 0.0}">
                                        <fmt:message key="no.vote"/>
                                    </c:if>
                                    <c:if test="${ristorante.getVoto() > 0}">
                                        <c:out value="${ristorante.getVoto()}"/>
                                    </c:if><br><br>
                                    <img src="<%= request.getContextPath()%><c:out value="${ristorante.getFoto().get(0).getFotopath()}"/>" class="img-responsive" alt="" height="200" width="300">
                                </a>
                            </h5>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>


        <!-- Ristoranti pi� visti -->
        <section>
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <h2><fmt:message key="most.seen"/></h2>
                        <hr class="star-primary">
                    </div>
                </div>

                <div class="row">
                    <c:forEach var="ristorante" items="${mostSeen}">
                        <div class="col-md-4">
                            <h5>
                                <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${ristorante.getId()}"/>o" class="portfolio-link" data-toggle="modal">
                                    <c:out value="${ristorante.getName()}"/><br>
                                    <fmt:message key="users.posClass"/>
                                    <c:out value="${ristorante.getPosizioneClassifica()}"/><br><br>
                                    <img src="<%= request.getContextPath()%><c:out value="${ristorante.getFoto().get(0).getFotopath()}"/>" class="img-responsive" alt="" height="200" width="300">
                                </a>
                            </h5>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>


        <!-- Recensioni pi� utili -->
        <section>
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <h2><fmt:message key="last.rec"/></h2>
                        <hr class="star-primary">
                    </div>
                </div>

                <div class="row">
                    <c:forEach var="rec" items="${lastRec}">
                        <div class="col-md-4">
                            <h5>
                                <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${rec.getRistorante().getId()}"/>" class="portfolio-link" data-toggle="modal">
                                    <fmt:message key="by"/> 
                                    <c:out value="${rec.getUtente().getNomeCognome()}"/>
                                    (<c:out value="${rec.getUtente().getReputazione()}"/>), 
                                    <c:out value="${rec.getData()}"/><br>
                                    <fmt:message key="val.media"/>: <c:out value="${rec.getMediaVoti()}"/><br><br>
                                    <img src="<%= request.getContextPath()%><c:out value="${rec.getFotoPath()}"/>" class="img-responsive" alt="<fmt:message key="no.img"/>"><br>
                                    <i><c:out value="${rec.getTesto()}"/></i><br><br>
                                    <c:if test="${rec.getCommento()!=null}">
                                        <fmt:message key="answer"/><br><c:out value="${rec.getCommento()}"/>
                                    </c:if>
                                </a>
                            </h5>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </section>


        <!-- Footer -->
        <footer class="text-center">
            <div class="footer-above">
                <div class="container">
                    <div class="row">
                        <div class="footer-col col-md-6">
                            <h3>Location</h3>
                            <p>Polo Ferrari, Via Sommarive 5
                                <br>TRENTO, TN 38100</p>
                        </div>
                        <div class="footer-col col-md-6">
                            <h3>About TripTNadvisor</h3>
                            <p>TripTNadvisor is free to use, developed by UNITN students on 2016</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="footer-below">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12">
                            Copyright &copy; TRIPTNADVISOR 2016
                        </div>
                    </div>
                </div>
            </div>
        </footer>


        <!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->

        <div class="scroll-top page-scroll hidden-sm hidden-xs hidden-lg hidden-md">
            <a class="btn btn-primary" href="#page-top">
                <i class="fa fa-chevron-up"></i>
            </a>
        </div>


        <!-- Geolocation -->
        <script src="<%= request.getContextPath()%>/js/geoloc.js" type="text/javascript"></script>

        <!-- jQuery -->
        <script src="<%= request.getContextPath()%>/vendor/jquery/jquery.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="<%= request.getContextPath()%>/vendor/bootstrap/js/bootstrap.min.js"></script>

        <!-- Plugin JavaScript -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>

        <!-- Theme JavaScript -->
        <script src="<%= request.getContextPath()%>/js/freelancer.min.js"></script>

    </body>

</html>
