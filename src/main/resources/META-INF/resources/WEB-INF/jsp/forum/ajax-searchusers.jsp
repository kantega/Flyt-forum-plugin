<%@ page contentType="text/xml;charset=utf-8" %><?xml version="1.0" encoding="utf-8"?>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<values>
    <c:forEach var="user" items="${userlist}" varStatus="status">
    <value key="<c:out value="${user.user}"/>"><c:out value="${user.fullName}"/></value>
    </c:forEach>
</values>