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
<input required type="hidden" name="command" value="finishFlight" />
   <div class="form-group">
    <label for="exampleFormControlSelect1"><fmt:message key="car.markVehicleCondition"/></label>
    <select class="form-control" id="exampleFormControlSelect1" name="vehicleCondition">
      <option>Excellent</option>
      <option>Good</option>
      <option>Fair</option>
      <option>Poor</option>
    </select>
  </div>
  
<div class="form-group">
   <button type="submit" class="btn btn-success mb-2"><fmt:message key="button.confirm"/></button>
   <button type="button" class="btn btn-primary mb-2" onclick="javascript:history.back()"><fmt:message key="button.back"/></button>
</div>
</form>
</div>
</div>
</div>
<%@ include file="/view/jspf/footer.jspf"%>
<%@ include file="/view/jspf/bodyScripts.jspf"%>
</body>
</html>