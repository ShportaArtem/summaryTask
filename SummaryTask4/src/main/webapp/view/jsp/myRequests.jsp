<%@ include file="/view/jspf/page.jspf"%>
<%@ include file="/view/jspf/taglib.jspf"%>
<!doctype html>
<html lang="en">
	<%@ include file="/view/jspf/head.jspf"%>
<body class="bg-secondary">
<c:choose>
<c:when test="${sessionScope.methodMyRequests==1 }">
	<%@ include file="/view/jspf/header2.jspf"%>
	</c:when>
	<c:when test="${sessionScope.methodMyRueqests==2 }">
	<%@ include file="/view/jspf/header.jspf"%>
	</c:when>
</c:choose>
<div class="container-fluid">


	<c:choose>
    	<c:when test="${sessionScope.myRequestNow !=null }">
    	<div class="row"><hr></div>
		<div class="row">
    	<div class="container-fluid text-align-center text-light"><h3>Now</h3></div>
    	<div class="col-lg-9">
    	
    	<table class="table table-bordered table-dark table-striped ">
  <thead class="thead-light">
    <tr>
      <th scope="col">#</th>
      <th scope="col"><fmt:message key="driverRequest.shippingId"/></th>
      <th scope="col"><fmt:message key="car.carryingCapacity"/></th>
      <th scope="col"><fmt:message key="car.passengersCapacity"/></th>
      <th scope="col"><fmt:message key="car.vehicleCondition"/></th>
    </tr>
  </thead>
  <tbody>
	<tr>
      <th scope="row">${myRequestNow.getId()}</th>
      <td>${myRequestNow.getShippingId()}</td>
      <td>${myRequestNow.getCarryinfCapacity()}</td>
      <td>${myRequestNow.getPassangersCapacity()}</td>
      <td>${myRequestNow.getVehicleCondition()}</td>
    </tr>
  </tbody>
</table>
    	</div>
    	<div class="col-sm">
		<div class="btn-group">
			<a class="btn btn-outline-success" href="/Autobase/Controller?command=openFinishFligth"
					data-toggle="tooltip bg-dark" data-placement="bottom" >Finish</a>  			
  		</div>
  	</div>
    	 </div>
    	</c:when>
    </c:choose>
 
  <div class="row"><hr></div>
  <div class="row">
  <div class="container-fluid text-align-center text-light"><h3>All</h3></div>
  <div class="col-lg-9">
	<table class="table table-bordered table-dark table-striped ">
  <thead class="thead-light">
    <tr>
      <th scope="col">#</th>
      <th scope="col"><fmt:message key="driverRequest.shippingId"/></th>
      <th scope="col"><fmt:message key="car.carryingCapacity"/></th>
      <th scope="col"><fmt:message key="car.passengersCapacity"/></th>
      <th scope="col"><fmt:message key="car.vehicleCondition"/></th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="request" items="${sessionScope.myRequests}">
	<tr>
      <th scope="row">${request.getId()}</th>
      <td>${request.getShippingId()}</td>
      <td>${request.getCarryinfCapacity()}</td>
      <td>${request.getPassangersCapacity()}</td>
      <td>${request.getVehicleCondition()}</td>
    </tr>
	</c:forEach>
  </tbody>
</table>
</div>
 <c:choose>
    <c:when test="${sessionScope.userRole == '2' }">
	
	<div class="col-sm">
		<div class="btn-group">
  			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-danger dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.delete"/>
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="request" items="${sessionScope.myRequests}">
						<a class="dropdown-item" href="/Autobase/Controller?command=deleteMyRequest&myRequestId=${request.getId()}">${request.getId()}</a>
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