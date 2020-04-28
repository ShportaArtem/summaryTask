<%@ include file="/view/jspf/page.jspf"%>
<%@ include file="/view/jspf/taglib.jspf"%>
<!doctype html>
<html lang="en">
	<%@ include file="/view/jspf/head.jspf"%>
<body class="bg-secondary">
<c:choose>
<c:when test="${sessionScope.methodRequests==1 }">
	<%@ include file="/view/jspf/header2.jspf"%>
	</c:when>
	<c:when test="${sessionScope.methodRequests==2 }">
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
      <th scope="col"><fmt:message key="driverRequest.shippingId"/></th>
      <th scope="col"><fmt:message key="driverRequest.driverlogin"/></th>
      <th scope="col"><fmt:message key="car.carryingCapacity"/></th>
      <th scope="col"><fmt:message key="car.passengersCapacity"/></th>
      <th scope="col"><fmt:message key="car.vehicleCondition"/></th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="request" items="${sessionScope.requestsView}">
	<tr>
      <th scope="row">${request.getId()}</th>
      <td>${request.getShippingId()}</td>
      <td>${request.getDriverLogin()}</td>
      <td>${request.getCarryinfCapacity()}</td>
      <td>${request.getPassangersCapacity()}</td>
      <td>${request.getVehicleCondition()}</td>
    </tr>
	</c:forEach>
  </tbody>
</table>
</div>
	<div class="col-sm">
		<div class="btn-group">
		<c:choose>
		<c:when test="${sessionScope.chooseRequestStage == 1 }">
		<a class="btn btn-outline-success" href="/Autobase/Controller?command=openChooseCar"
					data-toggle="tooltip bg-dark" data-placement="bottom" ><fmt:message key="button.chooseCar"/></a>
  			</c:when>
  			<c:when test="${sessionScope.chooseRequestStage == 2}">
  			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-success dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.choose"/>
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="request" items="${sessionScope.requests}">
						<a class="dropdown-item" href="/Autobase/Controller?command=chooseRequest&requestId=${request.getId()}">${request.getId()}</a>
					</c:forEach>
    			</div>
  			</div>
  			</c:when>
  			</c:choose>
  		</div>
  	</div>
</div>
</div>
	<%@ include file="/view/jspf/footer.jspf"%>
	<%@ include file="/view/jspf/bodyScripts.jspf"%>
</body>

</html>