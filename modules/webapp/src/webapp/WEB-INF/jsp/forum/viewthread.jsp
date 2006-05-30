<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="modules" uri="http://www.kantega.no/aksess/tags/modules" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading">
        <a href="."><spring:message code="forum.title"/></a> >
        <a href="viewforum?forumId=<c:out value="${thread.forum.id}"/>"><c:out value="${thread.forum.name}"/></a> >
        <a href="viewthread?threadId=<c:out value="${thread.id}"/>"><c:out value="${thread.name}"/></a>
    </div>

    <p><c:out value="${thread.description}"/></p>
    <kantega:section id="controls">

        <forum:haspermisson permission="POST_IN_THREAD" object="${thread}">
            <a href="<%=request.getContextPath()%>/forum/editpost?threadId=<c:out value="${thread.id}"/>"><spring:message code="post.add"/></a> |
        </forum:haspermisson>
        <forum:haspermisson permission="EDIT_THREAD" object="${thread}">
            <a href="<%=request.getContextPath()%>/forum/editthread?threadId=<c:out value="${thread.id}"/>"><spring:message code="thread.edit"/> </a>
        </forum:haspermisson>

    </kantega:section>

    <div style="padding-bottom: 10px">
        <kantega:getsection id="controls"/>
    </div>

    <c:choose>
        <c:when test="${empty posts}">
            <spring:message code="thread.empty"/>
        </c:when>

        <c:otherwise>

            <table width="100%" cellpadding="0" cellspacing="0">
                <c:forEach items="${posts}" var="post" varStatus="status">
                    <tr class="forum-labelRow">
                        <td>
                            <c:out value="${post.subject}"/>
                            <forum:haspermisson permission="EDIT_POST" object="${post}">
                                <a href="editpost?postId=<c:out value="${post.id}"/>">Endre</a> |
                            </forum:haspermisson>

                            <forum:haspermisson permission="POST_IN_THREAD" object="${thread}">
                                <a href="editpost?replayId=<c:out value="${post.id}"/>&threadId=<c:out value="${post.thread.id}"/>">Svar</a>
                            </forum:haspermisson>

                            <forum:haspermisson permission="DELETE_POST" object="${post}">
                                <c:if test="${gotchildren == 'false'}">
                                    | <a href="deletepost?postId=<c:out value="${post.id}"/>">Slett</a>
                                </c:if>
                            </forum:haspermisson>

                        </td>
                        <td align="right" style="font-weight: normal;">
                            Av  <c:out value="${post.author}"/>, <c:out value="${post.postDate}"/><br>
                        </td>
                    </tr>
                    <tr class="forum-tableRow0">
                        <td colspan="2">
                            <div class="forum-body">
                                <c:out value="${post.body}" escapeXml="false"/>
                            </div>
                        </td>
                    </tr>

                </c:forEach>
            </table>

        </c:otherwise>
    </c:choose>

    <div style="padding-top: 5px">
        <kantega:getsection id="controls"/>
    </div>
</kantega:section>

<%@ include file="include/design/design.jsf"%>