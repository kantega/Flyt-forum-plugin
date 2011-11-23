<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>

<c:set var="userProfileUrl"><aksess:geturl/><aksess:getconfig key="forum.userprofileurl"/>?userId=${post.owner}</c:set>
<c:set var="userImageUrl"><aksess:geturl/><aksess:getconfig key="forum.userimageurl"/>?userId=${post.owner}&amp;width=40</c:set>

<c:set var="userProfileBaseUrl"><aksess:geturl/><aksess:getconfig key="forum.userprofileurl"/>?userId=</c:set>
<c:set var="userImageBaseUrl"><aksess:geturl/><aksess:getconfig key="forum.userimageurl"/>?width=40&amp;userId=</c:set>

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

        <c:choose>
            <c:when test="${postsStatus.index == 0}">
                <%-- First post in thread. Deleting the first post will result in deleting the entire thread --%>
                <forum:haspermisson permission="DELETE_THREAD" object="${post.thread}">
                    <a class="oa-forum-deleteThread" href="<aksess:geturl/>/forum/deletethread?threadId=<c:out value="${post.thread.id}"/>"></a>
                </forum:haspermisson>
            </c:when>
            <c:otherwise>
                <forum:haspermisson permission="DELETE_POST" object="${post}">
                    <a class="oa-forum-deletePost" href="<aksess:geturl/>/forum/deletepost?postId=<c:out value="${post.id}"/>"></a>
                </forum:haspermisson>
            </c:otherwise>
        </c:choose>


        <div class="oa-forum-body">
            <p><c:out value="${post.body}" escapeXml="false"/></p>
        </div>
        <c:if test="${not empty post.attachments}">
            <div class="oa-forum-attachments">
                <c:forEach items="${post.attachments}" var="attachment" varStatus="status">
                    <c:choose>
                        <c:when test="${attachment.image}">
                            <a class="oa-forum-attachment" href="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=<c:out value="${attachment.id}"/>&width=100&height=100" target="_blank">
                                <img src="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=<c:out value="${attachment.id}"/>&width=100&height=100" alt="<c:out value="${attachment.fileName}"/>" border="0">
                            </a>
                            <p class="oa-forum-fadedText oa-forum-enlarge-photo">
                                <kantega:label key="forum.wall.image.sizeup" bundle="forum" locale="${forumLocale}"/>
                            </p>
                            <p class="oa-forum-fadedText oa-forum-reduce-photo oa-forum-hidden oa-forum-txtr">
                                <kantega:label key="forum.wall.image.sizedown" bundle="forum" locale="${forumLocale}"/>
                            </p>
                            <p class="oa-forum-download-original oa-forum-hidden oa-forum-txtr">
                                <a href="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=<c:out value="${attachment.id}"/>" target="_blank"><kantega:label key="forum.wall.image.download.original" bundle="forum" locale="${forumLocale}"/></a>
                            </p>
                        </c:when>
                        <c:otherwise>
                            <a class="oa-forum-attachment-doc" href="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=<c:out value="${attachment.id}"/>" target="_blank">
                                <c:out value="${attachment.fileName}"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>
        <div class="oa-forum-metadata">
            <span class="oa-forum-date" date-data="<fmt:formatDate value="${post.postDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>"><fmt:formatDate value="${post.postDate}" pattern="dd.MM.yyyy HH:mm"/></span>
            <c:if test="${fn:length(thread.posts) == 1}">
                &nbsp;|&nbsp; <a href="#" class="oa-forum-showReplyForm"><kantega:label key="forum.wall.leave.comment" bundle="forum" locale="${forumLocale}"/></a>
            </c:if>
            <c:if test="${postsStatus.index == 0 && !postsStatus.last && fn:length(thread.posts) > 2}">
                &nbsp;|&nbsp; <a href="" class="oa-forum-showFullThread"><kantega:label key="forum.wall.morecomments.part1" bundle="forum" locale="${forumLocale}"/> <span class="oa-forum-numberOfComments">${fn:length(thread.posts) - 2}</span> <kantega:label key="forum.wall.morecomments.part2" bundle="forum" locale="${forumLocale}"/></a>
            </c:if>
            &nbsp;|&nbsp;
            <c:set var="postRatings" value="${ratings[post.id]}"/>
            <c:set var="hasLikedPost" value="false"/>


            <c:forEach items="${postRatings}" var="rating" varStatus="status">
                <aksess:getuser userid="${rating.userid}" name="user"/>
                <c:if test="${rating.userid == user.id}">
                    <c:set var="hasLikedPost" value="true"/>
                </c:if>
            </c:forEach>

            <c:choose>
                <c:when test="${hasLikedPost}">
                    <span class="oa-forum-has-liked"><kantega:label key="forum.wall.likes" bundle="forum" locale="${forumLocale}"/></span>
                </c:when>
                <c:otherwise>
                    <a href="#" class="oa-forum-like-link"><kantega:label key="forum.wall.like" bundle="forum" locale="${forumLocale}"/></a>
                    <form action="<aksess:geturl url="/forum/like"/>" method="post" class="oa-forum-likeForm oa-forum-hidden">
                        <input type="hidden" name="rating" value="1">
                        <input type="hidden" name="objectId" value="${post.id}">
                        <input type="hidden" name="context" value="forum">
                        <input type="hidden" name="responsetype" value="json">
                        <input type="submit" value="Like">
                    </form>
                </c:otherwise>
            </c:choose>
            <c:set var="hasLikedPost" value="false"/>
        </div>
    </div>
    <div style="clear:both"></div>
    <c:if test="${not empty postRatings}">
        <div class="oa-forum-mediablock oa-forum-likes oa-forum-fadedText">
            <c:forEach items="${postRatings}" var="rating" varStatus="status">
                <aksess:getuser userid="${rating.userid}" name="user"/>
                <c:choose>
                    <c:when test="${status.index == 0}">

                    </c:when>
                    <c:when test="${status.index > 0 && status.last}">
                        <kantega:label key="forum.wall.like.and" bundle="forum" locale="${forumLocale}"/>
                    </c:when>
                    <c:when test="${status.index > 0 && !status.last}">
                        ,
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>
                <a href="${userProfileBaseUrl}${rating.userid}">${user.name}</a>
            </c:forEach>
            <kantega:label key="forum.wall.likes.this" bundle="forum" locale="${forumLocale}"/>
        </div>
    </c:if>
</div>