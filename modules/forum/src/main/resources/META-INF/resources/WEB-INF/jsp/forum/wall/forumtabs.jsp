<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/aksess" prefix="aksess" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="oa-forum-tabs-container">
    <ul class="oa-forum-tabs">
        <li class="oa-forum-tab <c:if test="${selectedForumId <= 0}">selected</c:if>"><aksess:link queryparams="forumId=-1"><kantega:label key="forum.share.tabs.showall" bundle="forum" locale="${forumLocale}"/></aksess:link></li>
        <c:forEach var="forum" items="${forumCategory.forums}">
            <c:if test="${hiddenForumId != forum.id}">
                <li class="oa-forum-tab <c:if test="${forum.id == selectedForumId}">selected</c:if>"><aksess:link queryparams="forumId=${forum.id}">${forum.name}</aksess:link></li>
            </c:if>
        </c:forEach>
    </ul>
</div>
