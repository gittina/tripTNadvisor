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
        <c:set value="/result.jsp" scope="session" var="lastPage"/>

        <!-- Bootstrap Core CSS -->
        <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <!-- Theme CSS -->
        <link href="css/freelancer.min.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet" type="text/css">

        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
        <script type="text/javascript" src="scripts/jquery-1.8.2.min.js"></script>
        <script type="text/javascript" src="scripts/jquery.mockjax.js"></script>
        <script type="text/javascript" src="src/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="autocomplete.txt"></script>
        <script type="text/javascript" src="scripts/demo.js"></script>

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
                                <img src="<fmt:message key="bandiera"/>" alt="- "/>
                                <fmt:message key="language"/>
                                <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=en_GB"><img src="img/flags/GB.png"/><fmt:message key="english"/></a></li>
                                <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=it_IT"><img src="img/flags/IT.png"/><fmt:message key="italian"/></a></li>
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

                    <form action="<%= request.getContextPath()%>/OrdinaServlet" method="POST">
                        <div class="col-md-3">
                            Ordina per: 
                            <select class="form-group selectBar" name="tipo">
                                <option value="NoOrdine">NessunOrdine</option>
                                <option value="pos">Posizione in classifica</option>
                                <option value="pre">Fascia di prezzo</option>
                                <option value="alf">Alfabeticamente</option>
                            </select><br>
                            <input type="radio" name="ordine" value="1" checked>Crescente
                            <input type="radio" name="ordine" value="2">Decrescente
                        </div>
                        
                        <div class="col-md-3">
                            Filtra per fascia:
                            <select class="form-group selectBar" name="fascia">
                                <option value="TuttiFascia">Tutti</option>
                                <option value="Economico">Economico</option>
                                <option value="Normale">Normale</option>
                                <option value="Lussuoso">Lussuoso</option>                                                    
                            </select>
                        </div>
                        
                        <div class="col-md-3">
                            Filtra per specialità
                            <select class="form-group selectBar" name="spec">
                                <option value="TuttiSpec">Tutti</option>
                                <option value="Ristorante">Ristorante</option>
                                <option value="Pizzeria">Pizzeria</option>
                                <option value="Trattoria">Trattoria</option>
                                <option value="Polleria">Polleria</option>
                                <option value="Chinese">Chinese</option>
                                <option value="Japanese">Japanese</option>                                                    
                            </select>
                        </div>
                        
                        <div class="col-md-3">
                            <button class="btn btn-primary" type="submit">Filtra e Ordina</button>
                        </div>
                    </form>
                </div>
            </div>
        </header>

        <!-- Portfolio Grid Section -->
        <section>
            <div class="container">
                <c:forEach var="ristorante" items="${result}">
                    <div class="row">
                        <div class="col-md-4">
                            <a href="">
                                <img src="<%= request.getContextPath()%><c:out value="${ristorante.getFoto().get(0).getFotopath()}"/>" class="img-responsive" alt="">
                            </a>
                        </div>
                        <div class="col-md-4">
                            <a href="<%= request.getContextPath()%>/ConfigurazioneRistorante?id_rist=<c:out value="${ristorante.getId()}"/>">
                                <c:out value="${ristorante.getName()}"/>
                            </a>
                            <br>
                            <fmt:message key="fascia"/>: <c:out value="${ristorante.getFascia()}"/><br>
                            <fmt:message key="cucina"/>: <c:out value="${ristorante.getCucina()}"/><br>
                            <fmt:message key="voto"/>: <c:out value="${ristorante.getVoto()}"/><br>
                            <fmt:message key="posClass"/>: <c:out value="${ristorante.getPosizioneClassifica()}"/><br>
                        </div>
                        <div class="col-md-4">
                            <img src="<%= request.getContextPath()%><c:out value="${ristorante.creaQR()}"/>" class="img-responsive" alt="" width="250" height="250">
                        </div>

                    </div>
                        <div class="row"><hr></div>
                </c:forEach>
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

        <!-- Portfolio Modals --><!--
        <div class="portfolio-modal modal fade" id="portfolioModal1" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-content">
                <div class="close-modal" data-dismiss="modal">
                    <div class="lr">
                        <div class="rl">
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="col-lg-8 col-lg-offset-2">
                            <div class="modal-body">
                                <h2>Project Title</h2>
                                <hr class="star-primary">
                                <img src="img/portfolio/cabin.png" class="img-responsive img-centered" alt="">
                                <p>Use this area of the page to describe your project. The icon above is part of a free icon set by <a href="https://sellfy.com/p/8Q9P/jV3VZ/">Flat Icons</a>. On their website, you can download their free set with 16 icons, or you can purchase the entire set with 146 icons for only $12!</p>
                                <ul class="list-inline item-details">
                                    <li>Client:
                                        <strong><a href="http://startbootstrap.com">Start Bootstrap</a>
                                        </strong>
                                    </li>
                                    <li>Date:
                                        <strong><a href="http://startbootstrap.com">April 2014</a>
                                        </strong>
                                    </li>
                                    <li>Service:
                                        <strong><a href="http://startbootstrap.com">Web Development</a>
                                        </strong>
                                    </li>
                                </ul>
                                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="portfolio-modal modal fade" id="portfolioModal2" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-content">
                <div class="close-modal" data-dismiss="modal">
                    <div class="lr">
                        <div class="rl">
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="col-lg-8 col-lg-offset-2">
                            <div class="modal-body">
                                <h2>Project Title</h2>
                                <hr class="star-primary">
                                <img src="img/portfolio/cake.png" class="img-responsive img-centered" alt="">
                                <p>Use this area of the page to describe your project. The icon above is part of a free icon set by <a href="https://sellfy.com/p/8Q9P/jV3VZ/">Flat Icons</a>. On their website, you can download their free set with 16 icons, or you can purchase the entire set with 146 icons for only $12!</p>
                                <ul class="list-inline item-details">
                                    <li>Client:
                                        <strong><a href="http://startbootstrap.com">Start Bootstrap</a>
                                        </strong>
                                    </li>
                                    <li>Date:
                                        <strong><a href="http://startbootstrap.com">April 2014</a>
                                        </strong>
                                    </li>
                                    <li>Service:
                                        <strong><a href="http://startbootstrap.com">Web Development</a>
                                        </strong>
                                    </li>
                                </ul>
                                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="portfolio-modal modal fade" id="portfolioModal3" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-content">
                <div class="close-modal" data-dismiss="modal">
                    <div class="lr">
                        <div class="rl">
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="col-lg-8 col-lg-offset-2">
                            <div class="modal-body">
                                <h2>Project Title</h2>
                                <hr class="star-primary">
                                <img src="img/portfolio/circus.png" class="img-responsive img-centered" alt="">
                                <p>Use this area of the page to describe your project. The icon above is part of a free icon set by <a href="https://sellfy.com/p/8Q9P/jV3VZ/">Flat Icons</a>. On their website, you can download their free set with 16 icons, or you can purchase the entire set with 146 icons for only $12!</p>
                                <ul class="list-inline item-details">
                                    <li>Client:
                                        <strong><a href="http://startbootstrap.com">Start Bootstrap</a>
                                        </strong>
                                    </li>
                                    <li>Date:
                                        <strong><a href="http://startbootstrap.com">April 2014</a>
                                        </strong>
                                    </li>
                                    <li>Service:
                                        <strong><a href="http://startbootstrap.com">Web Development</a>
                                        </strong>
                                    </li>
                                </ul>
                                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="portfolio-modal modal fade" id="portfolioModal4" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-content">
                <div class="close-modal" data-dismiss="modal">
                    <div class="lr">
                        <div class="rl">
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="col-lg-8 col-lg-offset-2">
                            <div class="modal-body">
                                <h2>Project Title</h2>
                                <hr class="star-primary">
                                <img src="img/portfolio/game.png" class="img-responsive img-centered" alt="">
                                <p>Use this area of the page to describe your project. The icon above is part of a free icon set by <a href="https://sellfy.com/p/8Q9P/jV3VZ/">Flat Icons</a>. On their website, you can download their free set with 16 icons, or you can purchase the entire set with 146 icons for only $12!</p>
                                <ul class="list-inline item-details">
                                    <li>Client:
                                        <strong><a href="http://startbootstrap.com">Start Bootstrap</a>
                                        </strong>
                                    </li>
                                    <li>Date:
                                        <strong><a href="http://startbootstrap.com">April 2014</a>
                                        </strong>
                                    </li>
                                    <li>Service:
                                        <strong><a href="http://startbootstrap.com">Web Development</a>
                                        </strong>
                                    </li>
                                </ul>
                                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="portfolio-modal modal fade" id="portfolioModal5" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-content">
                <div class="close-modal" data-dismiss="modal">
                    <div class="lr">
                        <div class="rl">
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="col-lg-8 col-lg-offset-2">
                            <div class="modal-body">
                                <h2>Project Title</h2>
                                <hr class="star-primary">
                                <img src="img/portfolio/safe.png" class="img-responsive img-centered" alt="">
                                <p>Use this area of the page to describe your project. The icon above is part of a free icon set by <a href="https://sellfy.com/p/8Q9P/jV3VZ/">Flat Icons</a>. On their website, you can download their free set with 16 icons, or you can purchase the entire set with 146 icons for only $12!</p>
                                <ul class="list-inline item-details">
                                    <li>Client:
                                        <strong><a href="http://startbootstrap.com">Start Bootstrap</a>
                                        </strong>
                                    </li>
                                    <li>Date:
                                        <strong><a href="http://startbootstrap.com">April 2014</a>
                                        </strong>
                                    </li>
                                    <li>Service:
                                        <strong><a href="http://startbootstrap.com">Web Development</a>
                                        </strong>
                                    </li>
                                </ul>
                                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="portfolio-modal modal fade" id="portfolioModal6" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-content">
                <div class="close-modal" data-dismiss="modal">
                    <div class="lr">
                        <div class="rl">
                        </div>
                    </div>
                </div>
                <div class="container">
                    <div class="row">
                        <div class="col-lg-8 col-lg-offset-2">
                            <div class="modal-body">
                                <h2>Project Title</h2>
                                <hr class="star-primary">
                                <img src="img/portfolio/submarine.png" class="img-responsive img-centered" alt="">
                                <p>Use this area of the page to describe your project. The icon above is part of a free icon set by <a href="https://sellfy.com/p/8Q9P/jV3VZ/">Flat Icons</a>. On their website, you can download their free set with 16 icons, or you can purchase the entire set with 146 icons for only $12!</p>
                                <ul class="list-inline item-details">
                                    <li>Client:
                                        <strong><a href="http://startbootstrap.com">Start Bootstrap</a>
                                        </strong>
                                    </li>
                                    <li>Date:
                                        <strong><a href="http://startbootstrap.com">April 2014</a>
                                        </strong>
                                    </li>
                                    <li>Service:
                                        <strong><a href="http://startbootstrap.com">Web Development</a>
                                        </strong>
                                    </li>
                                </ul>
                                <button type="button" class="btn btn-default" data-dismiss="modal"><i class="fa fa-times"></i> Close</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        -->

        <!-- jQuery -->
        <script src="vendor/jquery/jquery.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="vendor/bootstrap/js/bootstrap.min.js"></script>

        <!-- Plugin JavaScript -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>

        <!-- Theme JavaScript -->
        <script src="js/freelancer.min.js"></script>

    </body>

</html>
