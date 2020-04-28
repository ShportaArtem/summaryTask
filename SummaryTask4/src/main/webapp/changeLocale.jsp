<%@ include file="/view/jspf/page.jspf"%>
<%@ include file="/view/jspf/taglib.jspf"%>

<body class="bg-secondary">
<%-- set the locale --%>
<fmt:setLocale value="${param.locale}" scope="session"/>

<%-- load the bundle (by locale) --%>
<fmt:setBundle basename="resources"/>

<%-- set current locale to session --%>
<c:set var="currentLocale" value="${param.locale}" scope="session"/>

<%-- goto back to the settings--%>
<jsp:forward page="settings.jsp"/>

