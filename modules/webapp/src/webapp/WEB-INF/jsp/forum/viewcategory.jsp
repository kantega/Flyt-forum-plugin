<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <b><c:out value="${forumcategory.name}"/></b><br>
    <c:out value="${forumcategory.description}"/><br>
    <spring:message code="forumcategory.forums"/><br>
    <c:forEach items="${forumcategory.forums}" var="forum">
        <a href="<%=request.getContextPath()%>/forum/viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a><br>
    </c:forEach>
    <a href="<%=request.getContextPath()%>/forum/addforum?categoryId=<%=request.getParameter("id")%>">Legg til forum</a>
</kantega:section>

<%@include file="include/design/design.jsf"%>