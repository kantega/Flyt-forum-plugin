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

    <c:set var="hasforums" value="false"/>
    <kantega:section id="forums">
        <table border="0" cellspacing="0" cellpadding="0" width="100%">
            <tr class="forum-labelRow">
                <td>
                    <spring:message code="forum.name"/>
                </td>
            </tr>
                <c:forEach items="${forumcategory.forums}" var="forum" varStatus="status">
                    <c:set var="hasforums" value="true"/>
                    <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                        <td>
                            <a href="viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a>
                        </td>
                    </tr>
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