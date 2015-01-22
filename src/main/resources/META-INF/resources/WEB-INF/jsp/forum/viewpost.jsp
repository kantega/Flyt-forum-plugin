<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=utf-8" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <script type="text/javascript" >
        function deletePost(postId) {
            if(confirm("<spring:message code="post.delete"/>")) {
                document.deletepost.postId.value = postId;
                document.deletepost.submit();
            }
        }
    </script>

    <form action="deletepost" name="deletepost" method="POST">
        <input type="hidden" name="postId">
    </form>

    <div class="forum-heading"><c:out value="${post.subject}"/></div>
    <div class="forum-body">
            <c:out value="${post.body}" escapeXml="false"/>
    </div>

    <div style="padding-top: 10px">
        <c:if test="${!post.approved}">
        <forum:haspermisson permission="APPROVE_POST" object="${post}">
            <a href="approvepost?postId=<c:out value="${post.id}"/>">Godkjenn</a> |
        </forum:haspermisson>
        </c:if>
        <forum:haspermisson permission="EDIT_POST" object="${post}">
            <a href="editpost?postId=<c:out value="${post.id}"/>">Endre</a> |
        </forum:haspermisson>
        <forum:haspermisson permission="DELETE_POST" object="${post}">
            <c:if test="${gotchildren == 'false'}">
            <a href="javascript:deletePost(<c:out value="${post.id}"/>)">Slett</a>
            </c:if>
        </forum:haspermisson>
    </div>
    <c:if test="${!post.approved}">
        <div class="forum-message"><spring:message code="post.moderate.info"/></div>
    </c:if>
</kantega:section>

<%@ include file="include/design/design.jsf"%>