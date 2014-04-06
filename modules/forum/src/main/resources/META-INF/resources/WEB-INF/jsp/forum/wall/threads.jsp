<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=utf-8" %>

<c:forEach items="${threads}" var="thread" varStatus="status">
    <c:if test="${status.last && hasMorePosts}">
        <c:set var="wallHasMorePosts" value="true" scope="request"/>
    </c:if>
    <%@ include file="ajax-thread.jsp" %>
</c:forEach>