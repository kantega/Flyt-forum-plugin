<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading">
        <a href="."><spring:message code="forum.title"/></a> >
        
        <a href="viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a>
    </div>


    <p><c:out value="${forum.description}"/></p>

    <forum:haspermisson permission="EDIT_THREAD">
        <a href="editpost?forumId=<c:out value="${forum.id}"/>">Ny tråd</a>
    </forum:haspermisson>

    <forum:haspermisson permission="EDIT_FORUM" object="${forum}">
            | <a href="editforum?forumId=<c:out value="${forum.id}"/>">Endre forum</a>
            | <a href="deleteforum?forumId=<c:out value="${forum.id}"/>">Slett forum</a>
    </forum:haspermisson>


    <br><br>

    <c:choose>

    <c:when test="${empty threads}">
        <spring:message code="forum.empty"/>
    </c:when>
    <c:otherwise>

        <table width="100%" cellpadding="0" cellspacing="0">
            <tr class="forum-labelRow">
                <td>
                    <spring:message code="thread.name"/>
                </td>
                <td>
                    <spring:message code="thread.posts"/>
                </td>

            </tr>
            <c:forEach items="${threads}" var="thread" varStatus="status">
                <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                    <td>
                        <a href="viewthread?threadId=<c:out value="${thread.id}"/>"><c:out value="${thread.name}"/></a>
                    </td>
                    <td>
                        <c:out value="${thread.numPosts}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
    </c:choose>

</kantega:section>

<%@ include file="include/design/design.jsf" %>