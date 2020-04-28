<%@ include file="/view/jspf/page.jspf"%>
<%@ include file="/view/jspf/taglib.jspf"%>
<!doctype html>
<html lang="en">
<%@ include file="/view/jspf/head.jspf"%>
<body class="bg-secondary">
<%@ include file="/view/jspf/header1.jspf"%>
<div class="container-fluid">
<div class="row"><hr></div>
<div class="row">
<div class="col-lg-6">
<form id="login_form" action="Controller" method="post">
<input type="hidden" name="command" value="updateCar" />
<div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="car.number" /></label>
   <input class="form-control" type="text" placeholder="${sessionScope.carViewNow.getId()}" readonly>
  </div>
<div class="form-group">
    <label for="exampleFormControlSelect1"><fmt:message key="car.firm" /></label>
    <select class="form-control" id="exampleFormControlSelect1" name="firm">
    	<c:forEach var="firm1" items="${sessionScope.firms}">
			<option value="" selected disabled>${sessionScope.carViewNow.getFirmName()}</option>    	
			<option>${firm1.getName()}</option>
	</c:forEach>
    </select>
  </div>
<div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="car.model" /></label>
   <input class="form-control" type="text" placeholder="${sessionScope.carViewNow.getModel()}" name="model" pattern="\b\w+\b">
  </div>
  <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="car.carryingCapacity" /></label>
    <input type="number" class="form-control" id="carryingCapacity" name="carryingCapacity" placeholder="${sessionScope.carViewNow.getCarryingCapacity()}">
  </div>
   <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="car.passengersCapacity" /></label>
    <input type="number" class="form-control" id="passengersCapacity" name="passengersCapacity" placeholder="${sessionScope.carViewNow.getPassangersCapacity()}">
  </div>
   <div class="form-group">
    <label for="exampleFormControlSelect1"><fmt:message key="car.vehicleCondition" /></label>
    <select class="form-control" id="exampleFormControlSelect1" name="vehicleCondition">
      <option value="" selected disabled>${sessionScope.carViewNow.getVehicleCondition()}</option>    	
      <option>Excellent</option>
      <option>Good</option>
      <option>Fair</option>
      <option>Poor</option>
    </select>
  </div>
  
<div class="form-group">
   <button type="submit" class="btn btn-success mb-2"><fmt:message key="button.confirm" /></button>
   <button type="button" class="btn btn-primary mb-2" onclick="javascript:history.back()"><fmt:message key="button.back" /></button>
</div>
</form>
</div>
</div>
</div>
<%@ include file="/view/jspf/footer.jspf"%>
<%@ include file="/view/jspf/bodyScripts.jspf"%>
</body>
</html>