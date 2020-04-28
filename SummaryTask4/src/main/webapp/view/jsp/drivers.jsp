<%@ include file="/view/jspf/page.jspf"%>
<%@ include file="/view/jspf/taglib.jspf"%>
<!doctype html>
<html lang="en">
	<%@ include file="/view/jspf/head.jspf"%>
<body class="bg-secondary">
<c:choose>
<c:when test="${sessionScope.methodDriver==1 }">
	<%@ include file="/view/jspf/header2.jspf"%>
	</c:when>
	<c:when test="${sessionScope.methodDriver==2 }">
	<%@ include file="/view/jspf/header.jspf"%>
	</c:when>
</c:choose>
<div class="container-fluid">
<div class="row"><hr></div>
<div class="row">
<div class="col-lg-9">
	<table class="table table-bordered table-dark table-striped ">
  <thead class="thead-light">
    <tr>
      <th scope="col">#</th>
     <th scope="col"><fmt:message key="user.login" /></th>
      <th scope="col"><fmt:message key="user.password" /></th>
      <th scope="col"><fmt:message key="user.name" /></th>
      <th scope="col"><fmt:message key="user.surname" /></th>
      <th scope="col"><fmt:message key="driver.passport" /></th>
      <th scope="col"><fmt:message key="driver.phone" /></th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="driver" items="${sessionScope.driverViews}">
	<tr>
      <th scope="row">${driver.getId()}</th>
      <td>${driver.getLogin()}</td>
      <td>${driver.getPassword()}</td>
      <td>${driver.getName()}</td>
      <td>${driver.getSurname()}</td>
      <td>${driver.getPassport()}</td>
      <td>${driver.getPhone()}</td>
    </tr>
	</c:forEach>
  </tbody>
</table>
</div>
 <c:choose>
    <c:when test="${sessionScope.userRole == '1' }">
	
	<div class="col-sm">
		<div class="btn-group">
			<a class="btn btn-outline-success" href="/Autobase/Controller?command=openAddDriver"
					data-toggle="tooltip bg-dark" data-placement="bottom" ><fmt:message key="button.add" /></a>
					
			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-info dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.update" />
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="driver" items="${sessionScope.driverViews}">
						<a class="dropdown-item" href="/Autobase/Controller?command=openUpdateDriver&driverLogin=${driver.getLogin()}">${driver.getLogin()}</a>
					</c:forEach>
    			</div>
  			</div>
  			
  			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-danger dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.delete" />
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="driver" items="${sessionScope.driversNotUsed}">
						<a class="dropdown-item" href="/Autobase/Controller?command=deleteDriver&driverLogin=${driver.getLogin()}">${driver.getLogin()}</a>
					</c:forEach>
    			</div>
  			</div>
  			
  		</div>
  	</div>
	</c:when>
	</c:choose>
</div>
</div>
	<%@ include file="/view/jspf/footer.jspf"%>
	<%@ include file="/view/jspf/bodyScripts.jspf"%>
</body>

</html>