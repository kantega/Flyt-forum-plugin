<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="<aksess:geturl/>/css/wall/wall.css" media="all">

<script type="text/javascript" src="<aksess:geturl/>/js/wall/jquery-form-2.84.js"></script>
<script type="text/javascript" src="<aksess:geturl/>/js/wall/prettyDate.js"></script>
<script type="text/javascript" src="<aksess:geturl/>/js/wall/jquery.elastic-1.6.10.js"></script>
<script type="text/javascript">
// Globale variabler som servertid osv
var serverTime = "<aksess:getdate format="yyyy-MM-dd'T'HH:mm:ss"/>";
var contextPath = "<aksess:geturl/>";
var forumId = <c:out value="${forumid}" default="-1"/>;
var maxthreads = <c:out value="${maxthreads}" default="20"/>;
</script>
<script type="text/javascript" src="<aksess:geturl/>/js/wall/forum-wall-functions.js"></script>

