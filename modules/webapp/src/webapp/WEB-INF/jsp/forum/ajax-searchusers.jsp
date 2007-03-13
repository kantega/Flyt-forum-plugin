<%@ page contentType="text/xml;charset=utf-8" language="java" pageEncoding="iso-8859-1" %><?xml version="1.0" encoding="utf-8"?>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<values>
    <c:forEach var="user" items="${userlist}" varStatus="status">
    <value key="<c:out value="${user.user}"/>"><c:out value="${user.fullName}"/></value>
    </c:forEach>
</values>