<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
    Kategorier
</kantega:section>

<kantega:section id="innhold">
    <table border="0" cellspacing="0" cellpadding="0">
        <tr><td class="overskrift"><c:out value="${forumcategory.name}"/></td></tr>
        <tr><td><c:out value="${forumcategory.description}"/></td></tr>
        <tr><td><spring:message code="forumcategory.forums"/>:</td></tr>
        <c:forEach items="${forumcategory.forums}" var="forum">
            <tr><td><a href="<%=request.getContextPath()%>/forum/viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a></td></tr>
        </c:forEach>
        <tr><td>&nbsp;</td></tr>
        <tr><td><a href="<%=request.getContextPath()%>/forum/editcategory?categoryId=<c:out value="${forumcategory.id}"/>">Endre</a> | <a href="<%=request.getContextPath()%>/forum/addforum?categoryId=<c:out value="${forumcategory.id}"/>">Legg til forum</a> | <a href="<%=request.getContextPath()%>/forum/deletecategory?categoryId=<c:out value="${forumcategory.id}"/>">Slett kategori</a></td></tr>
    </table>
</kantega:section>

<%@include file="include/design/design.jsf"%>