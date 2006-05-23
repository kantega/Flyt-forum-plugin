<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<kantega:section id="tittel">
    Kategorier
</kantega:section>

<kantega:section id="innhold">

    <div class="forum-heading"><c:out value="${forumcategory.name}"/></div>

    <p><c:out value="${forumcategory.description}"/></p>


    <c:set var="hasforums" value="false"/>
    <kantega:section id="forums">
        <p><spring:message code="forumcategory.forums"/>:</p>
        <table border="0" cellspacing="0" cellpadding="0">

            <c:forEach items="${forumcategory.forums}" var="forum">
                <c:set var="hasforums" value="true"/>
                <tr><td><a href="<%=request.getContextPath()%>/forum/viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a></td></tr>
            </c:forEach>
        </table>
    </kantega:section>

    <c:choose>

        <c:when test="${hasforums != 'true'}">
            <spring:message code="forumcategory.empty"/>
        </c:when>

        <c:otherwise>
            <kantega:getsection id="forums"/>

        </c:otherwise>

    </c:choose>
    <forum:haspermisson permission="EDIT_CATEGORY" object="${forumcategory}">

        <div style="padding-top: 10px">
            <a href="<%=request.getContextPath()%>/forum/editcategory?categoryId=<c:out value="${forumcategory.id}"/>">Endre</a>
            | <a href="<%=request.getContextPath()%>/forum/editforum?categoryId=<c:out value="${forumcategory.id}"/>">Legg til forum</a>
            | <a href="<%=request.getContextPath()%>/forum/deletecategory?categoryId=<c:out value="${forumcategory.id}"/>">Slett kategori</a>
        </div>
    </forum:haspermisson>
</kantega:section>

<%@include file="include/design/design.jsf"%>