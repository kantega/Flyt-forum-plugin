<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="isForumWallSearch"><aksess:getconfig key="forum.wall.search" default="false"/></c:set>

<script type="text/javascript">
    $(document).ready(function(){
        if (typeof jQuery === undefined) {
            alert("jQuery is not loaded! OpenAksess forum wall requires jQuery to function.");
        }
        if (typeof jQuery.ui === undefined) {
            alert("jQueryUI is not loaded! OpenAksess forum wall requires jQueryUI to function.");
        }

        $.ajaxSetup ({
            // Disable caching of AJAX responses
            cache: false
        });
    });

</script>
<c:if test="${isForumWallSearch}">
    <script id="flyt-forum-search-templates" type="text/html">
        <li class="flyt-forum-search-hit">
            <div class="flyt-forum-search-hit-block">
                <div class="flyt-forum-search-hit-body">{{flyt-forum-search-hit-body}}</div>
                <div class="flyt-forum-search-hit-metadata">
                    <spab class="flyt-forum-search-hit-postDate">
                        Opprettet
                        <span class="flyt-forum-search-hit-postDate-dateTime">{{flyt-forum-search-hit-postDate}}</span>
                    </spab>
                    <span class="flyt-forum-search-hit-modifiedDate">
                        , redigert
                        <span class="flyt-forum-search-hit-modifiedDate-dateTime">{{flyt-forum-search-hit-modifiedDate}}</span>
                    </span>
                    <span class="flyt-forum-search-hit-forum">
                        i
                        <span class="flyt-forum-search-hit-forum-name">{{flyt-forum-search-hit-forum}}</span>
                    </span>
                </div>
            </div>
        </li>
    </script>
    <script type="text/javascript" src="<kantega:expireurl url="/js/wall/jquery.highlight.js"/>"></script>
    <script type="text/javascript" src="<kantega:expireurl url="/js/wall/flytThreads.js"/>"></script>
</c:if>
<script type="text/javascript" src="<kantega:expireurl url="/js/wall/forum-wall-functions.jjs"/>"></script>



