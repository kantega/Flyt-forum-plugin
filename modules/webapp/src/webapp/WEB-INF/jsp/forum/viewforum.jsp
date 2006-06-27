<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="iso-8859-1" %>

<kantega:section id="tittel">
</kantega:section>

<kantega:section id="innhold">
    <div class="forum-heading">
        <a href="."><spring:message code="forum.title"/></a> >
        
        <a href="viewforum?forumId=<c:out value="${forum.id}"/>"><c:out value="${forum.name}"/></a>
    </div>


    <p><c:out value="${forum.description}"/></p>

    <forum:haspermisson permission="EDIT_THREAD">
        <a href="editpost?forumId=<c:out value="${forum.id}"/>">Ny tråd</a>
    </forum:haspermisson>

    <forum:haspermisson permission="EDIT_FORUM" object="${forum}">
            | <a href="editforum?forumId=<c:out value="${forum.id}"/>">Endre forum</a>
            | <a href="deleteforum?forumId=<c:out value="${forum.id}"/>">Slett forum</a>
    </forum:haspermisson>


    <br>

    <c:choose>

    <c:when test="${empty threads}">
        <spring:message code="forum.empty"/>
    </c:when>
    <c:otherwise>

        <c:if test="${pages > 1}">
            <table width="100%" cellpadding="0" cellspacing="0" style="padding-bottom: 5px">
                <tr>
                    <td align="right">
                        <spring:message code="thread.pagexofy" arguments="${current+1},${pages}"/>:

                        <c:forEach var="index" items="${startindexes}" varStatus="status">
                            <c:if test="${startindex == index}">
                                <c:set var="current" value="-current"/>
                            </c:if>
                            <c:if test="${startindex != index}">
                                <c:set var="current" value=""/>
                            </c:if>
                            <a class="forum-pagenavigation<c:out value="${current}"/>" href="viewforum?forumId=<c:out value="${forum.id}"/>&amp;startIndex=<c:out value="${index}"/>"><c:out value="${status.index+1}"/></a>
                        </c:forEach>
                    </td>
                </tr>
            </table>

        </c:if>
        <table width="100%" cellpadding="0" cellspacing="0">
            <tr class="forum-labelRow">
                <td>
                    <spring:message code="thread.name"/>
                </td>
                <td>
                    <spring:message code="forumlist.lastpost"/>
                </td>
                <td>
                    <spring:message code="thread.posts"/>
                </td>

            </tr>
            <c:forEach items="${threads}" var="thread" varStatus="status">
                <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                    <td>
                        <a href="viewthread?threadId=<c:out value="${thread.id}"/>"><c:out value="${thread.name}"/></a>
                    </td>
                    <td valign="top">
                        <c:if test="${thread.lastPost != null}">
                            <a href="viewthread?threadId=<c:out value="${thread.id}"/>#post_<c:out value="${thread.lastPost.id}"/>"><c:out value="${thread.lastPost.subject}"/></a><br>
                            av <c:out value="${thread.lastPost.author}"/>
                        </c:if>
                    </td>

                    <td>
                        <c:out value="${thread.numPosts}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
    </c:choose>

</kantega:section>

<%@ include file="include/design/design.jsf" %>