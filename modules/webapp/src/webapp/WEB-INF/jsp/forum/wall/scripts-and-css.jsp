<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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
    })
</script>
<script type="text/javascript" src="<kantega:expireurl url="/js/wall/jquery-form-2.84.js"/>"></script>
<script type="text/javascript" src="<kantega:expireurl url="/js/wall/prettyDate.js"/>"></script>
<script type="text/javascript" src="<kantega:expireurl url="/js/wall/jquery.elastic-1.6.10.js"/>"></script>
<script type="text/javascript">
    // Globale variabler som servertid osv
    var serverTime = "<aksess:getdate format="yyyy-MM-dd'T'HH:mm:ss"/>";
    var contextPath = "<aksess:geturl/>";
    var locale = "<c:out value="${fn:substring(aksess_locale, 0, 2)}"/>";
</script>
<script type="text/javascript" src="<kantega:expireurl url="/js/wall/forum-wall-functions.jjs"/>"></script>



