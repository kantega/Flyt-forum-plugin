<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isForumWallSearch"><aksess:getconfig key="forum.wall.search" default="false"/></c:set>

<c:if test="${userCanViewForum}">
    <c:set scope="application" var="oaForumPostPreviewCharlength"><aksess:getconfig key="forum.post.previewcharlength" default="200"/></c:set>
    <c:set scope="application" var="trunctateAllPostsInThread"><aksess:getconfig key="forum.post.trunctateAllPostsInThread" default="false"/></c:set>

    <c:if test="${showForumTabs}">
        <%@include file="forumtabs.jsp"%>
    </c:if>
    <c:if test="${showSharebox}">
        <%@include file="sharebox.jsp"%>
    </c:if>
    <c:if test="${isForumWallSearch}">
        <%@include file="../flytThreadTemplates.jsp"%>
        <%@include file="search-output.jsp"%>
    </c:if>

    <div class="oa-forum-forumContent" data-forumId="${forumId}" data-forumCategoryId="${forumCategoryId}" data-forumWallUrl="${forumListPostsUrl}" data-defaultPostForumId="${defaultPostForumId}">
        <div class="oa-forum-new-posts"></div>
        <div class="oa-forum-threads"></div>
        <div class="oa-forum-wall-load-more-threads">
            <a href="#" class="button">
                <span><kantega:label key="forum.wall.loadmoreposts" bundle="forum" locale="${forumLocale}"/></span>
            </a>
        </div>
    </div>

    <script type="text/javascript">
        // Globale variabler som servertid osv
        var serverTime = "<aksess:getdate format="yyyy-MM-dd'T'HH:mm:ss"/>";
        var contextPath = "${pageContext.request.contextPath}";
        var locale = "${fn:substring(aksess_locale, 0, 2)}";
        var forumId = "${forumId}";
        var forumCategoryId = "${forumCategoryId}";
        var forumWallUrl = "${forumListPostsUrl}";
        var elva = {};
        elva.allowedFileExtensions = "${allowedFileextensions}";
    </script>
</c:if>
