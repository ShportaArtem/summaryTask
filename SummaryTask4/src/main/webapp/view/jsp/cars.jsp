<%@ include file="/view/jspf/page.jspf"%>
<%@ include file="/view/jspf/taglib.jspf"%>
<!doctype html>
<html lang="en">
	<%@ include file="/view/jspf/head.jspf"%>
<body class="bg-secondary">
<c:choose>
<c:when test="${sessionScope.methodCar==1 }">
	<%@ include file="/view/jspf/header2.jspf"%>
	</c:when>
	<c:when test="${sessionScope.methodCar==2 }">
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
      <th scope="col"><fmt:message key="car.model" /></th>
      <th scope="col"><fmt:message key="car.firm" /></th>
      <th scope="col"><fmt:message key="car.carryingCapacity" /></th>
      <th scope="col"><fmt:message key="car.passengersCapacity" /></th>
      <th scope="col"><fmt:message key="car.status" /></th>
      <th scope="col"><fmt:message key="car.vehicleCondition" /></th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="car" items="${sessionScope.carViews}">
	<tr>
      <th scope="row">${car.getId()}</th>
      <td>${car.getModel()}</td>
      <td>${car.getFirmName()}</td>
      <td>${car.getCarryingCapacity()}</td>
      <td>${car.getPassangersCapacity()}</td>
      <td>${car.getStatus()}</td>
      <td>${car.getVehicleCondition()}</td>
    </tr>
	</c:forEach>
  </tbody>
</table>
</div>
 <c:choose>
    <c:when test="${sessionScope.userRole != '2' }">
	
	<div class="col-sm">
		<div class="btn-group">
			<a class="btn btn-outline-success" href="/Autobase/Controller?command=openAddCar"
					data-toggle="tooltip bg-dark" data-placement="bottom" ><fmt:message key="button.add" /></a>
					
			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-info dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.update" />
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="car" items="${sessionScope.carViews}">
						<a class="dropdown-item" href="/Autobase/Controller?command=openUpdateCar&carId=${car.getId()}">${car.getId()}</a>
					</c:forEach>
    			</div>
  			</div>
  			
  			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-danger dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.delete" />
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="car" items="${sessionScope.carsNotUsed}">
						<a class="dropdown-item" href="/Autobase/Controller?command=deleteCar&carId=${car.getId()}">${car.getId()}</a>
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