<%@ include file="/view/jspf/page.jspf"%>
<%@ include file="/view/jspf/taglib.jspf"%>
<!doctype html>
<html lang="en">
<%@ include file="/view/jspf/head2.jspf"%>
<body class="bg-secondary">

	<%@ include file="/view/jspf/header1.jspf"%>
	<div class="container mt-sm-5 " style="width: 20%;">
		<div class="jumbotron jumbotron-liquid pt-3" style="height: 308px;">
			<form id="login_form" action="Controller" method="post">
				<input type="hidden" name="command" value="login" />
				<div class="form-group">
					<label for="login"><fmt:message key="login.login" /></label> <input type="text"
						name="loginUser" class="form-control" id="exampleInputEmail1"
						aria-describedby="emailHelp" /> <small id="loginHelp"
						class="form-text text-muted"><fmt:message key="login.enterLogin" /></small>
				</div>
				<div class="form-group">
					<label for="exampleInputPassword1"><fmt:message key="login.password" /></label> <input
						type="password" name="passwordUser" class="form-control"
						id="exampleInputPassword1" /> <small id="PasswordHelp"
						class="form-text text-muted"><fmt:message key="login.enterPassword" /></small>
				</div>
				<button type="submit" class="btn btn-outline-success "
					data-toggle="tooltip bg-dark" data-placement="bottom"
					title="log in to your account"><fmt:message key="login.signIn" /></button>
			</form>
		</div>
	</div>

	<%@ include file="/view/jspf/footer.jspf"%>
	<%@ include file="/view/jspf/bodyScripts.jspf"%>

</body>

</html>