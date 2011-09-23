<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>

<div class="oa-forum-thread">
    <div class="oa-forum-posts">
        <c:forEach items="${thread.posts}" var="post" varStatus="postsStatus">
            <c:set var="hiddenPost" value="false" scope="page"/>
            <c:if test="${postsStatus.count > 1 && !postsStatus.last}">
                <c:set var="hiddenPost" value="true" scope="page"/>
            </c:if>
            <%@ include file="ajax-post.jsp" %>
        </c:forEach>
    </div>

    <div class="oa-forum-mediablock oa-forum-reply <c:if test="${fn:length(thread.posts) == 1}">oa-forum-hidden</c:if>">
        <div class="oa-forum-mediablockContent oa-forum-reply">
            <form action="${pageContext.request.contextPath}/forum/editpost" id="ForumReply${thread.id}">
                <div>
                    <input type="hidden" name="threadId" value="${thread.id}">
                    <label class="oa-forum-hidden"><kantega:label key="forum.comment.inputfield.label" bundle="forum"/></label>
                    <textarea rows="1" cols="40" name="body" class="oa-forum-sharefield oa-forum-comment-reply oa-forum-fadedText"><kantega:label key="forum.comment.inputfield.label" bundle="forum"/></textarea>
                </div>
            </form>
        </div>
    </div>

</div>