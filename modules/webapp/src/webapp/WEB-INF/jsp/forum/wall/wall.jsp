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
<script type="text/javascript" src="<kantega:expireurl url="/js/wall/jquery.timers.js"/>"></script>
<script type="text/javascript">

    $(document).ready(function(){
        var loadTime = new Date().getTime();
        var newThreadsTemplate = "<kantega:label key="forum.wall.newThreads" bundle="forum" locale="${forumLocale}"/>";
        // Handles loading and animation of the wall.
        loadWallThreads();
        $("#oa-forum-wall-load-more-threads a").live("click", function(event){
            event.preventDefault();
            loadWallThreads();
            $(this).parent().hide();
        });
        $("#oa-forum-forumContent .oa-forum-new-posts").everyTime(10000, function() {
            $.get("<aksess:geturl url="/forum/numberOfNewThreads"/>" , {forumId:1, timeStamp:loadTime}, function(data){
                           if(data.numberOfNewThreads > 0){
                               var loadNewThreads = $('<a class="numberOfNewThreads" href="" onclick="javascript:loadWallThreads()">'+newThreadsTemplate.replace('$$', data.numberOfNewThreads)+'</a>');
                                $(".oa-forum-new-posts").html(loadNewThreads);
                           }
                    }
            )})
    });

    function loadWallThreads() {
        var $forumContent = $("#oa-forum-forumContent .oa-forum-threads");
        var noThreads = $(".oa-forum-thread", $forumContent).length;
        var forumWallUrl = "${forumListPostsUrl}";
        if (noThreads > 0) {
            forumWallUrl += "&offset=" + noThreads;
        }
        $.get(forumWallUrl, function(data){
            $("#oa-forum-loading-animation").fadeOut(150, function(){
                $forumContent = $("#oa-forum-forumContent .oa-forum-threads");
                var $processedData = $('<div></div>').html(data);
                $processedData.find(".oa-forum-sharefield").ata();
                $forumContent.append($processedData.children());
                $(".oa-forum-date", $forumContent).each(function(){
                    $(this).prettyDate({serverTime: serverTime, locale: locale});
                });
                if ($(".oa-forum-thread:last-child", $forumContent).hasClass("oa-forum-thread-has-more-posts")) {
                    $("#oa-forum-wall-load-more-threads").show();
                }
                $("body").trigger('oa.forumwall.loaded');
            });
        });
    }


</script>