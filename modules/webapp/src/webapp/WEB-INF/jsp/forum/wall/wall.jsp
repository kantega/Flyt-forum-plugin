<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${showSharebox != null}">
    <%@include file="sharebox.jsp"%>
</c:if>
<div id="oa-forum-loading-animation">
    <span class="oa-forum-submit-animation"><img class="oa-forum-posting-loading-animation" src="<aksess:geturl/>/bitmaps/forum/ajax-loader.gif"><kantega:label key="forum.share.wall.loading.label" bundle="forum" locale="${forumLocale}"/></span>
</div>
<div id="oa-forum-forumContent"></div>
<script type="text/javascript">
    $(document).ready(function(){
        // Handles loading and animation of the wall.
        var $forumContent = $("#oa-forum-forumContent");
        $forumContent.hide();
        var forumWallUrl = "${forumListPostsUrl}";
        //var forumWallUrl = contextPath + "/forum/listPosts?forumId=" + forumId + "&numberOfPostsToShow=" + maxthreads;
        $forumContent.load(forumWallUrl, function(responseText, textStatus, XMLHttpRequest){
            $(".oa-forum-date", $forumContent).each(function(){
                $(this).prettyDate({serverTime: serverTime, locale: locale});
            });
            window.setTimeout(function() { // Pausing for 200 ms for better user experience
                $("#oa-forum-loading-animation").fadeOut(200, function(){
                    $forumContent.fadeIn(250, function(){

                    });
                });
            },200);
        });
    });
</script>