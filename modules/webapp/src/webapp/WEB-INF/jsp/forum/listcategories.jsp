<%@ page import="no.kantega.forum.model.ForumCategory"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core"  prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="overskrift">Forumkategorier:</div>
    <c:forEach items="${categories}" var="category">
        <a href="<%=request.getContextPath()%>/forum/viewcategory?categoryId=<c:out value="${category.id}"/>"><c:out value="${category.name}"/></a><br>
    </c:forEach>
    <br>
    <a href="<%=request.getContextPath()%>/forum/addcategory">Ny kategori</a>
</kantega:section>

<%@include file="include/design/design.jsf"%>