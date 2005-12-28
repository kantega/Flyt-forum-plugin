<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <b><c:out value="${thread.name}"/></b><br>
    <c:out value="${thread.description}"/><br>
    <spring:message code="thread.posts"/><br>
    <c:forEach items="${thread.posts}" var="post">
        <a href="<%=request.getContextPath()%>/forum/viewpost?postId=<c:out value="${post.id}"/>"><c:out value="${post.subject}"/></a> (<c:out value="${post.postDate}"/>) [<c:out value="${post.owner.name}"/>]<br>
    </c:forEach>
    <a href="<%=request.getContextPath()%>/forum/editthread?threadId=<c:out value="${thread.id}"/>">Endre</a> | <a href="<%=request.getContextPath()%>/forum/addpost?threadId=<c:out value="${thread.id}"/>">Ny post</a>
</kantega:section>

<%@ include file="include/design/design.jsf"%>