<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <!DOCTYPE html>
 <html>
    <head>
		<c:if test="${sendRedirect}">
			<script type="text/javascript">
				alert(top.location.href);
				top.location.href='https://www.facebook.com/dialog/oauth?client_id=244853175563795&redirect_uri=http://apps.facebook.com/jssimpledemo/&scope=email,user_checkins,friends_checkins'
			</script>
		</c:if>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<link rel="stylesheet" href="http://twitter.github.com/bootstrap/1.3.0/bootstrap.min.css">
    </head>
    <body>
  		<div class="container" style="max-width: 720px">
		      <script src="js/jquery-1.6.2.min.js">
		      </script>
		      <br><br>
		      <h3>Places Where You've Checked In</h3>
		      <table class="zebra-striped">
		          <tr><th>Name</th></tr>
			      <c:forEach var="checkin" items="${checkIns}">
			      	<tr>
			      		<td>${checkin.place.name}</td>
			      	</tr>
			      </c:forEach>
		      </table>
	  	</div>
    </body>
 </html>
 
 
 
 
 
 
 
 
 
 
