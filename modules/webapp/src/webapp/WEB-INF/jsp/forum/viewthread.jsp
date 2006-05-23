<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading"><c:out value="${thread.name}"/></div>

    <p><c:out value="${thread.description}"/></p>

    <c:choose>
        <c:when test="${empty posts}">
            <spring:message code="thread.empty"/>
        </c:when>

        <c:otherwise>

            <table width="100%" cellpadding="0" cellspacing="0">
                <tr class="forum-labelRow">
                    <td>
                        <spring:message code="post.subject"/>
                    </td>
                    <td>
                        <spring:message code="post.date"/>
                    </td>
                    <td>
                        <spring:message code="post.author"/>
                    </td>
                </tr>
                <c:forEach items="${posts}" var="post" varStatus="status">
                    <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                        <td>
                            <a href="viewpost?postId=<c:out value="${post.id}"/>"><c:out value="${post.subject}"/></a>
                        </td>
                        <td>
                            <c:out value="${post.postDate}"/>
                        </td>

                        <td>
                            <c:out value="${post.owner.name}"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>

        </c:otherwise>
    </c:choose>

    <div style="padding-top: 5px">
        <forum:haspermisson permission="EDIT_THREAD" object="${thread}">
            <a href="<%=request.getContextPath()%>/forum/editthread?threadId=<c:out value="${thread.id}"/>">Endre</a> |
        </forum:haspermisson>

        <forum:haspermisson permission="POST_IN_THREAD" object="${thread}">
            <a href="<%=request.getContextPath()%>/forum/editpost?threadId=<c:out value="${thread.id}"/>">Ny post</a>
        </forum:haspermisson>
    </div>
</kantega:section>

<%@ include file="include/design/design.jsf"%>