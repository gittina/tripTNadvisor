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
        <c:set value="/privateRistoratore/modificaRist.jsp" scope="session" var="lastPage"/>
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
                                        <c:when test="${utente == null}">
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
                                                            <input name="username" id="username" type="text" placeholder="Mail" > 
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


                    <div class="jumbotron well" style="margin-top: 50px; background-image: url(<%= request.getContextPath()%>/img/sfondo2.jpg)">
                        <div id="jumbosearch">
                            <h2>
                                <fmt:message key="page.edit.restaurant"/> <c:out value="${ristorante.getName()}"/>
                            </h2>
                            <h3>
                                <fmt:message key="let.empty.field"/><br><br>
                            </h3>
                            <c:if test="${not empty message}">
                                <br>
                                <label class="label label-warning">
                                <c:out value="${message}"/>
                                </font>
                            </c:if>

                            <form method="POST" action="<%= request.getContextPath()%>/privateRistoratore/ModificaRistoranteServlet">
                                <table style=" align-self: center">
                                    <tr>
                                        <td>
                                            <font class="descrMessage"><fmt:message key="insert.new.name"/></font>
                                            <input name="nome" type="text" placeholder="Nuovo nome" /> 
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <font class="descrMessage"><fmt:message key="insert.new.address"/></font>
                                            <input name="address" type="text" placeholder="Nuovo indirizzo" /> 
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <font class="descrMessage"><fmt:message key="insert.new.website"/></font>
                                            <input name="linksito" type="text" placeholder="Nuovo sito web" /> 
                                        </td>
                                    </tr> 
                                    <tr>
                                        <td>
                                            <font class="descrMessage"><fmt:message key="insert.new.description"/></font>
                                            <input name="descr" type="text" placeholder="Nuova descizione"/><br>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <font class="descrMessage"><fmt:message key="insert.new.specialty"/></font>
                                            <select class="form-control" style="width: 100px" name="cucina">
                                                <option value="nothing"><fmt:message key="no.edit"/></option>
                                                <option value="Ristorante"><fmt:message key="restaurant"/></option>
                                                <option value="Pizzeria"><fmt:message key="pizzeria"/></option>
                                                <option value="Trattoria"><fmt:message key="tavern"/></option>
                                                <option value="Polleria"><fmt:message key="polleria"/></option>
                                                <option value="Chinese"><fmt:message key="chinese"/></option>
                                                <option value="Japanese"><fmt:message key="japanese"/></option>                                                    
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <font class="descrMessage"><fmt:message key="insert.new.economyzone"/></font>
                                            <select class="form-control" style="width: 100px" name="fascia">
                                                <option value="nothing"><fmt:message key="no.edit"/></option>
                                                <option value="Economico"><fmt:message key="economy"/></option>
                                                <option value="Normale"><fmt:message key="normal"/></option>
                                                <option value="Lussuoso"><fmt:message key="luxury"/></option>                                                    
                                            </select>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td>
                                            <button type='submit' ><fmt:message key="submit"/></button>
                                        </td>
                                    </tr>

                                </table>
                            </form>

                        </div>

                    </div>

                </div>
            </div>
        </div>
        <script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
        <script src="<%= request.getContextPath()%>/js/bootstrap.min.js"></script>
        <script src="<%= request.getContextPath()%>/js/scripts.js"></script>
    </body>
</html>