<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading"><c:out value="${post.subject}"/></div>
    <c:out value="${post.body}"/><br>

    <div style="padding-top: 10px">
        <forum:haspermisson permission="EDIT_POST" object="${post}">
            <a href="editpost?postId=<c:out value="${post.id}"/>">Endre</a> |
        </forum:haspermisson>

        <forum:haspermisson permission="POST_IN_THREAD" object="${post.thread}">
            <a href="editpost?replayId=<c:out value="${post.id}"/>&threadId=<c:out value="${post.thread.id}"/>">Svar</a>
        </forum:haspermisson>

        <forum:haspermisson permission="DELETE_POST" object="${post}">
            <c:if test="${gotchildren == 'false'}">
                | <a href="deletepost?postId=<c:out value="${post.id}"/>">Slett</a>
            </c:if>
        </forum:haspermisson>
    </div>
</kantega:section>

<%@ include file="include/design/design.jsf"%>