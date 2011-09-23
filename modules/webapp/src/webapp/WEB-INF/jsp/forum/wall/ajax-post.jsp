<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>


<c:set var="userProfileUrl"><aksess:geturl/><aksess:getconfig key="forum.userprofileurl"/>?userId=${post.owner}</c:set>
<c:set var="userImageUrl"><aksess:geturl/><aksess:getconfig key="forum.userimageurl"/>?userId=${post.owner}&amp;width=40</c:set>
<div class="oa-forum-post oa-forum-mediablock <c:if test="${hiddenPost}">oa-forum-hidden</c:if>">
    <div class="oa-forum-mediablockImage oa-forum-avatar">
        <a href="${userProfileUrl}">
            <img src="${userImageUrl}">
        </a>
    </div>
    <div class="oa-forum-mediablockContent">
        <a name="oa-forum-post_<c:out value="${post.id}"/>"></a>
        <div class="oa-forum-username">
            <a href="${userProfileUrl}"><c:out value="${post.author}"/></a>
        </div>
        <forum:haspermisson permission="DELETE_POST" object="${post}">
            <a class="oa-forum-deletePost" href="<aksess:geturl/>/forum/deletepost?postId=<c:out value="${post.id}"/>"></a>
        </forum:haspermisson>

        <div class="oa-forum-body">
            <p><c:out value="${post.body}" escapeXml="false"/></p>
        </div>
        <c:if test="${post.attachments != null}">
            <div class="oa-forum-attachments">
                <c:forEach items="${post.attachments}" var="attachment" varStatus="status">
                    <a class="oa-forum-attachment" href="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=<c:out value="${attachment.id}"/>&width=100&height=100" target="_blank"><img src="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=<c:out value="${attachment.id}"/>&width=100&height=100" alt="<c:out value="${attachment.fileName}"/>" border="0"></a>
                </c:forEach>
            </div>
        </c:if>

        <div class="oa-forum-metadata">
            <span class="oa-forum-date" date-data="<fmt:formatDate value="${post.postDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>"><fmt:formatDate value="${post.postDate}" pattern="dd.MM.yyyy HH:mm"/></span>
            <c:if test="${fn:length(thread.posts) == 1}">
                &nbsp;|&nbsp; <a href="#" class="oa-forum-showReplyForm">Leave a comment</a>
            </c:if>
            <c:if test="${postsStatus.index == 0 && !postsStatus.last && fn:length(thread.posts) > 2}">
            &nbsp;|&nbsp; <a href="" class="oa-forum-showFullThread"><kantega:label key="forum.wall.morecomments.part1" bundle="forum" locale="${forumLocale}"/> ${fn:length(thread.posts) - 2} <kantega:label key="forum.wall.morecomments.part2" bundle="forum" locale="${forumLocale}"/></a></c:if>
        </div>
    </div>
    <div style="clear:both"></div>
</div>