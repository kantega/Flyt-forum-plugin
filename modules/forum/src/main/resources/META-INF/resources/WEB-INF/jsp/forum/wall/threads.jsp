<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=utf-8" language="java" pageEncoding="iso-8859-1" %>

<c:forEach items="${threads}" var="thread" varStatus="status">
    <c:if test="${status.last && hasMorePosts}">
        <c:set var="wallHasMorePosts" value="true" scope="request"/>
    </c:if>
    <%@ include file="ajax-thread.jsp" %>
</c:forEach>