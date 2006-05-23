<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading"><c:out value="${forum.name}"/></div>
    <p><c:out value="${forum.description}"/></p>

    <c:choose>

    <c:when test="${empty threads}">
        </c:when>
    <c:otherwise>
        <p><spring:message code="forum.threads"/>:</p>

        <table width="100%" cellpadding="0" cellspacing="0">
            <c:forEach items="${threads}" var="thread" varStatus="status">
                <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                    <td>
                        <a href="viewthread?threadId=<c:out value="${thread.id}"/>"><c:out value="${thread.name}"/></a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
    </c:choose>

    <forum:haspermisson permission="EDIT_FORUM" object="${forum}">
        <div style="padding-top: 10px">
            <a href="editforum?forumId=<c:out value="${forum.id}"/>">Endre forum</a>
            | <a href="editthread?forumId=<c:out value="${forum.id}"/>">Ny tråd</a>
            | <a href="deleteforum?forumId=<c:out value="${forum.id}"/>">Slett forum</a>
        </div>
    </forum:haspermisson>
</kantega:section>

<%@ include file="include/design/design.jsf" %>