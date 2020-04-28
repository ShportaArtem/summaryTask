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
<input type="hidden" name="command" value="updateDriver" />
<div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="user.login" /></label>
    <input class="form-control" type="text" name="login" placeholder="${sessionScope.driverViewNow.getLogin()}" pattern="\b\w+\b">
	</div>
  
  <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="user.password" /></label>
    <input class="form-control" id="password" name="password" placeholder="${sessionScope.driverViewNow.getPassword()}" pattern="\b\w+\b">
  </div>
  
  <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="user.name" /></label>
    <input class="form-control" id="name" name="name" placeholder="${sessionScope.driverViewNow.getName()}" pattern="\b\w+\b">
  </div>
  
   <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="user.surname" /></label>
    <input class="form-control" id="surname" name="surname" placeholder="${sessionScope.driverViewNow.getSurname()}" pattern="\b\w+\b">
  </div>
  
  <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="driver.phone" /></label>
    <input class="form-control" type="tel" id="phone" name="phone" placeholder="${sessionScope.driverViewNow.getPhone()}" pattern="\+\([0-9]{3}\)[0-9]{2}-[0-9]{3}-[0-9]{4}">
  </div>
  
   <div class="form-group">
    <label for="exampleFormControlInput1"><fmt:message key="driver.passport" /></label>
    <input class="form-control"  id="passport" name="passport" placeholder="${sessionScope.driverViewNow.getPassport()}" pattern="[0-9]{9}">
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