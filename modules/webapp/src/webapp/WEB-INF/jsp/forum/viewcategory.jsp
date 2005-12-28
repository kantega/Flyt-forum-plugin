<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="overskrift"><c:out value="${forumcategory.name}"/></div>
    <c:out value="${forumcategory.description}"/><br><br>
    <spring:message code="forumcategory.forums"/>:<br>
    <c:forEach items="${forumcategory.forums}" var="forum">
        <a href="<%=request.getContextPath()%>/forum/viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a><br>
    </c:forEach>
    <br>
    <a href="<%=request.getContextPath()%>/forum/editcategory?categoryId=<c:out value="${forumcategory.id}"/>">Endre</a> | <a href="<%=request.getContextPath()%>/forum/addforum?categoryId=<c:out value="${forumcategory.id}"/>">Legg til forum</a> | <a href="<%=request.getContextPath()%>/forum/deletecategory?categoryId=<c:out value="${forumcategory.id}"/>">Slett kategori</a>
</kantega:section>

<%@include file="include/design/design.jsf"%>