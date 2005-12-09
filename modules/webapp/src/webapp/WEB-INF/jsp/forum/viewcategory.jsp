<%@ page import="no.kantega.forum.model.ForumCategory"%>
<%@ page import="java.util.List"%>
<%@ page import="no.kantega.forum.model.Forum"%>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <b><c:out value="${forumcategory.name}"/></b><br>
    <c:out value="${forumcategory.description}"/><br>
    <c:forEach items="${forumcategory.forums}" var="forum">
        <c:out value="${forum.name}"/><br>
    </c:forEach>
</kantega:section>

<%@include file="include/design/design.jsf"%>