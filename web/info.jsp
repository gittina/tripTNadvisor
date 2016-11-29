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
        <c:set value="/info.jsp" scope="session" var="lastPage"/>

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

        <!-- javascript file specific for this page-->
        <script type="text/javascript" src="<%= request.getContextPath()%>/personalScript/show_hidden.js"></script>

        <!-- maps scripts-->
        <script type="text/javascript">
            function initialize() {

                var myLatLng = new google.maps.LatLng(<c:out value="${ristorante.getLuogo().getLat()}"/>, <c:out value="${ristorante.getLuogo().getLng()}"/>);
                var myOptions = {
                    zoom: 14,
                    center: myLatLng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };

                var map = new google.maps.Map(document.getElementById("gmaps-canvas"), myOptions);

            <c:forEach items="${ristorante.getVicini()}" var="rist">

                var marker<c:out value="${rist.getId()}"/> = new google.maps.Marker({
                    position: new google.maps.LatLng(<c:out value="${rist.getLuogo().getLat()}"/>, <c:out value="${rist.getLuogo().getLng()}"/>),
                    map: map,
                    title: "<c:out value="${rist.getName()}"/>"
                });

                var contentString<c:out value="${rist.getId()}"/> = '<c:out value="${rist.getName()}"/>';
                var infoWindow<c:out value="${rist.getId()}"/> = new google.maps.InfoWindow({
                    content: contentString<c:out value="${rist.getId()}"/>
                });

                google.maps.event.addListener(marker<c:out value="${rist.getId()}"/>, 'click', function () {
                    infoWindow<c:out value="${rist.getId()}"/>.open(map, marker<c:out value="${rist.getId()}"/>);
                });

            </c:forEach>

            }
        </script>
        <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
        <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA7spDhgAtLeyh6b0F6MQI2I5fldqrR6oM&callback=initMap"></script>
    </head>

    <body id="page-top" class="index" onload="initialize()">

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
                    <div class="col-md-12">
                        <div class="intro-text">
                            <span class="name"><c:out value="${ristorante.getName()}"/></span>
                            <hr class="star-light">
                            <div id="myCarousel" class="carousel slide" data-ride="carousel">
                                <!-- Indicators -->
                                <c:set var="first" value="${true}" scope="session"/>

                                <ol class="carousel-indicators">
                                    <li data-target="#myCarousel" data-slide-to="<c:out value="${i}"/>" class="active"></li>
                                        <c:forEach var="i" begin="1" end="${ristorante.getFoto().size()-1}">
                                        <li data-target="#myCarousel" data-slide-to="<c:out value="${i}"/>"></li>
                                        </c:forEach>
                                </ol>

                                <!-- Wrapper for slides -->
                                <div class="carousel-inner" role="listbox">
                                    <c:set var="first" value="${true}" scope="session"/>
                                    <c:forEach var="foto" items="${ristorante.getFoto()}">
                                        <div class="item <c:if test="${first}"> active<c:set var="first" value="${false}" scope="session"/></c:if>">
                                            <img class="peopleCarouselImg" src="<%= request.getContextPath()%><c:out value="${foto.getFotopath()}"/>" alt="Chania">
                                        </div>
                                    </c:forEach>

                                </div>

                                <!-- Left and right controls -->
                                <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
                                    <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                                    <span class="sr-only">Previous</span>
                                </a>
                                <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
                                    <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                                    <span class="sr-only">Next</span>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <!-- Ristorante info Section -->
        <section id="portfolio">
            <div class="container">
                <div class="row">
                    <div class="col-sm-4">
                        <div class="caption">
                            <div class="caption-content">
                                <label class="control-label"><a href="<c:out value="${ristorante.getLinksito()}"/>"><fmt:message key="web.site"/></a></label>
                                <br>
                                <label class="control-label"><fmt:message key="cooking.type"/>: <c:out value="${ristorante.getCucina()}"/></label>
                                <br>
                                <label class="control-label"><fmt:message key="economy.zone"/>: <c:out value="${ristorante.getFascia()}"/></label>
                                <br>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="caption">
                            <div class="caption-content">
                                <i><label class="control-label"><c:out value="${ristorante.getDescr()}"/></label></i>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="caption">
                            <div class="caption-content">
                                <label class="control-label"><fmt:message key="ranking"/>: <c:out value="${ristorante.getPosizioneClassifica()}"/></label>
                                <br>
                                <label class="control-label">
                                    <fmt:message key="users.vote"/>:
                                    <c:if test="${ristorante.getVoto() == 0.0}">
                                        <fmt:message key="no.vote"/>
                                    </c:if>
                                    <c:if test="${ristorante.getVoto() > 0}">
                                        <c:out value="${ristorante.getVoto()}"/>
                                    </c:if></label>
                                <br>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">

                    <br><br>
                    <div class="col-md-4">
                        <c:if test="${utente.isLogged()}">
                            <label class="control-label"><fmt:message key="actions.ristorante"/></label>
                            <br>
                            <label class="control-label"><a href="<%= request.getContextPath()%>/private/choose.jsp"><fmt:message key="add.foto"/></a></label>
                            <br>
                            <c:choose>
                                <c:when test="${utente.proprietario(ristorante)}">
                                    <label class="control-label"><a href="<%= request.getContextPath()%>/privateRistoratore/modificaRist.jsp"><fmt:message key="modify.restaurant"/></a></label>
                                    <br>
                                    <label class="control-label"><a href="<%= request.getContextPath()%>/privateRistoratore/orari.jsp"><fmt:message key="gestisci.orari"/></a></label>
                                    <br>
                                </c:when>
                                <c:otherwise>
                                    <label class="label-danger"><c:out value="${notMessage}"/></label>
                                    <c:if test="${!ristorante.reclamato()}">
                                        <label class="control-label"><fmt:message key="is.your.restaurant"/> <a href="<%= request.getContextPath()%>/private/ReclamaRistoranteServlet"><fmt:message key="reclaim"/></a></label>
                                        </c:if>
                                    <br>
                                    <c:if test="${!utente.justRecensito(ristorante)}">
                                        <label class="control-label"><fmt:message key="wanna.review"/> <a href="<%= request.getContextPath()%>/private/ConfiguraRecensioniServlet"><fmt:message key="click"/></a></label>
                                        </c:if>
                                    <label class="label-danger"><c:out value="${messConfiguraRecensioni}"/></label>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </div>

                    <div class="col-md-4">
                        <c:if test="${utente != null && !utente.justVotatoOggi(ristorante) && !utente.proprietario(ristorante)}">
                            <button class="btn btn-primary" onClick="visualizza('nomediv')"><fmt:message key="vote"/></button>
                            <br><br>
                            <div id="nomediv" hidden>
                                <form method="post" action="<%= request.getContextPath()%>/private/VotaRistoranteServlet?">
                                    <label><input type="radio" name="rating" value="1"> 1 |</label>
                                    <label><input type="radio" name="rating" value="2"> 2 |</label>
                                    <label><input type="radio" name="rating" value="3" checked> 3 |</label> 
                                    <label><input type="radio" name="rating" value="4"> 4 | </label>
                                    <label><input type="radio" name="rating" value="5"> 5</label>
                                    <br><br>
                                    <button class="btn btn-primary" type="submit"><fmt:message key="go"/></button>
                                </form>
                            </div>
                        </c:if>
                        <label class="label-danger"><c:out value="${errMessageVoto}"/></label>
                        <br>
                        <label class="control-label"><c:out value="${messageVoto}"/></label>
                    </div>
                    <div class="col-md-4">
                        <label class="control-label"><fmt:message key="orari.apertura"/></label>
                        <br>
                        <c:forEach var="days" items="${ristorante.getDays()}">
                            <label class="control-label">
                                <c:out value="${days.getGiornoString()}"/>:
                                <c:forEach var="times" items="${days.getTimes()}">
                                    <c:out value="${times.toString()}"/>,
                                </c:forEach>
                            </label>
                            <br>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </section>

        <!-- Mappa ristorante -->
        <section>
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <h2>Map</h2>
                        <hr class="star-primary">
                        <div id="gmaps-canvas"></div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Recensioni ristorante Section -->
        <section>
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <h2>Recensioni</h2>
                        <hr class="star-primary">
                    </div>
                </div>
                <c:set var="recensioni" value="${ristorante.getRecensioni()}"/>
                <c:choose>
                    <c:when test="${recensioni.size()<=0}">
                        <label class="control-form">Nessuna recensione disponibile</label>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="rec" items="${recensioni}">
                            <div class="row">
                                <hr>
                                <div class="col-md-4">
                                    <div class="caption">
                                        <div class="caption-content">
                                            <img src="<%= request.getContextPath()%><c:out value="${rec.getFotoPath()}"/>" alt=" This review has kein image "/>
                                            <br>
                                            <c:if test="${utente.proprietario(ristorante) && !rec.justSegnalato()}">
                                                <label class="control-form">
                                                    <a href="<%= request.getContextPath()%>/privateRistoratore/SegnalaFotoServlet?id_rec=<c:out value="${rec.getId()}"/>&type=rec">
                                                </label>
                                                <label class="label-warning"><fmt:message key="photo.report"/></label>
                                                </a>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-3">
                                    <div class="caption">
                                        <div class="caption-content">
                                            <label class="control-form"><c:out value="${rec.getUtente().getNomeCognome()}"/>(<c:out value="${rec.getUtente().getReputazione()}"/>) - <c:out value="${rec.getData()}"/></label>
                                            <br>
                                            <label class="control-form"><fmt:message key="voto"/>: 
                                                <c:choose>
                                                    <c:when test="${rec.getMediaVoti()==0}">
                                                        <fmt:message key="not.voted"/>
                                                    </c:when>
                                                    <c:otherwise><c:out value="${rec.getMediaVoti()}"/></c:otherwise>
                                                </c:choose>
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <div class="col-md-3">
                                    <div class="caption">
                                        <div class="caption-content">
                                            <label class="control-form"><fmt:message key="titolo"/></label>
                                            <br>
                                            <label class="control-form"><c:out value="${rec.getTitolo()}"/></label>
                                            <br>
                                            <label class="control-form"><fmt:message key="testo"/></label>
                                            <br>
                                            <label class="control-form"><i><c:out value="${rec.getTesto()}"/></i></label>
                                            <br><br>
                                            <label class="control-form"><fmt:message key="king.response"/></label>
                                            <br>
                                            <label class="control-form"><i><c:out value="${rec.getCommento()}"/></i></label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-2">
                                    <div class="caption">

                                        <div class="caption-content">
                                            <c:if test="${utente.proprietario(ristorante)}">
                                                <td>
                                                    <button type="submit" class="btn btn-primary" onClick="visualizza('nomediv<c:out value="${rec.getId()}"/>1')">
                                                        <c:choose>
                                                            <c:when test='${rec.getCommento() != null && !rec.getCommento().equals("")}'>
                                                                <fmt:message key="ri.comment"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <fmt:message key="comment"/>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </button>
                                                    <div id='nomediv<c:out value="${rec.getId()}"/>1' hidden>
                                                        <form method="post" action="<%= request.getContextPath()%>/private/InserisciCommentoServlet?id_rec=<c:out value="${rec.getId()}"/>">
                                                            <input type="text" name="commento" class="form-control"/>
                                                            <button class="btn btn-primary" type="submit"><fmt:message key="go"/></button>
                                                        </form>
                                                    </div>
                                                </c:if>

                                                <br><br>

                                                <c:if test="${utente != null && !utente.proprietario(rec)}">
                                                <td>
                                                    <button type="submit" value="Pulsante" onClick="visualizza('nomediv<c:out value="${rec.getId()}"/>2');"><fmt:message key="vote"/></button>
                                                    <div id='nomediv<c:out value="${rec.getId()}"/>2' hidden>
                                                        <form method="post" action="<%= request.getContextPath()%>/private/VotaRecensioneServlet?id_rec=<c:out value="${rec.getId()}"/>">
                                                            <label><input type="radio" name="rating" value="1"> 1 |</label>
                                                            <label><input type="radio" name="rating" value="2"> 2 |</label>
                                                            <label><input type="radio" name="rating" value="3" checked> 3 |</label> 
                                                            <label><input type="radio" name="rating" value="4"> 4 | </label>
                                                            <label><input type="radio" name="rating" value="5"> 5</label>
                                                            <br><br>
                                                            <button class="btn btn-primary" type="submit"><fmt:message key="go"/></button>
                                                        </form>
                                                    </div>
                                                </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
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
