<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>

<link rel="stylesheet" type="text/css" href="<kantega:expireurl url="/css/wall/wall.css"/>" media="all">
<link rel="stylesheet" type="text/css" href="<kantega:expireurl url="/css/wall/wall-theme.css"/>" media="all">
<script type="text/javascript">
    if (typeof jQuery == 'undefined') {
        alert("jQuery is not loaded! OpenAksess forum wall requires jQuery to function.");
    }
    $(document).ready(function(){
        $.ajaxSetup ({
            // Disable caching of AJAX responses
            cache: false
        });
    });

</script><c:if test="${isForumWallSearch}">
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
    <script type="text/javascript" src="<kantega:expireurl url="/js/wall/flytThreads.js"/>"></script>
</c:if>
<script type="text/javascript" src="<kantega:expireurl url="/js/wall/forum-wall-functions.jjs"/>"></script>



