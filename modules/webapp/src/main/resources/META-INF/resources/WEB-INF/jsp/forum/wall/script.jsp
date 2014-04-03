<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>

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
<script type="text/javascript" src="<aksess:geturl />/META-INF/resources/js/wall/forum-wall-functions.jjs"></script>



