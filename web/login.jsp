
<%@page import="DataBase.Utente"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>
        <c:out value="${title}"/>
    </title>

    <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
    
    <link href="<%= request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
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
					</ul>
                                                
                                      
                                    
					<ul class="nav navbar-nav navbar-right" style="padding-right: 30px;">
                                                <li>
                                                    <div class="dropdown">
                                                        <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Language
                                                        <span class=" "></span></button>
                                                        <ul class="dropdown-menu">
                                                            <li><a href="<%= request.getContextPath()%>/ConfigLingua">Italiano</a></li>
                                                            <li><a href="<%= request.getContextPath()%>/ConfigLingua">Inglese</a></li>
                                                        </ul>
                                                    </div>
                                                </li>
					</ul>
				</div>	
			</nav>
		</div>
	</div>
        <br><br>
	<div class="row" id="dividers_container" style="background: transparent">
            <div class="col-md-12" id="regdiv">
                <br>
                <div class="regindiv">
                    <form method="POST" action="LoginServlet">

                        <h2>Login</h2>
                        <p><font class="errMessage"><c:out value="${message}"/></font></p>
                        <table>
                            <tr>
                                <td style="padding: 10px" class="txtreg">Email</td>
                                <td style="padding: 10px"><input type="text" name="mail" class="form-control"></td>
                            </tr>
                            <tr>
                                <td style="padding: 10px" class="txtreg">Password</td>
                                <td style="padding: 10px"><input type="password" name="password" class="form-control"></td>
                            </tr>
                            
                        </table>
                        <input type="submit" id="user" class="btn btn-warning"> <input type="reset" id="user" class="btn btn-warning" value="Annulla" onclick="history.go(-1);">
                    </form>
                </div>
            </div>
	</div>
</div>

    <script src="<%= request.getContextPath()%>/js/jquery.min.js"></script>
    <script src="<%= request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script src="<%= request.getContextPath()%>/js/scripts.js"></script>
  </body>
</html>