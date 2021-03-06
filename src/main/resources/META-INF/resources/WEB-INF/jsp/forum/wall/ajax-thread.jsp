<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>

<c:if test="${empty forumLocale}">
    <c:set var="forumLocale" value="no_NO"/>
</c:if>
<c:set var="defaultProfileImage"><aksess:getconfig key="forum.defaultProfileImage" default="/bitmaps/forum/avatar_anon.png"/></c:set>
<script>
    function handleProfileImageNotFound(image){
        image.onerror = "";
        <c:if test="${not empty defaultProfileImage}">
        image.src = "${pageContext.request.contextPath}${defaultProfileImage}";
        </c:if>
        return true;
    }
</script>
<div class="oa-forum-thread <c:if test="${not empty wallHasMorePosts}">oa-forum-thread-has-more-posts</c:if>">
    <div class="oa-forum-posts">
        <c:forEach items="${thread.posts}" var="post" varStatus="postsStatus">
            <c:set var="hiddenPost" value="false" scope="page"/>
            <c:if test="${postsStatus.count > 1 && !postsStatus.last}">
                <c:set var="hiddenPost" value="true" scope="page"/>
            </c:if>
            <c:set var="postsStatus" value="${postsStatus}" scope="request"/>
            <%@ include file="ajax-post.jsp" %>
        </c:forEach>
    </div>
    <div class="oa-forum-mediablock oa-forum-reply <c:if test="${fn:length(thread.posts) == 1}">oa-forum-hidden</c:if>">
        <div class="oa-forum-mediablockContent oa-forum-reply">
            <form action="${pageContext.request.contextPath}/forum/editpost" id="ForumReply${thread.id}">
                <div>
                    <div class="oa-forum-formElement">
                        <input type="hidden" name="threadId" value="${thread.id}">
                        <label class="oa-forum-hidden"><kantega:label key="forum.comment.inputfield.label" bundle="forum" locale="${forumLocale}"/></label>
                        <textarea rows="1" cols="40" name="body" class="oa-forum-sharefield oa-forum-comment-reply" placeholder="<kantega:label key="forum.comment.inputfield.label" bundle="forum" locale="${forumLocale}"/>"></textarea>
                    </div>
                    <div class="oa-forum-formElement oa-forum-txtR">
                        <span class="oa-forum-share-button">
                            <input type="submit" value="<kantega:label key="forum.wall.comment.submit" bundle="forum" locale="${forumLocale}"/>" name="send" class="oa-forum-share-comment">
                        </span>
                    </div>
                </div>
            </form>
        </div>
    </div>

</div>
