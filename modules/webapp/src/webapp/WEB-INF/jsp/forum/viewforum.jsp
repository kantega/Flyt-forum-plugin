<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <b><c:out value="${forum.name}"/></b><br>
    <c:out value="${forum.description}"/><br>
    <spring:message code="forum.threads"/><br>
    <c:forEach items="${forum.threads}" var="thread">
        <a href="<%=request.getContextPath()%>/forum/viewthread?threadId=<c:out value="${thread.id}"/>"><c:out value="${thread.name}"/></a><br>
    </c:forEach>
    <a href="<%=request.getContextPath()%>/forum/editforum?forumId=<c:out value="${forum.id}"/>">Ny tråd</a> | <a href="<%=request.getContextPath()%>/forum/addthread?forumId=<c:out value="${forum.id}"/>">Ny tråd</a>
</kantega:section>

<%@ include file="include/design/design.jsf" %>