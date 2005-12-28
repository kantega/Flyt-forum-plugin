<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <b><c:out value="${post.subject}"/></b><br>
    <c:out value="${post.body}"/><br>
    <a href="<%=request.getContextPath()%>/forum/editpost?postId=<c:out value="${post.id}"/>&postId=<c:out value="${post.id}"/>">Endre</a> | <a href="<%=request.getContextPath()%>/forum/addpost?postId=<c:out value="${post.id}"/>&threadId=<c:out value="${post.thread.id}"/>">Svar</a>
    <c:if test="${gotchildren == 'false'}">
    | <a href="<%=request.getContextPath()%>/forum/deletepost?postId=<c:out value="${post.id}"/>">Slett</a>
    </c:if>
</kantega:section>

<%@ include file="include/design/design.jsf"%>