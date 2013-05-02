<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set scope="application" var="oaForumPostPreviewCharlength"><aksess:getconfig key="forum.post.previewcharlength" default="200"/></c:set>

<c:if test="${showForumTabs}">
    <%@include file="forumtabs.jsp"%>
</c:if>
<c:if test="${showSharebox}">
    <%@include file="sharebox.jsp"%>
</c:if>

<div id="oa-forum-loading-animation">
    <span class="oa-forum-submit-animation"><img class="oa-forum-posting-loading-animation" src="<aksess:geturl/>/bitmaps/forum/ajax-loader.gif"><kantega:label key="forum.share.wall.loading.label" bundle="forum" locale="${forumLocale}"/></span>
</div>
<div id="submit-animation" style="display: none;">
    <span class="oa-forum-submit-animation"><img class="oa-forum-posting-loading-animation" src="<aksess:geturl/>/bitmaps/forum/ajax-loader.gif"><kantega:label key="forum.wall.submitting" bundle="forum" locale="${forumLocale}"/></span>
</div>

<div id="oa-forum-forumContent">
    <div class="oa-forum-new-posts"></div>
    <div class="oa-forum-threads"></div>
    <div id="oa-forum-wall-load-more-threads">
        <a href="#" class="button">
            <span><kantega:label key="forum.wall.loadmoreposts" bundle="forum" locale="${forumLocale}"/></span>
        </a>
    </div>
</div>