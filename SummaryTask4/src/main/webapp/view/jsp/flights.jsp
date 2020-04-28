<%@ include file="/view/jspf/page.jspf"%>
<%@ include file="/view/jspf/taglib.jspf"%>
<%@ taglib uri="/WEB-INF/sort.tld" prefix="my" %>
<!doctype html>
<html>
	<%@ include file="/view/jspf/head.jspf"%>
<body class="bg-secondary">
<c:choose>
<c:when test="${sessionScope.methodFlight==1 }">
	<%@ include file="/view/jspf/header2.jspf"%>
	</c:when>
	<c:when test="${sessionScope.methodFlight==2 }">
	<%@ include file="/view/jspf/header.jspf"%>
	</c:when>
</c:choose>
<div class="container-fluid">
<div class="row">
<div class="col-sm">
<div class="alert alert-warning"><fmt:message key="label.sortBy"/>
<div class="btn-group">
			<a class="btn btn-outline-primary" href="/Autobase/Controller?command=getFlights&sortType=id"
					data-toggle="tooltip bg-dark" data-placement="bottom" ><fmt:message key="sort.number"/></a>
			<a class="btn btn-outline-primary" href="/Autobase/Controller?command=getFlights&sortType=createTime"
					data-toggle="tooltip bg-dark" data-placement="bottom" ><fmt:message key="sort.creationTime"/></a>
			<a class="btn btn-outline-primary" href="/Autobase/Controller?command=getFlights&sortType=status"
					data-toggle="tooltip bg-dark" data-placement="bottom" ><fmt:message key="sort.status"/></a>				
	</div></div>
	</div>
</div>
<div class="row"><hr></div>
<div class="row">
<div class="col-lg-9">
	<table class="table table-bordered table-dark table-striped ">
  <thead class="thead-light">
    <tr>
      <th scope="col">#</th>
      <th scope="col"><fmt:message key="flight.dispatcher"/></th>
      <th scope="col"><fmt:message key="flight.status"/></th>
      <th scope="col"><fmt:message key="flight.driver"/></th>
      <th scope="col"><fmt:message key="flight.car"/></th>
      <th scope="col"><fmt:message key="flight.arrivalCity"/></th>
      <th scope="col"><fmt:message key="flight.departureCity"/></th>
      <th scope="col"><fmt:message key="flight.departureTime"/></th>
      <th scope="col"><fmt:message key="flight.creationTime"/></th>
    </tr>
  </thead>
  
  <tbody>
  <my:sortShip sortType="${sessionScope.sortType}"/>
  <c:forEach var="flight" items="${sessionScope.shippingsViews}">
	<tr>
      <th scope="row">${flight.getId()}</th>
      <td>${flight.getDispatcherLogin()}</td>
      <td>${flight.getStatus()}</td>
      <td>${flight.getDriverShippngRequestId()}</td>
      <td>${flight.getCarId()}</td>
      <td>${flight.getArrivalCity()}</td>
      <td>${flight.getDepartureCity()}</td>
      <td>${flight.getDepartureTime()}</td>
      <td>${flight.getCreationTimestamp()}</td>
    </tr>
	</c:forEach>
  </tbody>
  
</table>
</div>
 <c:choose>
    <c:when test="${sessionScope.userRole != '2' }">
	
	<div class="col-sm">
		<div class="btn-group">
			<a class="btn btn-outline-success" href="/Autobase/Controller?command=openAddFlight"
					data-toggle="tooltip bg-dark" data-placement="bottom" ><fmt:message key="button.add"/></a>
					
			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-info dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.update"/>
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="flight" items="${sessionScope.shippingsViews}">
						<a class="dropdown-item" href="/Autobase/Controller?command=openUpdateFlight&flightId=${flight.getId()}">${flight.getId()}</a>
					</c:forEach>
    			</div>
  			</div>
  			
  			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-danger dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.cancel"/>
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="flight" items="${sessionScope.shippingsNotUsed}">
						<a class="dropdown-item" href="/Autobase/Controller?command=cancelFlight&flightId=${flight.getId()}">${flight.getId()}</a>
					</c:forEach>
    			</div>
  			</div>
  			
  		</div>
  	</div>
	</c:when>
	<c:when test="${sessionScope.userRole == '2' }">
	
	<div class="col-sm">
		<div class="btn-group">
			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-info dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.addRequest"/>
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="flight" items="${sessionScope.shippingsNotRequested}">
						<a class="dropdown-item" href="/Autobase/Controller?command=openAddRequest&flightId=${flight.getId()}">${flight.getId()}</a>
					</c:forEach>
    			</div>
  			</div>
  		</div>
  	</div>
	</c:when>
	</c:choose>
</div>
<c:choose>
    <c:when test="${sessionScope.userRole != '2' }">
    <div class="col-sm">
		<div class="btn-group">
			<div class="dropdown">
    			<button type="button" class="btn btn btn-outline-warning dropdown-toggle" data-toggle="dropdown">
      			<fmt:message key="button.chooseCarAndDriver"/>
    			</button>
    			<div class="dropdown-menu dropdown-menu-right">
       				<c:forEach var="flight" items="${sessionScope.shippingsNotUsed}">
						<a class="dropdown-item" href="/Autobase/Controller?command=openChooseDriver&chooseStage=1&flightId=${flight.getId()}">${flight.getId()}</a>
					</c:forEach>
    			</div>
  			</div>
  			
		</div>
		</div>
    </c:when>
    </c:choose>
</div>
	<%@ include file="/view/jspf/footer.jspf"%>
	<%@ include file="/view/jspf/bodyScripts.jspf"%>
</body>

</html>