<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set scope="application" var="oaForumPostPreviewCharlength"><aksess:getconfig key="forum.post.previewcharlength" default="200"/></c:set>

<c:if test="${showForumTabs}">
    <%@include file="forumtabs.jsp"%>
</c:if>
<c:if test="${showSharebox}">
    <%@include file="sharebox.jsp"%>
</c:if>

<div id="oa-forum-forumContent">
    <div class="oa-forum-new-posts"></div>
    <div class="oa-forum-threads"></div>
    <div id="oa-forum-wall-load-more-threads">
        <a href="#" class="button">
            <span><kantega:label key="forum.wall.loadmoreposts" bundle="forum" locale="${forumLocale}"/></span>
        </a>
    </div>
</div>

<script type="text/javascript">
    // Globale variabler som servertid osv
    var serverTime = "<aksess:getdate format="yyyy-MM-dd'T'HH:mm:ss"/>";
    var contextPath = "<aksess:geturl/>";
    var locale = "<c:out value="${fn:substring(aksess_locale, 0, 2)}"/>";
    var forumId = "<c:out value="${forumId}"/>";
    var forumCategoryId = "<c:out value="${forumCategoryId}"/>";
    var forumWallUrl = "${forumListPostsUrl}";
</script>