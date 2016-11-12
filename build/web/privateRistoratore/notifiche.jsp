<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${lan.getLanSelected()}" />
<fmt:setBundle basename="Resources.string" />
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>
            <c:out value="${title}"/>
        </title>
        <c:set value="/privateRistoratore/notifiche.jsp" scope="session" var="lastPage"/>
        <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>

        <link href="<%= request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
        <link href="<%= request.getContextPath()%>/css/style.css" rel="stylesheet">
        <link href='https://fonts.googleapis.com/css?family=Open+Sans:600,400' rel='stylesheet' type='text/css'>

    </head>
    <body>

        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
                        <!--
                            Da qui in poi inizia la navbar in posizione TOP.
                        - navbar-header sempre visibile
                        - collapse navbar-collapse rientra in un menù a cascata se la pagina è troppo stretta e contiene i prossimi punti
                            - li active sono scritte normali (o link)
                            - li dropdown (class) sono i dropdown, contenenti nell'ul altri li che sono i link visibili all'interno del menù
                        -->

                        <div class="navbar-header">

                            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                                <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
                            </button> <a class="navbar-brand" href="<%= request.getContextPath()%>/HomeServlet"><img src="<%= request.getContextPath()%>/img/logo.png" width="110" style="margin-top: -5px; margin-right: -25px; padding: 0"></a>
                        </div>

                        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1"> 
                            <ul class="nav navbar-nav">
                                <li class="home" id="homelink">
                                    <a href="<%= request.getContextPath()%>/HomeServlet">Home</a>
                                </li>
                                <li>

                                    <c:choose>
                                        <c:when test="${utente==null}">
                                            <a href="<%= request.getContextPath()%>/registration.jsp"><fmt:message key="welcome.visitors"/></a>
                                        </c:when>
                                        <c:when test="${utente.isAmministratore()}">
                                            <div class="dropdown">
                                                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"> <img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                                    <span class="caret"></span></button>
                                                <ul class="dropdown-menu">
                                                    <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><fmt:message key="profile"/></a></li>
                                                    <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                                </ul>
                                            </div>
                                        </c:when>
                                        <c:when test="${utente.isRegistrato()}">
                                            <div class="dropdown">
                                                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"> <img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                                    <span class="caret"></span></button>
                                                <ul class="dropdown-menu">
                                                    <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><fmt:message key="profile"/></a></li>
                                                    <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneAddRistorante"><fmt:message key="add.restaurant"/></a></li>
                                                    <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                                </ul>
                                            </div>      
                                        </c:when>
                                        <c:when test="${utente.isRistoratore()}">
                                            <div class="dropdown">
                                                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"> <img src="<%= request.getContextPath()%><c:out value="${utente.getAvpath()}"/>" HEIGHT="25" WIDTH="25" BORDER="0" align="center">  <c:out value="${utente.getNomeCognome()}"/>
                                                    <span class="caret"></span></button>
                                                <ul class="dropdown-menu">
                                                    <li><a href="<%= request.getContextPath()%>/private/ConfigurazioneProfilo"><fmt:message key="profile"/></a></li>
                                                    <li><a href="<%= request.getContextPath()%>/privateRistoratore/ConfigurazioneRistoranti"><fmt:message key="my.restaurant"/></a></li>
                                                    <li><a href="<%= request.getContextPath()%>/private/LogoutServlet"><fmt:message key="exit"/></a></li>
                                                </ul>
                                            </div>      
                                        </c:when>
                                    </c:choose>

                                </li>
                            </ul>

                            <form class="navbar-form navbar-left" role="search" action="<%= request.getContextPath()%>/SearchServlet?tipo=Simple" method="POST">
                                <div class="form-group">
                                    <input type="text" class="form-control input-sm" placeholder="Search" name="research">
                                </div>
                                <button type="submit" class="btn btn-sm"><fmt:message key="submit"/></button>
                            </form>

                            <ul class="nav navbar-nav navbar-right" style="padding-right: 30px;">

                                <c:choose>
                                    <c:when test="${utente==null}">
                                        <li>
                                            <div class="dropdown">
                                                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"> <fmt:message key="login"/>
                                                    <span class="caret"></span></button>
                                                <ul class="dropdown-menu">
                                                    <li><form method="POST" action="<%= request.getContextPath()%>/LoginServlet"> 
                                                            <input name="username" id="username" type="text" placeholder="Username" > 
                                                            <input name="password" id="password" type="password" placeholder="Password"><br>
                                                            <button type="submit" class="btn btn-warning"><fmt:message key="login"/></button>
                                                        </form>
                                                    </li>
                                                </ul>
                                            </div>
                                        </li>
                                        <li><a href="<%= request.getContextPath()%>/registration.jsp"><fmt:message key="register"/></a></li>
                                        </c:when>
                                        <c:when test="${utente.isRistoratore() || utente.isAmministratore()}">
                                        <li>
                                            <div class="dropdown">
                                                <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><a href="<%=request.getContextPath()%>/ConfigurazioneNotifiche"><fmt:message key="notify"/></a>
                                                    <span class="caret"></span></button>
                                                <ul class="dropdown-menu">
                                                    <!-- lista delle notifiche -->
                                                </ul>
                                            </div>
                                        </li>
                                    </c:when>
                                </c:choose>  
                                <li>
                                    <div class="dropdown">
                                        <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown"><fmt:message key="language"/>
                                            <span class=" "></span></button>
                                        <ul class="dropdown-menu">
                                            <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=it_IT"><fmt:message key="italian"/></a></li>
                                            <li><a href="<%= request.getContextPath()%>/ConfigLingua?l=en_GB"><fmt:message key="english"/></a></li>
                                        </ul>
                                    </div>
                                </li>
                            </ul>
                        </div>	
                    </nav>              

                </div>
            </div>


        </div>
        <br>

        <div class="row" id="dividers_container" >
            <div class="col-md-12" style="padding-top: 65px;">
                <c:out value="${errMessageNotifica}"/>
                <c:forEach var="notif" items="${notifiche}">
                    <div class="row jumbotron" style="border-radius: 10px; margin: 20px; padding-left: 20px; background-color: #ffffff">
                        <div class="col-md-12">

                            <div class="row">

                                <div class="col-md-4">
                                    <p><c:out value="${notif.getData()}"/></p>
                                    <p><c:out value="${notif.toString()}"/></p>
                                </div>
                                <c:if test="${notif.isNotFoto()}">
                                    <div class="col-md-4">
                                        <img src="<%= request.getContextPath()%><c:out value="${notif.getFotoPath()}"/>" alt="<fmt:message key="no.image"/>">
                                    </div>
                                </c:if>
                                <div class="col-md-4">
                                    <a href="<%= request.getContextPath()%>/privateAdministrator/ApplicaNotificaServlet?accept=true&id_not=<c:out value="${notif.getId()}"/>"><button><fmt:message key="accept"/></button></a>
                                    <a href="<%= request.getContextPath()%>/privateAdministrator/ApplicaNotificaServlet?accept=false&id_not=<c:out value="${notif.getId()}"/>"><button><fmt:message key="regret"/></button></a>
                                </div>

                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>


        <script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
        <script src="<%= request.getContextPath()%>/js/bootstrap.min.js"></script>
        <script src="<%= request.getContextPath()%>/js/scripts.js"></script>
    </body>
</html>