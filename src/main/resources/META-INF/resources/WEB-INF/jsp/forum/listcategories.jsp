<%@ page contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.kantega.no/aksess/tags/commons" prefix="kantega" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="forum" uri="http://www.kantega.no/aksess/tags/forum" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="aksess" uri="http://www.kantega.no/aksess/tags/aksess" %>

<kantega:section id="tittel">
    <spring:message code="forum.title"/>
</kantega:section>

<c:set var="defaultSubjecten"><kantega:label key="post.subject" bundle="forum" locale="en" /></c:set>
<c:set var="defaultSubjectno"><kantega:label key="post.subject" bundle="forum" locale="no" /></c:set>

<kantega:section id="innhold">
    <div class="forum-heading">
        <spring:message code="forum.title"/>
    </div>

    <c:if test="${unapprovedPostCount != null && unapprovedPostCount > 0}">
        <div class="forum-message">
        <a href="listunapproved"><spring:message code="forumcategory.unapprovedposts"/> <strong>${unapprovedPostCount}</strong> <spring:message code="forumcategory.unapprovedposts2"/></a>
        </div>
    </c:if>

    <kantega:section id="cats">
        <table width="100%" cellpadding="0" cellspacing="0" border="0" class="forum-table">

            <c:forEach items="${categories}" var="category">
                <c:set var="hascats" value="true"/>
                <tr class="forum-labelRow">
                    <td valign="top">&nbsp;</td>
                    <td valign="top">
                        <c:set var="canedit" value="false"/>
                        <forum:haspermisson permission="EDIT_CATEGORY" object="${category}">
                            <a href="viewcategory?categoryId=${category.id}"><c:out value="${category.name}"/></a>
                            <c:set var="canedit" value="true"/>
                        </forum:haspermisson>
                        <c:if test="${canedit != 'true'}">
                            <c:out value="${category.name}"/>
                        </c:if>
                    </td>
                    <td>
                        <spring:message code="forumlist.lastpost"/>
                    </td>
                    <td>
                        <spring:message code="forum.threads"/>
                    </td>
                </tr>

                <c:forEach var="forum" items="${category.forums}"  varStatus="status">
                    <tr class="forum-tableRow<c:out value="${status.index mod 2}"/>">
                        <td valign="top">
                            <c:choose>
                                <c:when test="${forum.numNewPosts > 0}">
                                    <img src="../bitmaps/forum/forum_new.gif" alt="${forum.numNewPosts} <spring:message code="post.icon.newforum"/>" title="${forum.numNewPosts} <spring:message code="post.icon.newforum"/>">
                                </c:when>
                                <c:when test="${forum.numThreads > 10}">
                                    <img src="../bitmaps/forum/forum_hot.gif" alt="<spring:message code="post.icon.hotforum"/>" title="<spring:message code="post.icon.hotforum"/>">
                                </c:when>
                                <c:otherwise>
                                    <img src="../bitmaps/forum/forum_normal.gif" alt="">
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td valign="top">
                            <a href="viewforum?forumId=${forum.id}"><c:out value="${forum.name}"/></a>
                            <c:if test="${forum.description != ''}">
                                <div style="padding-top: 4px"><c:out value="${forum.description}"/></div>
                            </c:if>

                        </td>
                        <td valign="top">
                            <c:if test="${forum.lastPost != null}">
                                <c:set var="subject"><c:out value="${forum.lastPost.subject}"/></c:set>
                                <c:if test="${subject eq defaultSubjecten or subject eq defaultSubjectno or fn:length(subject) eq 0}">
                                    <c:set var="subject"><aksess:abbreviate maxsize="25">${forum.lastPost.body}</aksess:abbreviate></c:set>
                                </c:if>
                                <a href="viewthread?threadId=${forum.lastPost.thread.id}#post_${forum.lastPost.id}">${subject}</a><br>
                                av <c:out value="${forum.lastPost.author}"/>
                            </c:if>
                        </td>
                        <td valign="top">
                            ${forum.numThreads}
                        </td>

                    </tr>
                </c:forEach>
            </c:forEach>
        </table>

        <div class="forum-icon-descriptions">
            <div class="forum-icon-description">
                <img src="../bitmaps/forum/forum_normal.gif" alt="<spring:message code="post.icon.normalforum.description"/>" title="<spring:message code="post.icon.normalforum.description"/>"> <spring:message code="post.icon.normalforum.description"/>
            </div>
            <div class="forum-icon-description">
                <img src="../bitmaps/forum/forum_hot.gif" alt="<spring:message code="post.icon.hotforum.description"/>" title="<spring:message code="post.icon.hotforum.description"/>"> <spring:message code="post.icon.hotforum.description"/>
            </div>
            <div class="forum-icon-description">
                <img src="../bitmaps/forum/forum_new.gif" alt="<spring:message code="post.icon.newforum.description"/>" title="<spring:message code="post.icon.newforum.description"/>"> <spring:message code="post.icon.newforum.description"/>
            </div>
        </div>
    </kantega:section>
    <c:choose>
        <c:when test="${hascats != 'true'}">
            <spring:message code="forumlist.empty"/>
        </c:when>

        <c:otherwise>
            <kantega:getsection id="cats"/>
        </c:otherwise>

    </c:choose>
    <forum:haspermisson permission="EDIT_CATEGORY">
        <br><br>
        <a href="editcategory"><spring:message code="forumcategory.addcategory"/></a>
    </forum:haspermisson>
    <br>Sist besøk: <c:out value="${lastVisit}"/>
</kantega:section>

<%@ include file="include/design/design.jsf" %>
