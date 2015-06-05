<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>
<%@ taglib prefix="kantega" uri="http://www.kantega.no/aksess/tags/commons" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags" %>


<c:set var="userProfileUrl">${pageContext.request.contextPath}<aksess:getconfig key="forum.userprofileurl"/>?userId=${post.owner}</c:set>
<c:set var="userImageUrl">${pageContext.request.contextPath}<aksess:getconfig key="forum.userimageurl"/>?userId=${post.owner}&amp;width=40</c:set>

<c:set var="userProfileBaseUrl">${pageContext.request.contextPath}<aksess:getconfig key="forum.userprofileurl"/>?userId=</c:set>
<c:set var="userImageBaseUrl">${pageContext.request.contextPath}<aksess:getconfig key="forum.userimageurl"/>?width=40&amp;userId=</c:set>

<c:set var="imagePreviewWidth"><aksess:getconfig key="forum.imagePreviewWidth" default="100"/></c:set>
<c:set var="imagePreviewHeight"><aksess:getconfig key="forum.imagePreviewHeight" default="100"/></c:set>

<div class="oa-forum-post oa-forum-mediablock <c:if test="${hiddenPost && !expandThreads }">oa-forum-hidden</c:if>">
    <div class="oa-forum-mediablockImage oa-forum-avatar">
        <a href="${userProfileUrl}">
            <img src="${userImageUrl}" onerror="handleProfileImageNotFound(this);">
        </a>
    </div>
    <div class="oa-forum-mediablockContent">
        <a name="oa-forum-post_${post.id}"></a>

        <div class="oa-forum-username">
            <a href="${userProfileUrl}"><c:out value="${post.author}"/>:</a>
        </div>

        <c:choose>
            <c:when test="${postsStatus.index == 0}">
                <forum:haspermisson permission="EDIT_THREAD" object="${post.thread}">
                    <a class="oa-forum-editThread" href="${pageContext.request.contextPath}/forum/editthread?threadId=${post.thread.id}"></a>
                </forum:haspermisson>
            </c:when>
            <c:otherwise>
                <forum:haspermisson permission="EDIT_POST" object="${post}">
                    <a class="oa-forum-editPost" href="${pageContext.request.contextPath}/forum/editpost?postId=${post.id}"></a>
                </forum:haspermisson>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${postsStatus.index == 0}">
                <%-- First post in thread. Deleting the first post will result in deleting the entire thread --%>
                <forum:haspermisson permission="DELETE_THREAD" object="${post.thread}">
                    <a class="oa-forum-deleteThread" href="${pageContext.request.contextPath}/forum/deletethread?threadId=${post.thread.id}"></a>
                </forum:haspermisson>
            </c:when>
            <c:otherwise>
                <forum:haspermisson permission="DELETE_POST" object="${post}">
                    <a class="oa-forum-deletePost" href="${pageContext.request.contextPath}/forum/deletepost?postId=${post.id}"></a>
                </forum:haspermisson>
            </c:otherwise>
        </c:choose>

        <div class="oa-forum-body">
            <p>
                <c:choose>
                    <c:when test="${postsStatus.index == 0}">
                        <%-- Only the first post in a thread might need a preview. Comments in a thread is displayed as normal --%>
                        <forum:formatwallpost postbody="${post.body}" charsinbodypreview="${oaForumPostPreviewCharlength}"/>
                    </c:when>
                    <c:when test="${trunctateAllPostsInThread}">
                        <forum:formatwallpost postbody="${post.body}" charsinbodypreview="${oaForumPostPreviewCharlength}"/>
                    </c:when>
                    <c:otherwise>
                       <form:escapeBody javaScriptEscape="false" htmlEscape="false">${post.body}</form:escapeBody>
                    </c:otherwise>
                </c:choose>
            </p>
        </div>
        <c:if test="${not empty post.attachments}">
            <div class="oa-forum-attachments">
                <c:forEach items="${post.attachments}" var="attachment" varStatus="status">
                    <c:choose>
                        <c:when test="${attachment.image}">
                            <a class="oa-forum-attachment" data-download="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=${attachment.id}" href="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=${attachment.id}&width=${imagePreviewWidth}&height=${imagePreviewHeight}" target="_blank">
                                <img src="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=${attachment.id}&width=${imagePreviewWidth}&height=${imagePreviewHeight}" alt="<c:out value="${attachment.fileName}"/>" border="0">
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a class="oa-forum-attachment-doc" href="${pageContext.request.contextPath}/forum/viewattachment?attachmentId=${attachment.id}" target="_blank">
                                <c:out value="${attachment.fileName}"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </div>
        </c:if>
        <div class="oa-forum-metadata">

            <c:if test="${fn:length(thread.posts) == 1}">
                <a href="#" class="oa-forum-showReplyForm"><kantega:label key="forum.wall.leave.comment" bundle="forum" locale="${forumLocale}"/></a>
                <span class="oa-forum-divider">&nbsp;&sdot;&nbsp;</span>
            </c:if>

            <c:set var="postRatings" value="${ratings[post.id]}"/>
            <c:set var="hasLikedPost" value="false"/>

            <c:forEach items="${postRatings}" var="rating" varStatus="status">
                <aksess:getuser userid="${rating.userid}" name="user" />
                <aksess:getuser name="innloggetBruker" />
                <c:if test="${rating.userid == innloggetBruker.id}">
                    <c:set var="hasLikedPost" value="true"/>
                </c:if>
            </c:forEach>

            <c:choose>
                <c:when test="${hasLikedPost}">
                    <a href="${pageContext.request.contextPath}/forum/like" class="oa-forum-like-link" data-objectid="${post.id}" data-likes="true"><kantega:label key="forum.wall.likes" bundle="forum" locale="${forumLocale}"/></a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/forum/like" class="oa-forum-like-link" data-objectid="${post.id}" data-likes="false"><kantega:label key="forum.wall.like" bundle="forum" locale="${forumLocale}"/></a>
                </c:otherwise>
            </c:choose>
            <c:remove var="hasLikedPost"/>

            <span class="oa-forum-divider">&nbsp;&sdot;&nbsp;</span>
            <span class="oa-forum-date" date-data="<fmt:formatDate value="${post.postDate}" pattern="yyyy-MM-dd'T'HH:mm:ss"/>"><fmt:formatDate value="${post.postDate}" pattern="dd.MM.yyyy HH:mm"/></span>

            <c:if test="${postsStatus.first && post.thread.forum.id != hiddenForumId}">
                i <a href="?forumId=${post.thread.forum.id}"><span class="oa-forum-category">${post.thread.forum.name}</span></a>
            </c:if>

        </div>
    </div>
    <div style="clear:both"></div>

    <c:if test="${not empty postRatings}">
        <div class="oa-forum-mediablock oa-forum-likes oa-forum-fadedText">
            <c:set var="manyRatings" value="${fn:length(postRatings) > 3}"/>
            <c:choose>
                <c:when test="${!manyRatings}">
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
                </c:when>
                <c:otherwise>
                    <c:forEach items="${postRatings}" var="rating" varStatus="status" begin="0" end="2">
                        <aksess:getuser userid="${rating.userid}" name="user"/>
                        <c:choose>
                            <c:when test="${status.index == 0}">
                                <a href="${userProfileBaseUrl}${rating.userid}">${user.name}</a>
                            </c:when>
                            <c:when test="${status.index > 0 && status.last}">
                                <c:if test="${status.index == 2}">
                                    <kantega:label key="forum.wall.like.and" bundle="forum" locale="${forumLocale}"/>
                                    <a href="" class='oa-forum-showAllRatings'>
                                        ${fn:length(postRatings) - 2} <kantega:label key="forum.wall.likes.others" bundle="forum" locale="${forumLocale}"/>
                                    </a>
                                </c:if>
                            </c:when>
                            <c:when test="${status.index > 0 && !status.last}">
                                , <a href="${userProfileBaseUrl}${rating.userid}">${user.name}</a>
                            </c:when>
                            <c:otherwise>

                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <span class="oa-forum-minimizedRatings oa-forum-hidden">
                        <c:forEach items="${postRatings}" var="rating" varStatus="status" begin="2">
                            <aksess:getuser userid="${rating.userid}" name="user"/>
                            <c:choose>
                                <c:when test="${status.index > 0 && status.last}">
                                    <kantega:label key="forum.wall.like.and" bundle="forum" locale="${forumLocale}"/>
                                </c:when>
                                <c:when test="${status.index > 0 && !status.last}">
                                    ,
                                </c:when>
                            </c:choose>
                            <a href="${userProfileBaseUrl}${rating.userid}">${user.name}</a>
                        </c:forEach>
                    </span>
                </c:otherwise>
            </c:choose>
            <kantega:label key="forum.wall.likes.this" bundle="forum" locale="${forumLocale}"/>
        </div>
    </c:if>

    <c:if test="${postsStatus.index == 0 && !postsStatus.last && fn:length(thread.posts) > 2}">
        <div class="oa-forum-mediablock oa-forum-likes oa-forum-fadedText">
            <a href="" class='oa-forum-showFullThread <c:if test="${expandThreads}">oa-forum-hidden</c:if>'>
                <c:choose>
                    <c:when test="${fn:length(thread.posts) > 3}">
                        <kantega:label key="forum.wall.morecomments.part2" bundle="forum" locale="${forumLocale}"/>
                        ${fn:length(thread.posts)-1}
                        <kantega:label key="forum.wall.morecomments.part3" bundle="forum" locale="${forumLocale}"/>
                    </c:when>
                    <c:otherwise>
                        <kantega:label key="forum.wall.morecomments.twocomments" bundle="forum" locale="${forumLocale}"/>
                    </c:otherwise>
                </c:choose>
            </a>
            <a href="" class='oa-forum-minimizeThread <c:if test="${!expandThreads}">oa-forum-hidden</c:if>'>
                <kantega:label key="forum.wall.morecomments.collaps" bundle="forum" locale="${forumLocale}"/>
            </a>
        </div>
    </c:if>
</div>
