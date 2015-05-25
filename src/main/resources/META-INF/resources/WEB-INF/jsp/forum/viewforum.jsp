<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=utf-8" %>

<c:set var="defaultSubjecten"><kantega:label key="post.subject" bundle="forum" locale="en" /></c:set>
<c:set var="defaultSubjectno"><kantega:label key="post.subject" bundle="forum" locale="no" /></c:set>

<kantega:section id="innhold">
    <div class="forum-heading">
        <a href=""><spring:message code="forum.title"/></a> >

        <a href="viewforum?forumId=${forum.id}"><c:out value="${forum.name}"/></a>
    </div>


    <p><c:out value="${forum.description}"/></p>

    <div style="text-align: right;">
        <forum:haspermisson permission="EDIT_THREAD" object="${forum}">
            <a href="editpost?forumId=${forum.id}"><spring:message code="thread.addthread"/></a>
        </forum:haspermisson>

        <forum:haspermisson permission="EDIT_FORUM" object="${forum}">
            | <a href="editforum?forumId=${forum.id}"><spring:message code="forum.edit"/></a>
            | <a href="deleteforum?forumId=${forum.id}"><spring:message code="forum.delete"/></a>
        </forum:haspermisson>
    </div>

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
                            <a class="forum-pagenavigation<c:out value="${current}"/>" href="viewforum?forumId=${forum.id}&amp;startIndex=${index}">${status.index+1}</a>
                        </c:forEach>
                    </td>
                </tr>
            </table>

        </c:if>
        <table width="100%" cellpadding="0" cellspacing="0" class="forum-table">
            <tr class="forum-labelRow">
                <td valign="top">&nbsp;</td>
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
                    <td valign="top">
                        <c:choose>
                            <c:when test="${thread.numNewPosts > 0}">
                                <img src="../bitmaps/forum/thread_new.gif" alt="${thread.numNewPosts} <spring:message code="post.icon.newthread"/>" title="${thread.numNewPosts} <spring:message code="post.icon.newthread"/>">
                            </c:when>
                            <c:when test="${thread.numPosts > 10}">
                                <img src="../bitmaps/forum/thread_hot.gif" alt="<spring:message code="post.icon.hotthread"/>" title="<spring:message code="post.icon.hotthread"/>">
                            </c:when>
                            <c:otherwise>
                                <img src="../bitmaps/forum/thread_normal.gif" alt="">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:set var="threadname"><c:out value="${thread.lastPost.subject}"/></c:set>
                        <c:if test="${threadname eq defaultSubjecten or subject eq defaultSubjectno or fn:length(subject) eq 0}">
                            <c:set var="subject"><aksess:abbreviate maxsize="25">${thread.lastPost.body}</aksess:abbreviate></c:set>
                        </c:if>
                        <a href="viewthread?threadId=${thread.id}"><c:out value="${thread.name}"/></a>
                    </td>
                    <td valign="top">
                        <c:if test="${thread.lastPost != null}">
                            <c:set var="subject"><c:out value="${thread.lastPost.subject}"/></c:set>
                            <c:if test="${subject eq defaultSubjecten or subject eq defaultSubjectno or fn:length(subject) eq 0}">
                                <c:set var="subject"><aksess:abbreviate maxsize="25">${thread.lastPost.body}</aksess:abbreviate></c:set>
                            </c:if>
                            <a href="viewthread?threadId=${thread.lastPost.thread.id}#post_${thread.lastPost.id}">${subject}</a><br>
                            av <c:out value="${thread.lastPost.author}"/>
                        </c:if>
                    </td>

                    <td>${thread.numPosts}</td>
                </tr>
            </c:forEach>
        </table>

        <div class="forum-icon-descriptions">
            <div class="forum-icon-description">
                <img src="../bitmaps/forum/thread_normal.gif" alt="<spring:message code="post.icon.normalthread.description"/>" title="<spring:message code="post.icon.normalthread.description"/>"> <spring:message code="post.icon.normalthread.description"/>
            </div>
            <div class="forum-icon-description">
                <img src="../bitmaps/forum/thread_hot.gif" alt="<spring:message code="post.icon.hotthread.description"/>" title="<spring:message code="post.icon.hotthread.description"/>"> <spring:message code="post.icon.hotthread.description"/>
            </div>
            <div class="forum-icon-description">
                <img src="../bitmaps/forum/thread_new.gif" alt="<spring:message code="post.icon.newthread.description"/>" title="<spring:message code="post.icon.newthread.description"/>"> <spring:message code="post.icon.newthread.description"/>
            </div>
        </div>
    </c:otherwise>
    </c:choose>

</kantega:section>

<%@ include file="include/design/design.jsf" %>
