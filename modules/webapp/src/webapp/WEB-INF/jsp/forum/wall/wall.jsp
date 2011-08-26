<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${showSharebox != null}">
    <%@include file="sharebox.jsp"%>
</c:if>
<div id="oa-forum-loading-animation">
    <img style="vertical-align:middle;margin-right:10px;" src="<aksess:geturl/>/bitmaps/intranett/loading.gif"><span><kantega:label key="forum.share.wall.loading.label" bundle="forum"/></span>
</div>
<div id="oa-forum-forumContent"></div>