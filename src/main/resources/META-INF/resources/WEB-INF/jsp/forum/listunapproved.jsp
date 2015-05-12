<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<kantega:section id="innhold">
    <div class="forum-heading">
        <a href=""><spring:message code="forum.title"/></a> >
        <spring:message code="unapproved.title"/>
    </div>

    <c:if test="${not empty unapprovedPosts}">
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr class="forum-labelRow">
                <td valign="top" width="75%">
                    <spring:message code="unapproved.post"/>
                </td>
                <td valign="top" width="25%">
                    <spring:message code="unapproved.postdate"/>
                </td>
            </tr>
            <c:forEach var="post" items="${unapprovedPosts}" varStatus="status">
                <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                    <td valign="top">
                        <a href="viewpost?postId=<c:out value="${post.id}"/>"><c:out value="${post.subject}"/></a>
                    </td>
                    <td valign="top">
                        <fmt:formatDate value="${post.postDate}" pattern="dd.MM.yyyy"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>


</kantega:section>

<%@ include file="include/design/design.jsf" %>
