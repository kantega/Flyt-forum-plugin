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