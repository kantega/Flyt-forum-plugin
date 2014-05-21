<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
    if (typeof jQuery == 'undefined') {
        alert("jQuery is not loaded! OpenAksess forum wall requires jQuery to function.");
    }
    $(document).ready(function(){
        $.ajaxSetup ({
            // Disable caching of AJAX responses
            cache: false
        });
    })
</script>
<script type="text/javascript" src="<kantega:expireurl url="/js/wall/forum-wall-functions.jjs"/>"></script>



