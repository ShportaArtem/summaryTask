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
<input type="hidden" name="command" value="addFlight" />
<div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="flight.dispatcher" /></label>
   <input class="form-control" type="text" placeholder="${sessionScope.user.getLogin()}" readonly>
  </div>
  <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="flight.arrivalCity" /></label>
    <input class="form-control" id="arrivalCity" name="arrivalCity" placeholder="City" pattern="\b\w+\b">
  </div>
  <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="flight.departureCity" /></label>
    <input class="form-control" id="departureCity" name="departureCity" placeholder="City" pattern="\b\w+\b">
  </div>
  <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="flight.departureDate" /></label>
    <input type="date" class="form-control" id="departureDate" name="departureDate">
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